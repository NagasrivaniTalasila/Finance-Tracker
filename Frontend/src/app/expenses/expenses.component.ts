import { Component, OnInit } from '@angular/core';
import { ExpenseInfo } from './ExpenseInfo';
import { ExpenseTrackerService } from '../Services/expense-tracker.service';
import moment from 'moment';

@Component({
  selector: 'app-expenses',
  templateUrl: './expenses.component.html',
  styleUrls: ['./expenses.component.css']
})
export class ExpensesComponent implements OnInit {

  totalExpenses: any;
  items: any = [];
  errorMessage: any = "";
  categories: any = ['Housing', 'Food', 'Transport', 'Utilities', 'ENtertainment', 'Health', 'Shopping', 'Other'];
  info: ExpenseInfo = new ExpenseInfo();
  loading: boolean = false;
  addSpinner: boolean = false;
  addResponseData: any;
  successMessage: any = "";
  getResponseData: any;
  selectedDate: string = '';

  constructor(private expenseService: ExpenseTrackerService) { }

  ngOnInit(): void {
    this.info.email = localStorage.getItem("email");
    const today = new Date();
    this.selectedDate = this.formatDate(today);
    this.getExpenses();
  }

  formatDate(date: Date): string {
    return moment(date, 'YYYY-MM-dd').format('YYYY-MM');
  }

  onDateChange(): void {
    this.getExpenses();
  }

  getExpenses() {
    this.errorMessage = "";
    this.successMessage = "";
    this.info.expenseDate = moment(this.selectedDate, 'YYYY-MM').format('MM-YYYY');
    // this.info.expenseDate = moment(this.date, 'YYYY-MM-dd').format('MM-YYYY');
    let resp = this.expenseService.getExpenses(this.info);
    resp.subscribe((data) => {
      this.getResponseData = data;
      if (this.getResponseData.status == "0") {
        this.items = this.getResponseData.list;
        this.totalExpenses = this.getResponseData.totalExpenses;
      } else {
        this.errorMessage = "Failed to fetch data";
      }
    });
  }

  add() {
    this.errorMessage = "";
    this.successMessage = "";
    let desc = this.info.description;
    let category = this.info.category;
    let amount = this.info.amount;
    let date = this.info.date;

    if (desc == null || desc == undefined || desc == "") {
      this.errorMessage = "Please add description.";
    } else if (category == null || category == undefined || category == "") {
      this.errorMessage = "Please select category.";
    } else if (amount == null || amount == undefined || amount == "") {
      this.errorMessage = "Please enter amount";
    } else if (isNaN(amount)) {
      this.errorMessage = "Invalid amount.";
    } else if (date == null || date == undefined || date == "") {
      this.errorMessage = "Please choose date of expense.";
    } else {
      this.addSpinner = true;
      this.info.date = moment(date, 'YYYY-MM-DD').format('DD-MM-YYYY');
      let resp = this.expenseService.addExpense(this.info);
      resp.subscribe((data) => {
        this.addResponseData = data;
        if (this.addResponseData.status == "0") {
          this.successMessage = this.addResponseData.message;
          this.getExpenses();
        } else {
          this.errorMessage = this.addResponseData.message;
        }
        this.addSpinner = false;
      });
    }
  }

  remove(i: any) { }

}
