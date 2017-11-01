package tradingapp.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import tradingapp.model.OpenInterest;

@Repository
public interface OpenInterestRepository extends JpaRepository<OpenInterest, Long>{

  @Query("select oi from OpenInterest oi where date = CURRENT_DATE() and client_type='FII'")
  public OpenInterest getOpenInterest();
  
  public List<OpenInterest> findByDate(Date inputDate);
}
