import { Component, OnInit } from '@angular/core';
import {LoginService} from "../service/login.service";
import {Router} from "@angular/router";
import {request} from "../model/login";

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
    this.login.loginRequest(<request>{name: user}).subscribe(
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
