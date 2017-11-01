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
public class OpenInterest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "date")
  @Temporal(TemporalType.DATE)
  private Date date;

  @Column(name = "client_type")
  private String clientType;

  @Column(name = "future_index_long")
  private Long futureIndexLong;

  @Column(name = "future_index_short")
  private Long futureIndexShort;

  @Column(name = "future_stock_long")
  private Long futureStockLong;

  @Column(name = "future_stock_short")
  private Long futureStockShort;

  @Column(name = "option_index_call_long")
  private Long optionIndexCallLong;

  @Column(name = "option_index_put_long")
  private Long optionIndexPutLong;

  @Column(name = "option_index_call_short")
  private Long optionIndexCallShort;

  @Column(name = "option_index_put_phort")
  private Long optionIndexPutShort;

  @Column(name = "option_stock_call_long")
  private Long optionStockCallLong;

  @Column(name = "option_stock_put_long")
  private Long optionStockPutLong;

  @Column(name = "option_stock_call_short")
  private Long optionStockCallShort;

  @Column(name = "option_stock_put_short")
  private Long optionStockPutShort;

  @Column(name = "total_long_contracts")
  private Long totalLongContracts;

  @Column(name = "total_short_contracts")
  private Long totalShortContracts;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getClientType() {
    return clientType;
  }

  public void setClientType(String clientType) {
    this.clientType = clientType;
  }

  public Long getFutureIndexLong() {
    return futureIndexLong;
  }

  public void setFutureIndexLong(Long futureIndexLong) {
    this.futureIndexLong = futureIndexLong;
  }

  public Long getFutureIndexShort() {
    return futureIndexShort;
  }

  public void setFutureIndexShort(Long futureIndexShort) {
    this.futureIndexShort = futureIndexShort;
  }

  public Long getFutureStockLong() {
    return futureStockLong;
  }

  public void setFutureStockLong(Long futureStockLong) {
    this.futureStockLong = futureStockLong;
  }

  public Long getFutureStockShort() {
    return futureStockShort;
  }

  public void setFutureStockShort(Long futureStockShort) {
    this.futureStockShort = futureStockShort;
  }

  public Long getOptionIndexCallLong() {
    return optionIndexCallLong;
  }

  public void setOptionIndexCallLong(Long optionIndexCallLong) {
    this.optionIndexCallLong = optionIndexCallLong;
  }

  public Long getOptionIndexPutLong() {
    return optionIndexPutLong;
  }

  public void setOptionIndexPutLong(Long optionIndexPutLong) {
    this.optionIndexPutLong = optionIndexPutLong;
  }

  public Long getOptionIndexCallShort() {
    return optionIndexCallShort;
  }

  public void setOptionIndexCallShort(Long optionIndexCallShort) {
    this.optionIndexCallShort = optionIndexCallShort;
  }

  public Long getOptionIndexPutShort() {
    return optionIndexPutShort;
  }

  public void setOptionIndexPutShort(Long optionIndexPutShort) {
    this.optionIndexPutShort = optionIndexPutShort;
  }

  public Long getOptionStockCallLong() {
    return optionStockCallLong;
  }

  public void setOptionStockCallLong(Long optionStockCallLong) {
    this.optionStockCallLong = optionStockCallLong;
  }

  public Long getOptionStockPutLong() {
    return optionStockPutLong;
  }

  public void setOptionStockPutLong(Long optionStockPutLong) {
    this.optionStockPutLong = optionStockPutLong;
  }

  public Long getOptionStockCallShort() {
    return optionStockCallShort;
  }

  public void setOptionStockCallShort(Long optionStockCallShort) {
    this.optionStockCallShort = optionStockCallShort;
  }

  public Long getOptionStockPutShort() {
    return optionStockPutShort;
  }

  public void setOptionStockPutShort(Long optionStockPutShort) {
    this.optionStockPutShort = optionStockPutShort;
  }

  public Long getTotalLongContracts() {
    return totalLongContracts;
  }

  public void setTotalLongContracts(Long totalLongContracts) {
    this.totalLongContracts = totalLongContracts;
  }

  public Long getTotalShortContracts() {
    return totalShortContracts;
  }

  public void setTotalShortContracts(Long totalShortContracts) {
    this.totalShortContracts = totalShortContracts;
  }

}