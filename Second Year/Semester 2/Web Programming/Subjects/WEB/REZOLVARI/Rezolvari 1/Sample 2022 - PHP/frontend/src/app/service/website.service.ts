import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {keyword} from "../model/keyword";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class WebsiteService {

  constructor(private httpClient: HttpClient) { }

  //   addRecipe(recipe: Recipe): Observable<any>{
  //     console.log(recipe);
  //     var res = this._http.post<any>(this._url, recipe);
  //     console.log(res);
  //     return res;
  //   }

  addKeyword(keyword: keyword): Observable<any>{
    return this.httpClient.post("http://localhost/exam2022sample/backend/addKeyword.php", keyword);
  }

  filterDocuments(filter: string): Observable<any>{
    return this.httpClient.get("http://localhost/exam2022sample/backend/getMatchingDocuments.php?filter=" + `${filter}`);
  }

  renderDocument(id: number): Observable<any>{
    return this.httpClient.get("http://localhost/exam2022sample/backend/renderDocument.php?id="+ `${id}`);
  }

  getKeywords(): Observable<any>{
    return this.httpClient.get("http://localhost/exam2022sample/backend/getKeywords.php");
  }
}
