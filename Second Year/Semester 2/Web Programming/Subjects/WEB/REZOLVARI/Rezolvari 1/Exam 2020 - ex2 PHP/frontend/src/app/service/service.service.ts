import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ServiceService {

  constructor(private httpClient: HttpClient) { }

  getPeople(): Observable<any>{
    return this.httpClient.get<any>("http://localhost:63342/exam2020ex2/backend/persondropdown.php",
      {
        withCredentials: true
      });
  }

  // get user assets
  getChannels(id: string): Observable<any>{
    return this.httpClient.get<any>("http://localhost:63342/exam2020ex2/backend/getOwnedChannels.php?id=" + `${id}`,
      {
        withCredentials: true
      });
  }

  getSubscribedChannels(name: string): Observable<any>{
    return this.httpClient.get<any>("http://localhost:63342/exam2020ex2/backend/get" +
      "Channels.php?name=" + `${name}`,
      {
        withCredentials: true
      });
  }

  getAllChannels(): Observable<any>{
    return this.httpClient.get<any>("http://localhost:63342/exam2020ex2/backend/getAllChannels.php",
      {
        withCredentials: true
      });
  }

  subscribeUser(name: string, channelId: string){
    // exam2020ex2/backend/subscribeChannel.php?name=mick&cid=3
    return this.httpClient.get<any>("http://localhost:63342/exam2020ex2/backend/subscribeChannel.php?name=" + `${name}`
      + "&cid=" + `${channelId}`,
      {
        withCredentials: true
      });
  }

}
