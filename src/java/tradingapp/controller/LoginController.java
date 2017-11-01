package tradingapp.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tradingapp.dto.UserDTO;

@Controller
public class LoginController {
	
	@Autowired
	HttpServletRequest request;
	
  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public String login(@ModelAttribute UserDTO userDto) {
    if(userDto.getPassKey().equals("prathap")) {
    	request.getSession().setAttribute("user", userDto);
      return "redirect:home";  
    }
    return "index";
  }
  
  
  @RequestMapping(value = "/home", method = RequestMethod.GET)
  public String dashboard() {
	  UserDTO userDt= (UserDTO) request.getSession().getAttribute("user");
	  if(userDt == null)  return "index";
    return "dashboard";
  }
}