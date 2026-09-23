import { Component, OnInit } from '@angular/core';
import { ExpenseTrackerService } from '../Services/expense-tracker.service';
import { LoginInfo } from '../Models/LoginInfo';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  email: any;
  remember: any;
  password: any;
  errorMessage: any = "";
  loading: boolean = false;
  info: LoginInfo = new LoginInfo();
  responseData: any;

  constructor(private expenseService: ExpenseTrackerService, private router: Router) { }

  ngOnInit(): void {

  }

  submit() {
    this.errorMessage = "";
    let email = this.info.email;
    let password = this.info.password;

    if (email == null || email == undefined || email == "") {
      this.errorMessage = "Please enter email";
    } else if (password == null || password == undefined || password == "") {
      this.errorMessage = "Please enter password";
    } else {
      this.loading = true;
      let resp = this.expenseService.login(this.info);
      resp.subscribe((data) => {
        this.responseData = data;
        if (this.responseData != null || this.responseData != undefined) {
          this.loading = false;
          if (this.responseData.status == "0") {
            localStorage.setItem("userName", this.responseData.userName);
            localStorage.setItem("email", email);
            localStorage.setItem("userId", this.responseData.userId);
            this.router.navigate(['/dashboard']);
          } else {
            this.errorMessage = this.responseData.message;
          }
        } else {
          this.loading = false;
          this.errorMessage = "Failed to login. Please contact admin.";
        }
      })
    }
  }

}
