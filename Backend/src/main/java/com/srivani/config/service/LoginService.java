package com.srivani.config.service;

import com.srivani.config.model.IncomeInfo;
import com.srivani.config.model.LoginInfo;
import com.srivani.config.model.SavingsInfo;
import com.srivani.config.response.LoginResponseInfo;

public interface LoginService {

	public LoginResponseInfo createUser(LoginInfo info);

	public LoginResponseInfo login(LoginInfo info);

	public LoginResponseInfo addIncome(IncomeInfo info);

	public LoginResponseInfo getIncome(IncomeInfo info);

	public LoginResponseInfo addExpense(IncomeInfo info);

	public LoginResponseInfo getExpenses(IncomeInfo info);

	public LoginResponseInfo createSavingsGoal(SavingsInfo info);

	public LoginResponseInfo getSavingsGoal(SavingsInfo info);

	public LoginResponseInfo addSavingsContribution(SavingsInfo info);

	public LoginResponseInfo getSavingsOverview(SavingsInfo info);

	public LoginResponseInfo getFinancialSummary(SavingsInfo info);

	public LoginResponseInfo getLastSixMonthSummary(SavingsInfo info);
}
