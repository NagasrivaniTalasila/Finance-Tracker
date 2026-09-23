import { Component, OnInit } from '@angular/core';
import { SignupInfo } from '../Models/SignupInfo';
import { ExpenseTrackerService } from '../Services/expense-tracker.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  loading: boolean = false;
  info: SignupInfo = new SignupInfo();
  errorMessage: any = "";
  responseData: any;

  ngOnInit(): void { }

  constructor(private expenseService: ExpenseTrackerService, private router: Router) { }

  submit() {
    this.errorMessage = "";
    let username = this.info.username;
    let email = this.info.email;
    let pwd = this.info.password;
    let confirmpwd = this.info.confirmpwd;
    if (username == null || username == undefined || username == "") {
      this.errorMessage = "Please enter username";
    } else if (email == null || email == undefined || email == "") {
      this.errorMessage = "Please enter email";
    } else if (pwd == null || pwd == undefined || pwd == "") {
      this.errorMessage = "Please enter password";
    } else if (confirmpwd == null || confirmpwd == undefined || confirmpwd == "") {
      this.errorMessage = "Please enter confirm password";
    } else if (pwd != confirmpwd) {
      this.errorMessage = "Password and confirm password should match";
    } else {
      this.loading = true;
      let resp = this.expenseService.createUser(this.info);
      resp.subscribe((data: any) => {
        this.responseData = data;
        this.loading = false;
        this.errorMessage = this.responseData.messsage;
      });
    }
  }
}
