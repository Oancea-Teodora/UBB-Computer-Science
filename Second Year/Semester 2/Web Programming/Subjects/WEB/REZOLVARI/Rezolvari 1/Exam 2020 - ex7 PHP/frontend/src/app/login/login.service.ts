import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../model/user";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private url = "http://localhost:80/exam2020ex7/login.php"

  constructor(private httpClient: HttpClient) { }

  loginRequest(user: User): Observable<any> {
    //let options = new RequestOptions({ headers: headers, withCredentials: true });
    return this.httpClient.post<any>(this.url, user, {
      withCredentials: true
    });
  }
}
