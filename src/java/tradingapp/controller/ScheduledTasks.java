package tradingapp.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTasks {/*

  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
  
  //@Scheduled(cron = "0 0 22 1/1 * ? *")
  //@Scheduled(cron = "0 0/1 * 1/1 * ? *")
  public void getOpenInterestData() {
    String url = "https://www.nseindia.com/content/nsccl/fao_participant_oi_12082016.csv";
    try {
      Document documet = Jsoup.connect(url).get();
      System.out.println(documet.title());
      
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
  }
  

      //@Scheduled(fixedRate = 5000)
      public void reportCurrentTime() {
          System.out.println("The time is now " + dateFormat.format(new Date()));
          
          String url = "https://www.nseindia.com/content/nsccl/fao_participant_oi_12082016.csv";
          try {
            File baseDir = new File(System.getProperty("java.io.tmpdir"));
            File tempFile = new File(baseDir.getAbsolutePath());
            byte[] bytes = Jsoup.connect(url).ignoreContentType(true).execute().bodyAsBytes();
            org.apache.commons.io.FileUtils.writeByteArrayToFile(tempFile, bytes);
            System.out.println(baseDir.getName());
          } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          
        
      }

  @Scheduled(fixedDelay = 1200000)
  public void herokuNotIdle() {
    try {
      RestTemplate restTemplate = new RestTemplate();
      restTemplate.getForObject("https://technicaltrader.herokuapp.com/", Object.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }*/
}