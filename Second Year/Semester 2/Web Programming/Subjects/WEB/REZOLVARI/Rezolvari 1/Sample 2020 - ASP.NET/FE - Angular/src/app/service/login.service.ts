import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {User} from "../model/user";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private url = "https://localhost:7115/api/User/login"

  constructor(private httpClient: HttpClient) { }

  loginRequest(user: User): Observable<any> {
    //let options = new RequestOptions({ headers: headers, withCredentials: true });
    return this.httpClient.post<any>(this.url, user);
  }

  getUserId(username: string): Observable<any>{
    return this.httpClient.get("https://localhost:7115/api/User/getUserId?username="+`${username}`);
  }
}
