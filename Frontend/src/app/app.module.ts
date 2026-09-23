import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { HeaderComponent } from './header/header.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { SignupComponent } from './signup/signup.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ShellComponent } from './shell/shell.component';
import { IncomeComponent } from './income/income.component';
import { ExpensesComponent } from './expenses/expenses.component';
import { SavingsComponent } from './savings/savings.component';
import { MoneyPipe } from './money.pipe';
import { DonutChartComponent } from './donut-chart/donut-chart.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HeaderComponent,
    SidebarComponent,
    SignupComponent,
    DashboardComponent,
    ShellComponent,
    IncomeComponent,
    ExpensesComponent,
    SavingsComponent,
    DonutChartComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CommonModule,
    FormsModule,
    RouterLink,
    RouterLinkActive, RouterOutlet,
    BrowserAnimationsModule,
    HttpClientModule,
    MoneyPipe
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
