package ie.ncirl.tradingplatform.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ie.ncirl.tradingplatform.dto.GoalsRequest;
import ie.ncirl.tradingplatform.model.Account;
import ie.ncirl.tradingplatform.model.Goal;
import ie.ncirl.tradingplatform.repo.GoalsRepo;
import ie.ncirl.tradingplatform.vo.GoalVo;

@Service
public class GoalsService {

	@Autowired
	private GoalsRepo goalsRepo;
	
	public void createGoals(Account account, GoalsRequest goalsRequest){
		Goal goals =Goal.builder().account(account).name(goalsRequest.getName()).startDate(goalsRequest.getStartDate())
				.endDate(goalsRequest.getEndDate()).targetAmount(goalsRequest.getTargetAmount())
				.percent(goalsRequest.getPercent()).build();
		goalsRepo.save(goals);
	}
	
	
	public List<GoalVo> getGoals(Account account){
		List<Goal> goals=goalsRepo.findAllByAccountId(account.getId());
		List<GoalVo> goalsVos=new ArrayList<>();
		Double balance=account.getBalance();
		SimpleDateFormat sf=new SimpleDateFormat("DD-MMM-YY");
		for(Goal goal:goals) {
			List<Double> amounts=new ArrayList<>();
			Double completed =(balance*(goal.getPercent()/100.0))>=goal.getTargetAmount()?goal.getTargetAmount():(balance*(goal.getPercent()/100.0));
			Double remaining= goal.getTargetAmount()-completed;
			balance=balance-completed;
			amounts.add(completed<0?0:completed);
			amounts.add(remaining<0?0:remaining);
			GoalVo goalVo= new GoalVo();
			goalVo.setId(goal.getId());
			goalVo.setName(goal.getName());
			goalVo.setAmounts(amounts);
			goalVo.setPercent((int) ((completed/goal.getTargetAmount())*100));
			goalsVos.add(goalVo);
		}
		return goalsVos;
	}
	
	public void deleteGoal(Long goalId) {
		goalsRepo.deleteById(goalId);
	}
}
