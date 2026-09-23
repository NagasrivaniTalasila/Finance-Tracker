import { Component, Input, OnInit } from '@angular/core';
import moment from 'moment';
import { ExpenseTrackerService } from '../Services/expense-tracker.service';
import { DashboardInfo } from './DashboardInfo';

interface ChartData {
  name: string;
  value: number;
  color: any;
  category: any;
  amount: any;
  offset: any;
  dash: any;
}

interface ChartSegment extends ChartData {
  percentage: number;
  dashArray: string;
  dashOffset: number;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  selectedDate: string = '';
  info: DashboardInfo = new DashboardInfo();
  responseData: any;
  totalIncome: any = "";
  totalExpenses: any = "";
  netBal: any = "";
  totalSaved: any = "";
  summaryResponseData: any;
  chartList: any = [];
  expResponseData: any;
  data: any = [];
  segments: ChartSegment[] = [];
  @Input() centerLabel = 'SPENT';
  @Input() ariaLabel = 'Donut chart';
  readonly radius = 45;
  readonly circumference = 2 * Math.PI * this.radius;

  constructor(private service: ExpenseTrackerService) { }

  ngOnInit(): void {
    const today = new Date();
    this.selectedDate = this.formatDate(today);
    this.info.userId = localStorage.getItem("userId");
    this.info.email = localStorage.getItem("email");
    this.getFinancialSummary();
    this.getLastSixMonthSummary();
    this.getExpenses();
  }

  formatDate(date: Date): string {
    return moment(date, 'YYYY-MM-dd').format('YYYY-MM');
  }

  onDateChange() {
    this.getFinancialSummary();
    this.getLastSixMonthSummary();
    this.getExpenses();
  }

  getFinancialSummary() {
    this.info.date = moment(this.selectedDate, 'YYYY-MM').format('MM-YYYY');
    let resp = this.service.getFinancialSummary(this.info);
    resp.subscribe((data) => {
      this.responseData = data;
      this.totalSaved = this.responseData.totalSaved;
      this.netBal = this.responseData.netBal;
      this.totalIncome = this.responseData.totalIncome;
      this.totalExpenses = this.responseData.totalExpenses;
    });
  }

  getLastSixMonthSummary() {
    this.info.date = moment(this.selectedDate, 'YYYY-MM').format('MM-YYYY');
    let resp = this.service.getLastSixMonthSummary(this.info);
    resp.subscribe((data) => {
      this.summaryResponseData = data;
      this.chartList = this.summaryResponseData.chartList;
    });
  }

  pct(value: number): number {
    if (!this.chartList || this.chartList.length === 0) {
      return 0;
    }
    const maxValue = Math.max(
      ...this.chartList.flatMap((item: any) => [
        Number(item.income) || 0,
        Number(item.expense) || 0
      ])
    );
    if (maxValue === 0) {
      return 0;
    }
    return Math.min((Number(value) / maxValue) * 100, 100);
  }

  getExpenses() {
    this.data = [];
    this.info.expenseDate = moment(this.selectedDate, 'YYYY-MM').format('MM-YYYY');
    console.log(this.info);
    let resp = this.service.getExpenses(this.info);
    resp.subscribe((data) => {
      this.expResponseData = data;
      console.log(this.expResponseData);
      if (this.expResponseData.status == "0") {
        this.data = this.expResponseData.incByCategoryList;
        this.createChart();
      }
    });
  }

  trackByLabel(index: number, seg: any): string {
    return seg.category;
  }

  createChart(): void {
    const total = this.totalExpenses;
    if (total === 0) {
      this.segments = [];
      return;
    }
    let currentOffset = 0;

    this.segments = this.data.map((item: any) => {
      const percentage = (item.amount / total) * 100;
      const segmentLength = (percentage / 100) * this.circumference;
      const segment: ChartSegment = {
        ...item,
        percentage,
        dashArray: `${segmentLength} ${this.circumference}`,
        dashOffset: -currentOffset
      };
      currentOffset += segmentLength;
      return segment;
    });
  }

}
