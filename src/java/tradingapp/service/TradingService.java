package tradingapp.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tradingapp.dto.OpenInterestDTO;
import tradingapp.enums.ClientType;
import tradingapp.model.OpenInterest;
import tradingapp.repository.HolidaysRepository;
import tradingapp.repository.OpenInterestRepository;
import tradingapp.util.DateTimeUtil;

@Service
public class TradingService {

  @Autowired
  private OpenInterestRepository openInterestRepository;

  @Autowired
  private HolidaysRepository holidaysRepository;

  public Map<String, OpenInterestDTO> getOpenInterestDataForInterval(Date from, Date to) {
    if (isInValidateInput(from, to)) {
      return null;
    }
    Map<String, OpenInterestDTO> mapOfClientTypeAndOpenInterestDto = new HashMap<>();
    try {
      Calendar start = Calendar.getInstance();
      start.setTime(from);
      Calendar end = Calendar.getInstance();
      end.setTime(to);

      List<OpenInterest> listOfOpenInterestModels = new ArrayList<>();
      while (!start.after(end)) {
        Date targetDay = start.getTime();

        if (DateTimeUtil.isWeekendDay(targetDay) || isHoliday(targetDay) || (targetDay.after(new Date()))) {
          start.add(Calendar.DATE, 1);
          continue;
        }
        listOfOpenInterestModels = openInterestRepository.findByDate(targetDay);

        if (listOfOpenInterestModels.size() < 4) {
          listOfOpenInterestModels = getOpenInterestDataFromNSEByInputDate(targetDay);
        }

        if (listOfOpenInterestModels.size() != 4) {
          throw new Exception("There should be 4 records always in the list");
        }
        constructOpenInterestDataByClientType(mapOfClientTypeAndOpenInterestDto, targetDay, listOfOpenInterestModels);
        start.add(Calendar.DATE, 1);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return mapOfClientTypeAndOpenInterestDto;
  }

  private boolean isInValidateInput(Date from, Date to) {
    Calendar currentDate = Calendar.getInstance();
    Calendar fromDate = Calendar.getInstance();
    fromDate.setTime(from);
    Calendar toDate = Calendar.getInstance();
    toDate.setTime(to);

    if (fromDate == null || toDate == null)
      return true;
    if (fromDate.after(toDate))
      return true;

    if (toDate.after(currentDate))
      return true;

    if (fromDate.get(Calendar.YEAR) < 2016 || toDate.get(Calendar.YEAR) < 2016) {
      return true;
    }

    if (fromDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
        && fromDate.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR)) {
      if (currentDate.get(Calendar.HOUR_OF_DAY) < 17) {
        return true;
      }
    }

    if (toDate.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)
        && toDate.get(Calendar.DAY_OF_YEAR) == currentDate.get(Calendar.DAY_OF_YEAR)) {
      if (currentDate.get(Calendar.HOUR_OF_DAY) < 18) {
        return true;
      }
    }

    long diffInMillies = to.getTime() - from.getTime();
    if (diffInMillies / (1000 * 60 * 60 * 24) > 10) {
      return true;
    }

    return false;
  }

  public void constructOpenInterestDataByClientType(Map<String, OpenInterestDTO> mapOfClientTypeAndOpenInterestDto,
      Date targetDay, List<OpenInterest> listOfOpenInterestModels) {
    OpenInterestDTO openInterestDto;
    for (OpenInterest openInterest : listOfOpenInterestModels) {
      if (openInterest.getClientType().equals(ClientType.CLIENT.getName())) {
        if (mapOfClientTypeAndOpenInterestDto.containsKey(ClientType.CLIENT.getName())) {
          openInterestDto = mapOfClientTypeAndOpenInterestDto.get(ClientType.CLIENT.getName());
        } else {
          openInterestDto = new OpenInterestDTO();
        }
        addUpOpenInterest(openInterestDto, openInterest);
        mapOfClientTypeAndOpenInterestDto.put(ClientType.CLIENT.getName(), openInterestDto);
      } else if (openInterest.getClientType().equals(ClientType.FII.getName())) {
        if (mapOfClientTypeAndOpenInterestDto.containsKey(ClientType.FII.getName())) {
          openInterestDto = mapOfClientTypeAndOpenInterestDto.get(ClientType.FII.getName());
        } else {
          openInterestDto = new OpenInterestDTO();
        }
        addUpOpenInterest(openInterestDto, openInterest);
        mapOfClientTypeAndOpenInterestDto.put(ClientType.FII.getName(), openInterestDto);
      }
    }
  }

