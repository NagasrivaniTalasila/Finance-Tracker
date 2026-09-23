package com.srivani.config.dao;

import java.util.List;

import com.srivani.config.model.IncomeInfo;
import com.srivani.config.model.SavingsInfo;

public interface LoginDao {

	public String createUser(String username, String password, String email);

	public String login(String email, String password, StringBuilder userName, StringBuilder userId);

	public String addIncome(String email, IncomeInfo info);

	public String getIncome(IncomeInfo info, List<IncomeInfo> list, StringBuilder totalIncome);

	public String addExpense(String email, IncomeInfo info);

	public String getExpenses(IncomeInfo info, List<IncomeInfo> list, StringBuilder totalExpenses);

	public String createSavingsGoal(String userId, String goalName, String targetAmt, String initialAmt,
			StringBuilder goalId);

	public String getSavingsGoal(SavingsInfo info, List<SavingsInfo> list);

	public String addSavingsContribution(String userId, String goalId, String contribAmt);

	public void getSavingsOverview(String userId, StringBuilder totalSaved, StringBuilder totalTarget,
			StringBuilder totalSavedPerc);

	public String getFinancialSummary(SavingsInfo info, StringBuilder totalSaved, StringBuilder totalExpenses,
			StringBuilder totalIncome, StringBuilder netBal);

	public String getLastSixMonthSummary(String userId, String email, SavingsInfo info, List<SavingsInfo> list,
			List<SavingsInfo> ExpList);
}
