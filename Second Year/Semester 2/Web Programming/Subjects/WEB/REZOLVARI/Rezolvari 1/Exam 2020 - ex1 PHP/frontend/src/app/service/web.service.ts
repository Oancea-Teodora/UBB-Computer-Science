import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Article} from "../model/article";

@Injectable({
  providedIn: 'root'
})
export class WebService {

  constructor(private httpClient: HttpClient) { }

  getJournals(): Observable<any>{
    return this.httpClient.get<any>("http://localhost:63342/exam2020ex1/backend/getJournals.php",
      {
        withCredentials: true
      });
  }


  getArticles(): Observable<any>{
    return this.httpClient.get<any>("http://localhost:63342/exam2020ex1/backend/getArticles.php",
      {
        withCredentials: true
      });
  }

  // http://localhost:63342/exam2020ex1/backend/getArticlesFromJournal.php?user=mick&jid=1
  getUserJournals(user: string, journalid: number){
    return this.httpClient.get<any>("http://localhost:63342/exam2020ex1/backend/getArticlesFromJournal.php?user="
      + `${user}` + "&jid=" + `${journalid}`,
      {
        withCredentials: true
      });
  }

  //   updateDocument(document: document): Observable<any>{
  //     return this.httpClient.post("http://localhost:80/exam2020ex7/updateDocument.php", document,{
  //         withCredentials: true
  //       }
  //      )
  //   }

  addArticle(s: string, j: string, u: string): Observable<any>{
    const params = new FormData();
    params.append('username', u);
    params.append("summary", s);
    params.append("journal", j);
    return this.httpClient.post("http://localhost:80/exam2020ex1/backend/addArticle.php", params, {
      withCredentials: true
    })
  }


}
