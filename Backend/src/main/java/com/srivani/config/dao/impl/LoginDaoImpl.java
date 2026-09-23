package com.srivani.config.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.srivani.config.dao.LoginDao;
import com.srivani.config.model.IncomeInfo;
import com.srivani.config.model.SavingsInfo;
import com.srivani.config.util.ConnectionUtil;
import com.srivani.config.util.Properties;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class LoginDaoImpl implements LoginDao {

	private final Properties prop;
	private static final Logger logger = LogManager.getLogger(LoginDaoImpl.class);

	private String callProc(String input) {
		return "{call " + prop.getProperty(input) + " }";
	}

	@Override
	public String createUser(String username, String password, String email) {
		String status = "";
		try (Connection con = ConnectionUtil.getConnection();
				CallableStatement cstmt = con.prepareCall(callProc("create_user"))) {
			cstmt.setString(1, username);
			cstmt.setString(2, password);
			cstmt.setString(3, email);
			cstmt.registerOutParameter(4, Types.VARCHAR);
			cstmt.execute();
			status = cstmt.getString(4);
			logger.info("status to create user = " + status);
		} catch (Exception e) {
			logger.error("Exception occurred in createUser()/LoginDaoImpl " + e);
		}
		return status;
	}

	@Override
	public String login(String email, String password, StringBuilder userName, StringBuilder userId) {
		String status = "";
		try (Connection con = ConnectionUtil.getConnection();
				CallableStatement cstmt = con.prepareCall(callProc("login"))) {
			cstmt.setString(1, email);
			cstmt.setString(2, password);
			cstmt.registerOutParameter(3, Types.VARCHAR);
			cstmt.registerOutParameter(4, Types.VARCHAR);
			cstmt.registerOutParameter(5, Types.VARCHAR);
			cstmt.execute();
			status = cstmt.getString(5);
			logger.info("status to login = " + status);
			if ("0".equals(status)) {
				userName.append(cstmt.getString(3));
				userId.append(cstmt.getString(4));
			}
		} catch (Exception e) {
			logger.error("Exception occurred in login()/LoginDaoImpl " + e);
		}
		return status;
	}

	@Override
	public String addIncome(String email, IncomeInfo info) {
		String status = "";
		logger.info(email + " " + info.getDescription() + " " + info.getDate() + " " + info.getCategory() + " "
				+ info.getAmount());
		try (Connection con = ConnectionUtil.getConnection();
				CallableStatement cstmt = con.prepareCall(callProc("addIncome"))) {
			cstmt.setString(1, email);
			cstmt.setString(2, info.getDescription());
			cstmt.setString(3, info.getCategory());
			cstmt.setString(4, info.getDate());
			cstmt.setString(5, info.getAmount());
			cstmt.registerOutParameter(6, Types.VARCHAR);
			cstmt.execute();
			status = cstmt.getString(6);
			logger.info("status to add income = " + status);
		} catch (Exception e) {
			logger.error("Exception occurred in addIncome()/LoginDaoImpl " + e);
		}
		return status;
	}

	@Override
	public String getIncome(IncomeInfo info, List<IncomeInfo> list, StringBuilder totalIncome) {
		String status = "";
		double tIncome = 0.0;
		logger.info(info.getEmail() + " " + info.getDate());
		try (Connection con = ConnectionUtil.getConnection();
				CallableStatement cstmt = con.prepareCall(callProc("getIncome"))) {
			cstmt.setString(1, info.getEmail());
			cstmt.setString(2, info.getIncomeDate());
			cstmt.registerOutParameter(3, Types.VARCHAR);
			cstmt.execute();
			status = cstmt.getString(3);
			logger.info("status to get income = " + status);
			if ("0".equals(status)) {
				IncomeInfo incomeInfo = new IncomeInfo();
				try (ResultSet rs = cstmt.executeQuery()) {
					while (rs.next()) {
						incomeInfo = new IncomeInfo();
						incomeInfo.setDescription(rs.getString(1));
						incomeInfo.setCategory(rs.getString(2));
						incomeInfo.setDate(rs.getString(3));
						incomeInfo.setAmount(rs.getString(4));
						tIncome = tIncome + Double.parseDouble(rs.getString(4));
						list.add(incomeInfo);
					}
					totalIncome.append(tIncome);
				}
			}
		} catch (Exception e) {
			logger.error("Exception occurred in getIncome()/LoginDaoImpl " + e);
		}
		return status;
	}

	@Override
	public String addExpense(String email, IncomeInfo info) {
		String status = "";
		logger.info(email + " " + info.getDescription() + " " + info.getDate() + " " + info.getCategory() + " "
				+ info.getAmount());
		try (Connection con = ConnectionUtil.getConnection();
				CallableStatement cstmt = con.prepareCall(callProc("addExpense"))) {
			cstmt.setString(1, email);
			cstmt.setString(2, info.getDescription());
			cstmt.setString(3, info.getCategory());
			cstmt.setString(4, info.getDate());
			cstmt.setString(5, info.getAmount());
			cstmt.registerOutParameter(6, Types.VARCHAR);
			cstmt.execute();
			status = cstmt.getString(6);
			logger.info("status to add expense = " + status);
		} catch (Exception e) {
			logger.error("Exception occurred in addExpense()/LoginDaoImpl " + e);
		}
		return status;
	}

	@Override
	public String getExpenses(IncomeInfo info, List<IncomeInfo> list, StringBuilder totalExpenses) {
		String status = "";
		double tExp = 0.0;
		logger.info(info.getEmail() + " " + info.getExpenseDate());
		try (Connection con = ConnectionUtil.getConnection();
				CallableStatement cstmt = con.prepareCall(callProc("getExpenses"))) {
			cstmt.setString(1, info.getEmail());
			cstmt.setString(2, info.getExpenseDate());
			cstmt.registerOutParameter(3, Types.VARCHAR);
			cstmt.execute();
			status = cstmt.getString(3);
			logger.info("status to get expenses = " + status);
			if ("0".equals(status)) {
				IncomeInfo incomeInfo = new IncomeInfo();
				try (ResultSet rs = cstmt.executeQuery()) {
					while (rs.next()) {
						incomeInfo = new IncomeInfo();
						incomeInfo.setDescription(rs.getString(1));
						incomeInfo.setCategory(rs.getString(2));
						incomeInfo.setDate(rs.getString(3));
						incomeInfo.setAmount(rs.getString(4));
						tExp = tExp + Double.parseDouble(rs.getString(4));
						list.add(incomeInfo);
					}
					totalExpenses.append(tExp);
				}
			}
		} catch (Exception e) {
			logger.error("Exception occurred in getExpenses()/LoginDaoImpl " + e);
		}
		return status;
	}

	@Override
	public String createSavingsGoal(String userId, String goalName, String targetAmt, String initialAmt,
			StringBuilder goalId) {
		String status = "";
		logger.info(userId + " " + targetAmt + " " + initialAmt + " " + goalName);
		try (Connection con = ConnectionUtil.getConnection();
				CallableStatement cstmt = con.prepareCall(callProc("create_savings_goal"))) {
			cstmt.setString(1, userId);
			cstmt.setString(2, goalName);
			cstmt.setString(3, targetAmt);
			cstmt.setString(4, initialAmt);
			cstmt.registerOutParameter(5, Types.VARCHAR);
			cstmt.registerOutParameter(6, Types.VARCHAR);
			cstmt.execute();
			status = cstmt.getString(5);
			logger.info("status to create savings goal = " + status);
			if (status.equals("0")) {
				goalId.append(cstmt.getString(6));
			}
		} catch (Exception e) {
			logger.error("Exception occurred in createSavingsGoal()/LoginDaoImpl " + e);
		}
		return status;
	}

	@Override
	public String getSavingsGoal(SavingsInfo info, List<SavingsInfo> list) {
		String status = "0";
		logger.info(info.getUserId());
		try (Connection con = ConnectionUtil.getConnection();
				CallableStatement cstmt = con.prepareCall(callProc("get_savings_goals"))) {
			cstmt.setString(1, info.getUserId());
			cstmt.execute();
			SavingsInfo savingsInfo = new SavingsInfo();
			List<String> colors = Arrays.asList("#2fbf71", "#d9b26a", "#4bb3d4", "#8e7bd6", "#e07a5f");
			int colorIndex = 0;
			try (ResultSet rs = cstmt.executeQuery()) {
				while (rs.next()) {
					savingsInfo = new SavingsInfo();
					savingsInfo.setGoalId(rs.getString(1));
					savingsInfo.setGoalName(rs.getString(2));
					savingsInfo.setTargetAmount(new BigDecimal(rs.getString(3)).stripTrailingZeros().toPlainString());
					savingsInfo.setSavedAmount(new BigDecimal(rs.getString(4)).stripTrailingZeros().toPlainString());
					savingsInfo
							.setRemainingAmount(new BigDecimal(rs.getString(5)).stripTrailingZeros().toPlainString());
					savingsInfo.setProgressPerc(rs.getString(6));
					savingsInfo.setColor(colors.get(colorIndex));
					list.add(savingsInfo);
					colorIndex++;

					if (colorIndex == colors.size()) {
						colorIndex = 0;
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception occurred in getSavingsGoal()/LoginDaoImpl " + e);
		}
		return status;
	}

	@Override
	public String addSavingsContribution(String userId, String goalId, String contribAmt) {
		String status = "";
		logger.info(userId + " " + goalId + " " + contribAmt);
		try (Connection con = ConnectionUtil.getConnection();
				CallableStatement cstmt = con.prepareCall(callProc("add_savings_amount"))) {
			cstmt.setString(1, userId);
			cstmt.setString(2, goalId);
			cstmt.setString(3, contribAmt);
			cstmt.registerOutParameter(4, Types.VARCHAR);
			cstmt.execute();
			status = cstmt.getString(4);
			logger.info("status to add savings contribution = " + status);
		} catch (Exception e) {
			logger.error("Exception occurred in addSavingsContribution()/LoginDaoImpl " + e);
		}
		return status;
	}

	@Override
	public void getSavingsOverview(String userId, StringBuilder totalSaved, StringBuilder totalTarget,
			StringBuilder totalSavedPerc) {
		logger.info(userId);
		try (Connection con = ConnectionUtil.getConnection();
				CallableStatement cstmt = con.prepareCall(callProc("get_savings_overview"))) {
			cstmt.setString(1, userId);
			cstmt.execute();
			try (ResultSet rs = cstmt.executeQuery()) {
				while (rs.next()) {
					totalTarget.append(rs.getString(1));
					totalSaved.append(rs.getString(2));
					totalSavedPerc.append(rs.getString(3));
				}
			}
		} catch (Exception e) {
			logger.error("Exception occurred in getSavingsOverview()/LoginDaoImpl " + e);
		}
	}

	@Override
	public String getFinancialSummary(SavingsInfo info, StringBuilder totalSaved, StringBuilder totalExpenses,
			StringBuilder totalIncome, StringBuilder netBal) {
		String status = "";
		logger.info(info.getUserId() + " " + info.getEmail() + " " + info.getDate());
		try (Connection con = ConnectionUtil.getConnection();
				CallableStatement cstmt = con.prepareCall(callProc("get_financial_summary"))) {
			cstmt.setString(1, info.getEmail());
			cstmt.setString(2, info.getUserId());
			cstmt.setString(3, info.getDate());
			cstmt.registerOutParameter(4, Types.VARCHAR);
			cstmt.registerOutParameter(5, Types.VARCHAR);
			cstmt.registerOutParameter(6, Types.VARCHAR);
			cstmt.registerOutParameter(7, Types.VARCHAR);
			cstmt.registerOutParameter(8, Types.VARCHAR);
			cstmt.execute();
			status = cstmt.getString(8);
			logger.info("status to get financial summary = " + status);
			if (status.equals("0")) {
				totalIncome.append(cstmt.getString(4));
				totalExpenses.append(cstmt.getString(5));
				netBal.append(cstmt.getString(6));
				totalSaved.append(cstmt.getString(7));
			}
		} catch (Exception e) {
			logger.error("Exception occurred in getFinancialSummary()/LoginDaoImpl " + e);
		}
		return status;
	}

	@Override
	public String getLastSixMonthSummary(String userId, String email, SavingsInfo info, List<SavingsInfo> incList,
			List<SavingsInfo> ExpList) {
		String status = "";
		logger.info(info.getUserId() + " " + info.getEmail() + " " + info.getDate());
		try (Connection con = ConnectionUtil.getConnection();
				CallableStatement cstmt = con.prepareCall(callProc("get_incExp_lastSix_months"))) {
			cstmt.setString(1, info.getEmail());
			cstmt.setString(2, info.getUserId());
			cstmt.setString(3, info.getDate());
			cstmt.registerOutParameter(4, Types.VARCHAR);
			cstmt.execute();
			status = cstmt.getString(4);
			logger.info("status to get last six months financial summary = " + status);
			if (status.equals("0")) {
				SavingsInfo savingsInfo = new SavingsInfo();
				try (ResultSet rs = cstmt.executeQuery()) {
					while (rs.next()) {
						savingsInfo = new SavingsInfo();
						savingsInfo.setIncomeDesc(rs.getString(1));
						savingsInfo.setCategory(rs.getString(2));
						savingsInfo.setIncDate(rs.getString(3));
						savingsInfo.setAmount(rs.getString(4));
						incList.add(savingsInfo);
					}
				}

				cstmt.getMoreResults();
				try (ResultSet rs1 = cstmt.getResultSet()) {
					while (rs1.next()) {
						savingsInfo = new SavingsInfo();
						savingsInfo.setExpenseDesc(rs1.getString(1));
						savingsInfo.setCategory(rs1.getString(2));
						savingsInfo.setExpDate(rs1.getString(3));
						savingsInfo.setAmount(rs1.getString(4));
						ExpList.add(savingsInfo);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception occurred in getLastSixMonthSummary()/LoginDaoImpl " + e);
		}
		return status;
	}

}
