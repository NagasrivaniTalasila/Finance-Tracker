import { Component, computed, inject, Input, OnInit, signal } from '@angular/core';
import { SavingsInfo } from './SavingsInfo';
import { ExpenseTrackerService } from '../Services/expense-tracker.service';
import { Router } from '@angular/router';

interface ChartData {
  name: string;
  value: number;
  color: any;
  goalName: any;
  savedAmount: any;
  offset: any;
  dash: any;
}

interface ChartSegment extends ChartData {
  percentage: number;
  dashArray: string;
  dashOffset: number;
}

@Component({
  selector: 'app-savings',
  templateUrl: './savings.component.html',
  styleUrls: ['./savings.component.css']
})
export class SavingsComponent implements OnInit {

  goals: any = [];
  totalSaved: any = "";
  errorMessage: any = "";
  totalTarget: any = "";
  overallPct: any = "";
  info: SavingsInfo = new SavingsInfo();
  contrib: Record<string, number | null> = {};
  createSpinner: boolean = false;
  createResponseData: any;
  successMessage: any = "";
  responseData: any;
  savingsResponseData: any;
  overviewResponseData: any;
  data: any = [];
  segments: ChartSegment[] = [];
  @Input() centerLabel = 'Saved';
  @Input() ariaLabel = 'Donut chart';
  readonly radius = 45;
  readonly circumference = 2 * Math.PI * this.radius;


  constructor(private service: ExpenseTrackerService, private router: Router) { }

  ngOnInit(): void {
    let userId = localStorage.getItem("userId");
    if (userId == null || userId == undefined || userId == "") {
      this.router.navigate(['/logout']);
    } else {
      this.info.userId = userId;
      this.getSavingsGoal();
    }
  }

  getSavingsGoal() {
    this.goals = [];
    this.segments = [];
    this.data = [];
    let resp = this.service.getSavingsGoal(this.info);
    resp.subscribe((data) => {
      this.responseData = data;
      this.goals = this.responseData.savingsList;
      this.data = this.responseData.savingsList;
      this.getSavingsOverview();
    }, error => {
      this.errorMessage = "Failed to fetch data."
    });
  }

  getSavingsOverview() {
    this.overallPct = "";
    this.totalSaved = "";
    this.totalTarget = "";
    let resp = this.service.getSavingsOverview(this.info);
    resp.subscribe((data) => {
      this.overviewResponseData = data;
      this.overallPct = this.overviewResponseData.totalSavedPerc;
      this.totalSaved = this.overviewResponseData.totalSaved;
      this.totalTarget = this.overviewResponseData.totalTarget;
      this.createChart();
    }, error => {
      this.errorMessage = "Failed to fetch data."
    });
  }

  pct(saved: number, target: number): number {
    return target > 0 ? Math.min(100, Math.round((saved / target) * 100)) : 0;
  }

  contribute(element: any) {
    const amt = Number(this.contrib[element.goalId]);
    if (!amt || amt <= 0) {
    } else {
      this.info.contribAmount = amt;
      this.info.goalId = element.goalId;
      let resp = this.service.addSavingsContribution(this.info);
      resp.subscribe((data) => {
        this.savingsResponseData = data;
        this.getSavingsGoal();
      });
    }
    this.contrib[element.goalId] = null;
  }

  reset() {
    this.info.targetAmount = "";
    this.info.goalName = "";
    this.info.initialAmount = "";
  }

  addGoal() {
    this.errorMessage = "";
    this.successMessage = "";
    let goalName = this.info.goalName;
    let targetAmt = this.info.targetAmount;
    let initialAmount = this.info.initialAmount;
    if (goalName == null || goalName == undefined || goalName == "") {
      this.errorMessage = "Please enter goal name";
    } else if (targetAmt == null || targetAmt == undefined || targetAmt == "") {
      this.errorMessage = "Please enter target amount";
    } else if (initialAmount == undefined || initialAmount == null || initialAmount == "") {
      this.errorMessage = "Please enter initial amount";
    } else if (isNaN(initialAmount)) {
      this.errorMessage = "Initial amount should be a number";
    } else if (isNaN(targetAmt)) {
      this.errorMessage = "Target amount should be a number";
    } else {
      this.createSpinner = true;
      let resp = this.service.createSavingsGoal(this.info);
      resp.subscribe((data) => {
        this.createResponseData = data;
        this.createSpinner = false;
        if (this.createResponseData.status == "0") {
          this.successMessage = this.createResponseData.message;
          this.getSavingsGoal();
        } else {
          this.errorMessage = this.createResponseData.message;
        }
        this.reset();
      }, error => {
        this.errorMessage = "Failed to create goal. Please check the log."
      });
    }
  }

  trackByLabel(index: number, seg: any): string {
    return seg.goalName;
  }

  createChart(): void {
    const total = this.totalSaved;
    if (total === 0) {
      this.segments = [];
      return;
    }
    let currentOffset = 0;

    this.segments = this.data.map((item: any) => {
      const percentage = (item.savedAmount / total) * 100;
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
