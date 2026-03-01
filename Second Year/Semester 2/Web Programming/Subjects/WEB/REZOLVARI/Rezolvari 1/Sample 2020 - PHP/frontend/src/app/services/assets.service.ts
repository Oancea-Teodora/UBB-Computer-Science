import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import { Asset } from '../model/asset';

@Injectable({
  providedIn: 'root'
})
export class AssetsService {

  constructor(private httpClient: HttpClient) { }

  // get user assets
  getAssets(id: string): Observable<any>{
    return this.httpClient.get<any>("http://localhost:63342/exam2020sample/backend/getUserAssets.php?id=" + `${id}`,
      {
        withCredentials: true
      });
  }

  addAssets(assets: Asset[]): Observable<any>{
    return this.httpClient.post("http://localhost:80/exam2020sample/backend/addAssets.php", assets,
      {withCredentials: true});
  }

  //   updateDocument(document: document): Observable<any>{
  //     return this.httpClient.post("http://localhost:80/exam2020ex7/updateDocument.php", document,{
  //         withCredentials: true
  //       }
  //      )
  //   }
}
