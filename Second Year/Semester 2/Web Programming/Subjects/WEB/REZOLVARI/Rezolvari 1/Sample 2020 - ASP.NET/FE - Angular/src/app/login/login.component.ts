import { Component, OnInit } from '@angular/core';
import {User} from "../model/user";
import {Router} from "@angular/router";
import {LoginService} from "../service/login.service";

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
        if(data == true) {
          this.login.getUserId(user).subscribe(
            res =>{
               sessionStorage.setItem("username", user);
               console.log(res);
               sessionStorage.setItem("id", res);
               this.router.navigate(['/website']);
            }
          )
        }
        else
          this.loginResponse = 'Login unsuccessful'
        // this.loginResponse = `Welcome ${data}`;
        // data = data + ' ';
        // let arr = data.split(';');
        // sessionStorage.setItem("username", arr[0]);
        // sessionStorage.setItem("id", arr[1]);
        // this.router.navigate(['/website']);
      },
      error => {console.log(error),this.loginResponse = 'Login unsuccessful'}
    )
  }

}