  private List<OpenInterest> getOpenInterestDataFromNSEByInputDate(Date targetDate) throws Exception {
    List<OpenInterest> tempListOfOpenInterest = new ArrayList<>();
    try {

      File currentDayFile = null;
      Calendar currentDate = Calendar.getInstance();
      currentDate.setTime(targetDate);
      int maxRetries = 0;
      while (currentDayFile == null) {
        if (DateTimeUtil.isWeekendDay(currentDate.getTime()) || isHoliday(currentDate.getTime())
            || (currentDate.after(Calendar.getInstance()))) {
          currentDate.add(Calendar.DAY_OF_MONTH, 1);
          continue;
        }

        /*
         * below condition is to avoid 2 soncecutive holidays problem - not a good way; need to
         * think of something better
         */
        tempListOfOpenInterest = openInterestRepository.findByDate(currentDate.getTime());
        if (tempListOfOpenInterest.size() == 4)
          return tempListOfOpenInterest;

        String url = "https://www.nseindia.com/content/nsccl/fao_participant_oi_"
            + DateTimeUtil.getNumberedDate(currentDate.getTime()) + ".csv";
        try {
          // File baseDir = new File(System.getProperty("java.io.tmpdir"));
          // File tempFile = new File(baseDir.getAbsolutePath());
          File tempFile = new File("./temp2.csv");
          byte[] bytes = Jsoup.connect(url).ignoreContentType(true).execute().bodyAsBytes();
          org.apache.commons.io.FileUtils.writeByteArrayToFile(tempFile, bytes);
          currentDayFile = tempFile;
        } catch (IOException e) {
          e.printStackTrace();
          currentDate.add(Calendar.DAY_OF_MONTH, 1);
          continue;
        }

        if (++maxRetries > 10)
          throw new Exception("Max reties acheived, retuning exception..");
      }

      File prevDayFile = null;
      Calendar prevDate = Calendar.getInstance();
      prevDate.setTime(targetDate);
      maxRetries = 0;
      while (prevDayFile == null) {
        prevDate.add(Calendar.DAY_OF_MONTH, -1);
        if (DateTimeUtil.isWeekendDay(prevDate.getTime()) || isHoliday(prevDate.getTime())
            || (prevDate.after(Calendar.getInstance()))) {
          continue;
        }
        // DateTimeUtil.getNumberedDate(prevDate.getTime());
        String url = "https://www.nseindia.com/content/nsccl/fao_participant_oi_"
            + DateTimeUtil.getNumberedDate(prevDate.getTime()) + ".csv";
        try {
          // File baseDir = new File("/tempFile.csv");
          // File tempFile = new File(baseDir.getAbsolutePath());
          File tempFile = new File("./temp1.csv");
          byte[] bytes = Jsoup.connect(url).ignoreContentType(true).execute().bodyAsBytes();
          org.apache.commons.io.FileUtils.writeByteArrayToFile(tempFile, bytes);
          prevDayFile = tempFile;
        } catch (IOException e) {
          e.printStackTrace();
          currentDate.add(Calendar.DAY_OF_MONTH, 1);
          continue;
        }
        if (++maxRetries > 10)
          throw new Exception("Max reties acheived, retuning exception..");
      }

      if (prevDayFile != null && currentDayFile != null) {
        Map<String, OpenInterest> mapOfClientTypeAndOpenInterest = new HashMap<>();
        OpenInterest openInterest = null;
        String line = "";
        String cvsSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(currentDayFile.getPath()))) {
          int counter = 0;
          while ((line = br.readLine()) != null) {
            if (++counter <= 2 || counter == 7) {
              continue;
            }
            String[] values = line.split(cvsSplitBy);
            openInterest = setCurrentDayOpenInterestValues(values);
            mapOfClientTypeAndOpenInterest.put(values[0], openInterest);
          }
        } catch (IOException e) {
          e.printStackTrace();
          throw e;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(prevDayFile))) {
          int counter = 0;
          while ((line = br.readLine()) != null) {
            if (++counter <= 2 || counter == 7) {
              continue;
            }
            String[] values = line.split(cvsSplitBy);

            openInterest = mapOfClientTypeAndOpenInterest.get(values[0]);
            openInterest = setPrevDayOpenInterestValues(openInterest, values);
            openInterest.setClientType(values[0]);
            openInterest.setDate(currentDate.getTime());
            openInterestRepository.save(openInterest);
            tempListOfOpenInterest.add(openInterest);
          }
        } catch (IOException e) {
          e.printStackTrace();
          throw e;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return tempListOfOpenInterest;
  }

