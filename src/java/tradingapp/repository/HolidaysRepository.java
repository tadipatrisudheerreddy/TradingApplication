package tradingapp.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tradingapp.model.Holidays;

@Repository
public interface HolidaysRepository extends JpaRepository<Holidays, Long> {
  
  public Holidays findByHoliday(Date inputDate);
  
}
