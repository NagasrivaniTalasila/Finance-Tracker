package com.srivani.config.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.srivani.config.model.IncomeInfo;
import com.srivani.config.model.LoginInfo;
import com.srivani.config.model.SavingsInfo;
import com.srivani.config.response.LoginResponseInfo;
import com.srivani.config.service.LoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4900")
public class LoginController {

	private final LoginService service;

	@PostMapping(value = "/createUser")
	public LoginResponseInfo createUser(@RequestBody LoginInfo info) {
		return service.createUser(info);
	}

	@PostMapping(value = "/login")
	public LoginResponseInfo login(@RequestBody LoginInfo info) {
		return service.login(info);
	}

	@PostMapping(value = "/addIncome")
	public LoginResponseInfo addIncome(@RequestBody IncomeInfo info) {
		return service.addIncome(info);
	}

	@PostMapping(value = "/getIncome")
	public LoginResponseInfo getIncome(@RequestBody IncomeInfo info) {
		return service.getIncome(info);
	}

	@PostMapping(value = "/addExpense")
	public LoginResponseInfo addExpense(@RequestBody IncomeInfo info) {
		return service.addExpense(info);
	}

	@PostMapping(value = "/getExpenses")
	public LoginResponseInfo getExpenses(@RequestBody IncomeInfo info) {
		return service.getExpenses(info);
	}

	@PostMapping(value = "/createSavingsGoal")
	public LoginResponseInfo createSavingsGoal(@RequestBody SavingsInfo info) {
		return service.createSavingsGoal(info);
	}

	@PostMapping(value = "/getSavingsGoal")
	public LoginResponseInfo getSavingsGoal(@RequestBody SavingsInfo info) {
		return service.getSavingsGoal(info);
	}

	@PostMapping(value = "/addSavingsContribution")
	public LoginResponseInfo addSavingsContribution(@RequestBody SavingsInfo info) {
		return service.addSavingsContribution(info);
	}

	@PostMapping(value = "/getSavingsOverview")
	public LoginResponseInfo getSavingsOverview(@RequestBody SavingsInfo info) {
		return service.getSavingsOverview(info);
	}

	@PostMapping(value = "/getFinancialSummary")
	public LoginResponseInfo getFinancialSummary(@RequestBody SavingsInfo info) {
		return service.getFinancialSummary(info);
	}

	@PostMapping(value = "/getLastSixMonthSummary")
	public LoginResponseInfo getLastSixMonthSummary(@RequestBody SavingsInfo info) {
		return service.getLastSixMonthSummary(info);
	}

}
