import { Component, OnInit } from '@angular/core';
import {LoginService} from "../service/login.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {


  username!: string;
  loginResponse!: string;


  constructor(private login: LoginService, private router: Router) { }

  ngOnInit(): void {
  }

  clickLogin(user: string): void{
    console.log("user: " + user)
    this.login.loginRequest(user).subscribe(
      data => {
        this.loginResponse = `Welcome ${data}`;
        data = data + ' ';
        let arr = data.split(';');
        sessionStorage.setItem("username", arr[0]);
        sessionStorage.setItem("id", arr[1]);
        this.router.navigate(['/website']);
      },
      error => {console.log(error),this.loginResponse = 'Login unsuccessful'}
    )
  }

}
