package tradingapp.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tradingapp.dto.OpenInterestDTO;
import tradingapp.model.OpenInterest;
import tradingapp.repository.OpenInterestRepository;
import tradingapp.service.TradingService;

@Controller
public class TradingController {

  @Autowired
  private OpenInterestRepository openInterestRepository;
  
  @Autowired
  private TradingService tradingService;
  
  @RequestMapping("/")
  public String greeting() {
      return "index";
  }
  
  @RequestMapping("/oidata") 
  public @ResponseBody List<OpenInterest> getOpenInterestDataForToday() {
   return openInterestRepository.findByDate(new Date()); 
  }
  
  @RequestMapping(value = "/oidata/interval", method=RequestMethod.POST) 
  public @ResponseBody Map<String, OpenInterestDTO> getOpenInterestDataForInterval(@RequestParam(value="from") Long from, @RequestParam(value="to") Long to) {
    return tradingService.getOpenInterestDataForInterval(new Date(from), new Date(to)); 
  }
  
}