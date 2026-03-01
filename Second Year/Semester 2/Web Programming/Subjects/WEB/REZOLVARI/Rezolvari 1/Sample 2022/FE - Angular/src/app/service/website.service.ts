import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {devDTO} from "../model/adddto";

@Injectable({
  providedIn: 'root'
})
export class WebsiteService {

  // https://localhost:7093/api/Website/getAll

  constructor(private httpClient: HttpClient) { }

  getUserProjects(user: string): Observable<any> {
    //let options = new RequestOptions({ headers: headers, withCredentials: true });
    return this.httpClient.get("https://localhost:7093/api/Website/getProjects?name="+`${user}`);
  }

  getProjects(): Observable<any>{
    return this.httpClient.get("https://localhost:7093/api/Website/getAll");
  }

  getSoftwareDevelopers(): Observable<any>{
    return this.httpClient.get("https://localhost:7093/api/Website/getAllDevs");
  }

  addDev(dev: devDTO): Observable<any>{
    return this.httpClient.post("https://localhost:7093/api/Website/addDev", dev);
  }

}