  public OpenInterest setCurrentDayOpenInterestValues(String[] values) {
    OpenInterest openInterest = new OpenInterest();
    openInterest.setClientType(values[0]);

    openInterest.setFutureIndexLong(Long.valueOf(values[1]));
    openInterest.setFutureIndexShort(Long.valueOf(values[2]));
    openInterest.setFutureStockLong(Long.valueOf(values[3]));
    openInterest.setFutureStockShort(Long.valueOf(values[4]));

    openInterest.setOptionIndexCallLong(Long.valueOf(values[5]));
    openInterest.setOptionIndexPutLong(Long.valueOf(values[6]));
    openInterest.setOptionIndexCallShort(Long.valueOf(values[7]));
    openInterest.setOptionIndexPutShort(Long.valueOf(values[8]));

    openInterest.setOptionStockCallLong(Long.valueOf(values[9]));
    openInterest.setOptionStockPutLong(Long.valueOf(values[10]));
    openInterest.setOptionStockCallShort(Long.valueOf(values[11]));
    openInterest.setOptionStockPutShort(Long.valueOf(values[12]));

    openInterest.setTotalLongContracts(Long.valueOf(values[13]));
    openInterest.setTotalShortContracts(Long.valueOf(values[14]));
    return openInterest;
  }

  public OpenInterest setPrevDayOpenInterestValues(OpenInterest openInterest, String[] values) {
    openInterest.setFutureIndexLong(openInterest.getFutureIndexLong() - Long.valueOf(values[1]));
    openInterest.setFutureIndexShort(openInterest.getFutureIndexShort() - Long.valueOf(values[2]));
    openInterest.setFutureStockLong(openInterest.getFutureStockLong() - Long.valueOf(values[3]));
    openInterest.setFutureStockShort(openInterest.getFutureStockShort() - Long.valueOf(values[4]));

    openInterest.setOptionIndexCallLong(openInterest.getOptionIndexCallLong() - Long.valueOf(values[5]));
    openInterest.setOptionIndexPutLong(openInterest.getOptionIndexPutLong() - Long.valueOf(values[6]));
    openInterest.setOptionIndexCallShort(openInterest.getOptionIndexCallShort() - Long.valueOf(values[7]));
    openInterest.setOptionIndexPutShort(openInterest.getOptionIndexPutShort() - Long.valueOf(values[8]));

    openInterest.setOptionStockCallLong(openInterest.getOptionStockCallLong() - Long.valueOf(values[9]));
    openInterest.setOptionStockPutLong(openInterest.getOptionStockPutLong() - Long.valueOf(values[10]));
    openInterest.setOptionStockCallShort(openInterest.getOptionStockCallShort() - Long.valueOf(values[11]));
    openInterest.setOptionStockPutShort(openInterest.getOptionStockPutShort() - Long.valueOf(values[12]));

    openInterest.setTotalLongContracts(openInterest.getTotalLongContracts() - Long.valueOf(values[13]));
    openInterest.setTotalShortContracts(openInterest.getTotalShortContracts() - Long.valueOf(values[14]));
    return openInterest;
  }

  private boolean isHoliday(Date inputDate) {
    if (holidaysRepository.findByHoliday(inputDate) != null) {
      return true;
    }
    return false;
  }

