import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {document} from "../model/document";

@Injectable({
  providedIn: 'root'
})
export class WebService {

  constructor(private httpClient: HttpClient) { }

  getWebsites(): Observable<Array<string[]>>{
    return this.httpClient.get<Array<string[]>>("http://localhost:80/exam2020ex7/getWebsites.php", {
      withCredentials: true
    });
  }

  getDocuments(): Observable<Array<string[]>>{
    return this.httpClient.get<Array<string[]>>("http://localhost:80/exam2020ex7/getDocuments.php",
      {
        withCredentials: true
      });
  }

  getUsername(): Observable<any>{
    return this.httpClient.get("http://localhost:80/exam2020ex7/auth.php")
  }

  getDocument(id: string): Observable<any>{
    return this.httpClient.get<any>("http://localhost:80/exam2020ex7/getDocument.php?id=" + `${id}`,
      {
        withCredentials: true
      });
  }

  updateDocument(document: document): Observable<any>{
    return this.httpClient.post("http://localhost:80/exam2020ex7/updateDocument.php", document,{
        withCredentials: true
      }
     )
  }

  getMatches(array: string): Observable<Array<string[]>>{
    return this.httpClient.get<Array<string[]>>("http://localhost:80/exam2020ex7/keywordmatch.php?array=" + `${array}`,
      {
        withCredentials: true
      });
  }

  // http://localhost:63343/exam2020ex7/pagination.php?page=2&results=3

  getPage(page: number, results: number): Observable<Array<string[]>>{
    return this.httpClient.get<Array<string[]>>("http://localhost:80/exam2020ex7/pagination.php?page=" + `${page}` + "&results=" + `${results}`,
      {
        withCredentials: true
      });
  }
}
