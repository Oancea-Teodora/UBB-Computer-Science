import { Component, OnInit } from '@angular/core';
import {LoginService} from "./login.service";
import {User} from "../model/user";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username!: string;
  password!: string;
  loginResponse!: string;


  constructor(private login: LoginService, private router: Router) { }

  ngOnInit(): void {
  }

  clickLogin(user: string, password: string): void{
    console.log("user: " + user)
    this.login.loginRequest(<User>{username: user, password: password}).subscribe(
      data => {
        this.loginResponse = `Welcome ${data}`;
        sessionStorage.setItem("username", data);
        this.router.navigate(['/website']);
      },
        error => {console.log(error),this.loginResponse = 'Login unsuccessful'}
    )
  }

}
