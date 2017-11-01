package tradingapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class UserDTO {

  @JsonProperty("pass_key")
  private String passKey;

  public String getPassKey() {
    return passKey;
  }

  public void setPassKey(String passKey) {
    this.passKey = passKey;
  }
  
  /*@JsonProperty("username")
  private String username;
  
  @JsonProperty("password")
  private String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }*/
  
}
