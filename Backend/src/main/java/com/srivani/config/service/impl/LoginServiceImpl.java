package com.srivani.config.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.srivani.config.dao.LoginDao;
import com.srivani.config.model.IncomeInfo;
import com.srivani.config.model.LoginInfo;
import com.srivani.config.model.SavingsInfo;
import com.srivani.config.response.LoginResponseInfo;
import com.srivani.config.service.LoginService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

	private static final Logger logger = LogManager.getLogger(LoginServiceImpl.class);
	private final LoginDao dao;

	private LoginResponseInfo returnResponse(String status, String message) {
		LoginResponseInfo responseInfo = new LoginResponseInfo();
		responseInfo.setStatus(status);
		responseInfo.setMessage(message);
		return responseInfo;
	}

	@Override
	public LoginResponseInfo createUser(LoginInfo info) {
		String username = info.getUsername();
		String password = info.getPassword();
		String email = info.getEmail();
		String status = dao.createUser(username, password, email);
		if ("0".equals(status)) {
			return returnResponse(status, "User created successfully");
		} else if ("5".equals(status)) {
			return returnResponse(status, "User already existed.");
		} else if ("4".equals(status)) {
			return returnResponse(status, "Please fill all the details");
		} else {
			return returnResponse(status, "User not created. Please contact admin.");
		}
	}

	@Override
	public LoginResponseInfo login(LoginInfo info) {
		LoginResponseInfo responseInfo = new LoginResponseInfo();
		String email = info.getEmail();
		String password = info.getPassword();
		StringBuilder userName = new StringBuilder();
		StringBuilder userId = new StringBuilder();
		String status = dao.login(email, password, userName, userId);
		if ("4".equals(status)) {
			return returnResponse(status, "Please fill all the details");
		} else if ("5".equals(status)) {
			return returnResponse(status, "User not existed. Please signup.");
		} else if ("7".equals(status)) {
			return returnResponse(status, "Invalid email or password.");
		} else if ("6".equals(status)) {
			return returnResponse(status, "User expired. Please contact admin.");
		} else if ("0".equals(status)) {
			logger.info(userName.toString());
			responseInfo.setUserName(userName.toString());
			responseInfo.setUserId(userId.toString());
			responseInfo.setStatus(status);
			responseInfo.setMessage("Login success.");
			return responseInfo;
		} else {
			return returnResponse("300", "Login failed. Please contact admin.");
		}
	}

	@Override
	public LoginResponseInfo addIncome(IncomeInfo info) {
		String email = info.getEmail();
		String status = dao.addIncome(email, info);
		if ("0".equals(status)) {
			return returnResponse(status, "Income added successfully.");
		} else if ("4".equals(status)) {
			return returnResponse(status, "Invalid email.");
		} else if ("404".equals(status)) {
			return returnResponse(status, "No user found. Please signup.");
		} else if ("99".equals(status)) {
			return returnResponse(status, "Please add minimum amount.");
		} else if ("2".equals(status)) {
			return returnResponse(status, "Please fill all the details.");
		} else {
			return returnResponse(status, "Failed to add income.");
		}
	}

	@Override
	public LoginResponseInfo getIncome(IncomeInfo info) {
		LoginResponseInfo responseInfo = new LoginResponseInfo();
		List<IncomeInfo> list = new ArrayList<>();
		StringBuilder totalIncome = new StringBuilder();
		String status = dao.getIncome(info, list, totalIncome);
		responseInfo.setStatus(status);
		responseInfo.setList(list);
		responseInfo.setTotalIncome(totalIncome.toString());
		return responseInfo;
	}

	@Override
	public LoginResponseInfo addExpense(IncomeInfo info) {
		String email = info.getEmail();
		String status = dao.addExpense(email, info);
		if ("0".equals(status)) {
			return returnResponse(status, "Expense added successfully.");
		} else if ("4".equals(status)) {
			return returnResponse(status, "Invalid email.");
		} else if ("404".equals(status)) {
			return returnResponse(status, "No user found. Please signup.");
		} else if ("99".equals(status)) {
			return returnResponse(status, "Please add minimum amount.");
		} else if ("2".equals(status)) {
			return returnResponse(status, "Please fill all the details.");
		} else {
			return returnResponse(status, "Failed to add expense.");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public LoginResponseInfo getExpenses(IncomeInfo info) {
		LoginResponseInfo responseInfo = new LoginResponseInfo();
		List<IncomeInfo> list = new ArrayList<>();
		List<IncomeInfo> incByCategoryList = new ArrayList<>();
		StringBuilder totalExpenses = new StringBuilder();
		String status = dao.getExpenses(info, list, totalExpenses);
		responseInfo.setStatus(status);
		responseInfo.setList(list);

		Map<String, List<IncomeInfo>> categoryGroupedData = new LinkedHashMap<>();

		List<String> colors = Arrays.asList("#2fbf71", "#d9b26a", "#4bb3d4", "#8e7bd6", "#e07a5f");
		int colorIndex = 0;

		for (IncomeInfo inf : list) {
			categoryGroupedData.computeIfAbsent(inf.getCategory(), k -> new ArrayList()).add(inf);
		}

		for (Map.Entry<String, List<IncomeInfo>> itr : categoryGroupedData.entrySet()) {
			String category = itr.getKey();
			double total = 0;

			for (IncomeInfo inf : itr.getValue()) {
				total += Double.parseDouble(inf.getAmount());
			}
			IncomeInfo incomeInfo = new IncomeInfo();
			incomeInfo.setCategory(category);
			incomeInfo.setAmount(String.valueOf(total));
			incomeInfo.setColor(colors.get(colorIndex));
			incByCategoryList.add(incomeInfo);
			colorIndex++;

			if (colorIndex == colors.size()) {
				colorIndex = 0;
			}

		}
		responseInfo.setIncByCategoryList(incByCategoryList);
		responseInfo.setTotalExpenses(totalExpenses.toString());
		return responseInfo;
	}

	@Override
	public LoginResponseInfo createSavingsGoal(SavingsInfo info) {
		String userId = info.getUserId();
		String targetAmt = info.getTargetAmount();
		String initialAmt = info.getInitialAmount();
		String goalName = info.getGoalName();

		StringBuilder goalId = new StringBuilder();
		if (userId == null || userId == "") {
			return returnResponse("4", "User Id is invalid");
		} else if (goalName == null || goalName == "") {
			return returnResponse("4", "Goal name cannot be empty");
		} else if (targetAmt == null || targetAmt == "") {
			return returnResponse("4", "Target amount cannot be empty");
		} else if (initialAmt == null || initialAmt == "") {
			return returnResponse("4", "Initial amount cannot be empty");
		} else {
			String status = dao.createSavingsGoal(userId, goalName, targetAmt, initialAmt, goalId);
			if (status.equals("0")) {
				return returnResponse("0", "Goal created successfully");
			} else {
				return returnResponse(status, "Goal creation failed.");
			}
		}
	}

	@Override
	public LoginResponseInfo getSavingsGoal(SavingsInfo info) {
		LoginResponseInfo responseInfo = new LoginResponseInfo();
		String userId = info.getUserId();
		if (userId == null || userId == "") {
			return returnResponse("4", "User Id cannot be empty");
		} else {
			List<SavingsInfo> list = new ArrayList<>();
			String status = dao.getSavingsGoal(info, list);
			responseInfo.setStatus(status);
			responseInfo.setSavingsList(list);
			return responseInfo;
		}
	}

	@Override
	public LoginResponseInfo addSavingsContribution(SavingsInfo info) {
		String userId = info.getUserId();
		String goalId = info.getGoalId();
		String contribAmt = info.getContribAmount();
		if (userId == null || userId == "") {
			return returnResponse("4", "User Id cannot be empty");
		} else if (goalId == null || goalId == "") {
			return returnResponse("4", "Goal Id cannot be empty");
		} else {
			String status = dao.addSavingsContribution(userId, goalId, contribAmt);
			if (status.equals("0")) {
				return returnResponse("0", "Contribution added successfully");
			} else {
				return returnResponse(status, "Contribution adding failed.");
			}
		}
	}

	@Override
	public LoginResponseInfo getSavingsOverview(SavingsInfo info) {
		LoginResponseInfo responseInfo = new LoginResponseInfo();
		StringBuilder totalSaved = new StringBuilder();
		StringBuilder totalTarget = new StringBuilder();
		StringBuilder totalSavedPerc = new StringBuilder();

		String userId = info.getUserId();
		if (userId == null || userId == "") {
			return returnResponse("4", "User Id cannot be empty");
		} else {
			dao.getSavingsOverview(userId, totalSaved, totalTarget, totalSavedPerc);
			responseInfo.setTotalSaved(totalSaved.toString());
			responseInfo.setTotalTarget(totalTarget.toString());
			responseInfo.setTotalSavedPerc(totalSavedPerc.toString());
		}
		return responseInfo;
	}

	@Override
	public LoginResponseInfo getFinancialSummary(SavingsInfo info) {
		LoginResponseInfo responseInfo = new LoginResponseInfo();

		StringBuilder totalSaved = new StringBuilder();
		StringBuilder totalExpenses = new StringBuilder();
		StringBuilder totalIncome = new StringBuilder();
		StringBuilder netBal = new StringBuilder();

		String userId = info.getUserId();

		if (userId == null || userId == "") {
			return returnResponse("4", "User Id cannot be empty");
		} else {
			String status = dao.getFinancialSummary(info, totalSaved, totalExpenses, totalIncome, netBal);
			responseInfo.setStatus(status);
			if (status.equals("0")) {
				responseInfo.setTotalExpenses(totalExpenses.toString());
				responseInfo.setNetBal(netBal.toString());
				responseInfo.setTotalIncome(totalIncome.toString());
				responseInfo.setTotalSaved(totalSaved.toString());
				return responseInfo;
			} else {
				responseInfo.setMessage("Failed to load the data.");
			}
		}
		return responseInfo;
	}

	@Override
	public LoginResponseInfo getLastSixMonthSummary(SavingsInfo info) {
		LoginResponseInfo responseInfo = new LoginResponseInfo();
		String userId = info.getUserId();
		String email = info.getEmail();
		if (userId == null || userId == "" || email == null || email == "") {
			return returnResponse("4", "User Id cannot be empty");
		} else {
			List<SavingsInfo> incList = new ArrayList<>();
			List<SavingsInfo> ExpList = new ArrayList<>();
			String status = dao.getLastSixMonthSummary(userId, email, info, incList, ExpList);
			if (status.equals("0")) {
				responseInfo.setStatus(status);
				Map<String, Double> incomeMap = new LinkedHashMap<>();
				Map<String, Double> expenseMap = new LinkedHashMap<>();

				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

				DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH);

				for (int i = 5; i >= 0; i--) {
					LocalDate date = LocalDate.now().minusMonths(i);
					String month = date.format(monthFormat);
					incomeMap.put(month, 0.0);
					expenseMap.put(month, 0.0);
				}

				for (SavingsInfo income : incList) {

					LocalDate date = LocalDate.parse(income.getIncDate(), dateFormat);

					String month = date.format(monthFormat);

					double amount = Double.parseDouble(income.getAmount());

					incomeMap.put(month, incomeMap.getOrDefault(month, 0.0) + amount);
				}

				for (SavingsInfo expense : ExpList) {

					LocalDate date = LocalDate.parse(expense.getExpDate(), dateFormat);

					String month = date.format(monthFormat);

					double amount = Double.parseDouble(expense.getAmount());

					expenseMap.put(month, expenseMap.getOrDefault(month, 0.0) + amount);
				}

				List<Map<String, Object>> chartList = new ArrayList<>();

				for (String month : incomeMap.keySet()) {

					Map<String, Object> data = new LinkedHashMap<>();

					data.put("month", month);
					data.put("income", incomeMap.get(month));
					data.put("expense", expenseMap.getOrDefault(month, 0.0));

					chartList.add(data);
				}
				responseInfo.setChartList(chartList);
			} else {
				return returnResponse(status, "Failed to load last six months summary.");
			}
		}
		return responseInfo;
	}
}
