import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {User} from "../model/user";
import {Observable} from "rxjs";
import {Asset} from "../model/asset";

@Injectable({
  providedIn: 'root'
})
export class WebsiteService {

  // https://localhost:7115/api/Website

  constructor(private httpClient: HttpClient) { }

  getAssets(userid: string): Observable<any> {
    //let options = new RequestOptions({ headers: headers, withCredentials: true });
    return this.httpClient.get("https://localhost:7115/api/Website?userid="+`${userid}`);
  }

  // https://localhost:7115/api/Website/addAssets
  addAssets(assets: Asset[]): Observable<any>{
    return this.httpClient.post("https://localhost:7115/api/Website/addAssets", assets);
  }

  //getPage
  getPage(pageNo: number, pageSize: number, userid: number): Observable<any>{
    return this.httpClient.get("https://localhost:7115/api/Website/getPage?pageNo=" + `${pageNo}` +"&pageSize=" +
      `${pageSize}`+"&userId=" + `${userid}`)
}
}
