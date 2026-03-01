import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private url = "http://localhost:80/exam2020ex2/backend/login.php"

  constructor(private httpClient: HttpClient) { }

  loginRequest(user: string): Observable<any> {
    const params = new FormData();
    params.append('username', user);
    //let options = new RequestOptions({ headers: headers, withCredentials: true });
    return this.httpClient.post<any>(this.url, params, {
      withCredentials: true
    });
  }
}
