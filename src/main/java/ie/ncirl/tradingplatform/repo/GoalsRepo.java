package ie.ncirl.tradingplatform.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ie.ncirl.tradingplatform.model.Goal;

@Repository
public interface GoalsRepo extends JpaRepository<Goal, Long>{

	List<Goal> findAllByAccountId(Long Id);
}
