package tradingapp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Holidays {

  @Id
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private long id;
  
  @Column(name = "holiday")
  @Temporal(TemporalType.DATE)
  private Date holiday;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getHoliday() {
    return holiday;
  }

  public void setHoliday(Date holiday) {
    this.holiday = holiday;
  }
  
}
