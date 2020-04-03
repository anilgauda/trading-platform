package ie.ncirl.tradingplatform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ie.ncirl.tradingplatform.dto.GoalsDeleteRequest;
import ie.ncirl.tradingplatform.dto.GoalsRequest;
import ie.ncirl.tradingplatform.dto.ResponseDTO;
import ie.ncirl.tradingplatform.model.Account;
import ie.ncirl.tradingplatform.service.GoalsService;
import ie.ncirl.tradingplatform.service.UserService;
import ie.ncirl.tradingplatform.vo.GoalVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class GoalsController {
	
	@Autowired
	GoalsService goalsService;
	
	@Autowired
	UserService userService;
	
	@PostMapping("/app/goal")
	public ResponseDTO<String> createGoals(GoalsRequest goalsRequest) {
		log.info("Current Goals Request:{}",goalsRequest);
		Account account=userService.getActiveAccount();
		goalsService.createGoals(account, goalsRequest);
		return new ResponseDTO<String>().withData("OK");
	}
	
	@GetMapping("/app/goal")
	public List<GoalVo> getGoals(){
		Account account=userService.getActiveAccount();
		List<GoalVo> goalsVos=goalsService.getGoals(account);
		return goalsVos;
	}
	
	@PostMapping("/app/goal/")
	public ResponseDTO<String> delete(GoalsDeleteRequest goalsRequest) {
		log.info("Delete goal Request:{}",goalsRequest.getGoalId());
		goalsService.deleteGoal(goalsRequest.getGoalId());
		return new ResponseDTO<String>().withData("OK");
	}

}
