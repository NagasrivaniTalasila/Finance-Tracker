import { Component, OnInit } from '@angular/core';
import { IncomeInfo } from './IncomeInfo';
import { ExpenseTrackerService } from '../Services/expense-tracker.service';
import moment from 'moment';

@Component({
  selector: 'app-income',
  templateUrl: './income.component.html',
  styleUrls: ['./income.component.css']
})
export class IncomeComponent implements OnInit {

  totalIncome: any;
  items: any = [];
  errorMessage: any = "";
  categories: any = ['Salary', 'Freelance', 'Investments', 'Business', 'Gifts', 'Other'];
  info: IncomeInfo = new IncomeInfo();
  loading: boolean = false;
  addSpinner: boolean = false;
  addResponseData: any;
  successMessage: any = "";
  getResponseData: any;
  selectedDate: string = '';
  // date = new Date();

  constructor(private expenseService: ExpenseTrackerService) { }

  ngOnInit(): void {
    this.info.email = localStorage.getItem("email");
    const today = new Date();
    this.selectedDate = this.formatDate(today);
    this.getIncome();
  }

  formatDate(date: Date): string {
    return moment(date, 'YYYY-MM-dd').format('YYYY-MM');
  }

  onDateChange(): void {
    this.getIncome();
  }

  getIncome() {
    this.errorMessage = "";
    this.successMessage = "";
    // this.info.incomeDate = moment(this.date, 'YYYY-MM-dd').format('MM-YYYY');
    this.info.incomeDate = moment(this.selectedDate, 'YYYY-MM').format('MM-YYYY');
    let resp = this.expenseService.getIncome(this.info);
    resp.subscribe((data) => {
      this.getResponseData = data;
      if (this.getResponseData.status == "0") {
        this.items = this.getResponseData.list;
        this.totalIncome = this.getResponseData.totalIncome;
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
      this.errorMessage = "Please choose date of income.";
    } else {
      this.addSpinner = true;
      this.info.date = moment(date, 'YYYY-MM-DD').format('DD-MM-YYYY');
      let resp = this.expenseService.addIncome(this.info);
      resp.subscribe((data) => {
        this.addResponseData = data;
        if (this.addResponseData.status == "0") {
          this.successMessage = this.addResponseData.message;
          this.getIncome();
        } else {
          this.errorMessage = this.addResponseData.message;
        }
        this.addSpinner = false;
      });
    }
  }

  remove(i: any) { }

}
