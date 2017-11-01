package tradingapp.repositoryImpl;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tradingapp.model.OpenInterest;
import tradingapp.repository.OpenInterestRepository;

@Component
public class OpenInterestRepositoryImpl {
  
  @Autowired 
  private OpenInterestRepository openInterestRepository;
  
  @Transactional
  public void save(OpenInterest openInterest) {
    openInterestRepository.save(openInterest);
  }
  
}
