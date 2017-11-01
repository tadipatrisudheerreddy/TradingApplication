package tradingapp.enums;

public enum ClientType {
  CLIENT("Client"), FII("FII"), DII("DII"),PRO("Pro");

  private String name;
  
  private ClientType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  
}
