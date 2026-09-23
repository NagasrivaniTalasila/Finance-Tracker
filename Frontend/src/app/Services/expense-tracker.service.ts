import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ExpenseTrackerService {

  apiUrl = "http://localhost:8000";

  constructor(private http: HttpClient) { }

  createUser(request: any) {
    return this.http.post(this.apiUrl + "/createUser", request, { responseType: 'json' });
  }

  login(request: any) {
    return this.http.post(this.apiUrl + "/login", request, { responseType: 'json' });
  }

  addIncome(request: any) {
    return this.http.post(this.apiUrl + "/addIncome", request, { responseType: 'json' });
  }

  getIncome(request: any) {
    return this.http.post(this.apiUrl + "/getIncome", request, { responseType: 'json' });
  }

  addExpense(request: any) {
    return this.http.post(this.apiUrl + "/addExpense", request, { responseType: 'json' });
  }

  getExpenses(request: any) {
    return this.http.post(this.apiUrl + "/getExpenses", request, { responseType: 'json' });
  }

  createSavingsGoal(request: any) {
    return this.http.post(this.apiUrl + "/createSavingsGoal", request, { responseType: 'json' });
  }

  getSavingsGoal(request: any) {
    return this.http.post(this.apiUrl + "/getSavingsGoal", request, { responseType: 'json' });
  }

  addSavingsContribution(request: any) {
    return this.http.post(this.apiUrl + "/addSavingsContribution", request, { responseType: 'json' });
  }

  getSavingsOverview(request: any) {
    return this.http.post(this.apiUrl + "/getSavingsOverview", request, { responseType: 'json' });
  }

  getFinancialSummary(request: any) {
    return this.http.post(this.apiUrl + "/getFinancialSummary", request, { responseType: 'json' });
  }

  getLastSixMonthSummary(request: any) {
    return this.http.post(this.apiUrl + "/getLastSixMonthSummary", request, { responseType: 'json' });
  }
}
