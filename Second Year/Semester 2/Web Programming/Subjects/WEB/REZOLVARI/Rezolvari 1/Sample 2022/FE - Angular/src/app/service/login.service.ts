import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {request} from "../model/login";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private httpClient: HttpClient) { }

  loginRequest(name: request): Observable<any> {
    console.log(name);
    //let options = new RequestOptions({ headers: headers, withCredentials: true });
    return this.httpClient.post<any>("https://localhost:7093/api/User/login", name);
  }

  getUserId(username: string): Observable<any>{
    return this.httpClient.get("https://localhost:7093/api/User/getUserId?username="+`${username}`);
  }
}