  public void addUpOpenInterest(OpenInterestDTO openInterestDto, OpenInterest openInterest) {
    if (openInterestDto.getFutureIndexLong() == null) {
      openInterestDto.setFutureIndexLong(openInterest.getFutureIndexLong());
    } else {
      openInterestDto.setFutureIndexLong(openInterestDto.getFutureIndexLong() + openInterest.getFutureIndexLong());
    }

    if (openInterestDto.getFutureIndexShort() == null) {
      openInterestDto.setFutureIndexShort(openInterest.getFutureIndexShort());
    } else {
      openInterestDto.setFutureIndexShort(openInterestDto.getFutureIndexShort() + openInterest.getFutureIndexShort());
    }

    if (openInterestDto.getFutureStockLong() == null) {
      openInterestDto.setFutureStockLong(openInterest.getFutureStockLong());
    } else {
      openInterestDto.setFutureStockLong(openInterestDto.getFutureStockLong() + openInterest.getFutureStockLong());
    }

    if (openInterestDto.getFutureStockShort() == null) {
      openInterestDto.setFutureStockShort(openInterest.getFutureStockShort());
    } else {
      openInterestDto.setFutureStockShort(openInterestDto.getFutureStockShort() + openInterest.getFutureStockShort());
    }

    if (openInterestDto.getOptionIndexCallLong() == null) {
      openInterestDto.setOptionIndexCallLong(openInterest.getOptionIndexCallLong());
    } else {
      openInterestDto.setOptionIndexCallLong(openInterestDto.getOptionIndexCallLong() + openInterest.getOptionIndexCallLong());
    }

    if (openInterestDto.getOptionIndexCallShort() == null) {
      openInterestDto.setOptionIndexCallShort(openInterest.getOptionIndexCallShort());
    } else {
      openInterestDto.setOptionIndexCallShort(openInterestDto.getOptionIndexCallShort() + openInterest.getOptionIndexCallShort());
    }

    if (openInterestDto.getOptionIndexPutLong() == null) {
      openInterestDto.setOptionIndexPutLong(openInterest.getOptionIndexPutLong());
    } else {
      openInterestDto.setOptionIndexPutLong(openInterestDto.getOptionIndexPutLong() + openInterest.getOptionIndexPutLong());
    }

    if (openInterestDto.getOptionIndexPutShort() == null) {
      openInterestDto.setOptionIndexPutShort(openInterest.getOptionIndexPutShort());
    } else {
      openInterestDto.setOptionIndexPutShort(openInterestDto.getOptionIndexPutShort() + openInterest.getOptionIndexPutShort());
    }

    if (openInterestDto.getOptionStockCallLong() == null) {
      openInterestDto.setOptionStockCallLong(openInterest.getOptionStockCallLong());
    } else {
      openInterestDto.setOptionStockCallLong(openInterestDto.getOptionStockCallLong() + openInterest.getOptionStockCallLong());
    }

    if (openInterestDto.getOptionStockCallShort() == null) {
      openInterestDto.setOptionStockCallShort(openInterest.getOptionStockCallShort());
    } else {
      openInterestDto.setOptionStockCallShort(openInterestDto.getOptionStockCallShort() + openInterest.getOptionStockCallShort());
    }

    if (openInterestDto.getOptionStockPutLong() == null) {
      openInterestDto.setOptionStockPutLong(openInterest.getOptionStockPutLong());
    } else {
      openInterestDto.setOptionStockPutLong(openInterestDto.getOptionStockPutLong() + openInterest.getOptionStockPutLong());
    }

    if (openInterestDto.getOptionStockPutShort() == null) {
      openInterestDto.setOptionStockPutShort(openInterest.getOptionStockPutShort());
    } else {
      openInterestDto.setOptionStockPutShort(openInterestDto.getOptionStockPutShort() + openInterest.getOptionStockPutShort());
    }

    if (openInterestDto.getTotalLongContracts() == null) {
      openInterestDto.setTotalLongContracts(openInterest.getTotalLongContracts());
    } else {
      openInterestDto.setTotalLongContracts(openInterestDto.getTotalLongContracts() + openInterest.getTotalLongContracts());
    }

    if (openInterestDto.getTotalShortContracts() == null) {
      openInterestDto.setTotalShortContracts(openInterest.getTotalShortContracts());
    } else {
      openInterestDto.setTotalShortContracts(openInterestDto.getTotalShortContracts() + openInterest.getTotalShortContracts());
    }
  }
}
