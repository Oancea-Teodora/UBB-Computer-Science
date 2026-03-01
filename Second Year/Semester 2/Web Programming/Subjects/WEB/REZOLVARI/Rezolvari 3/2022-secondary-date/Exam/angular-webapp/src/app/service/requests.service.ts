import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {map, Observable} from "rxjs";
import {Project} from "../model/project";
import {Developer} from "../model/dev";

@Injectable({
  providedIn: 'root'
})
export class RequestsService {
  private baseUrl = 'http://localhost:8080/home?action=';

  constructor(private httpClient: HttpClient) { }

  saveUser(user: string): Observable<any> {
    return this.httpClient
      .get<any>(this.baseUrl + "user&user=" + user).pipe();
  }

  getProjects(): Observable<Project[]> {
    return this.httpClient
      .get<any>(this.baseUrl + "allProjects").pipe();
  }

  getMemberProjects(): Observable<Project[]> {
    return this.httpClient
      .get<any>(this.baseUrl + "memberProjects").pipe();
  }

  getDevs(): Observable<Developer[]> {
    return this.httpClient
      .get<any>(this.baseUrl + "allDevs").pipe();
  }

  assignDevToProjects(devName: string, projects: string): Observable<any> {
    return this.httpClient.post<any>(this.baseUrl + "assign", {devName: devName, projects: projects});
  }
}
