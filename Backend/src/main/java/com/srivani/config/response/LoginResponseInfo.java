package com.srivani.config.response;

import java.util.List;
import java.util.Map;

import com.srivani.config.model.IncomeInfo;
import com.srivani.config.model.SavingsInfo;

import lombok.Data;

@Data
public class LoginResponseInfo {

	private String status;
	private String message;
	private String userName;
	private String totalIncome;
	private String totalExpenses;
	private List<IncomeInfo> list;
	private String userId;
	private List<SavingsInfo> savingsList;
	private String totalSaved;
	private String totalTarget;
	private String totalSavedPerc;
	private String netBal;
	private List<Map<String, Object>> chartList;
	private List<IncomeInfo> incByCategoryList;
}
