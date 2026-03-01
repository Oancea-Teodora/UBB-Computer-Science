import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {ServiceService} from "../service/service.service";
import {Person} from "../model/person";
import {Channel} from "../model/channel";

@Component({
  selector: 'app-website',
  templateUrl: './website.component.html',
  styleUrls: ['./website.component.css']
})
export class WebsiteComponent implements OnInit {

  username!: string;
  id!: string;
  people: Person[]=[];
  channels!: Array<string[]>;
  person!: Person;
  Channels: Channel[] = [];
  channelDr: Channel[] = [];
  chosenChannel!: Channel;

  constructor(private service: ServiceService, private router: Router) { }

  ngOnInit(): void {
    if(sessionStorage.getItem("username") === null)
      this.router.navigate(['/login']);
    else
    { // @ts-ignore
      this.username = sessionStorage.getItem("username");
      // @ts-ignore
      this.id = sessionStorage.getItem("id");
    }
  this.loadPeople();
    console.log("People: " + this.people)
  this.getAllChannels();
  }

  loadPeople(){
    this.service.getPeople().subscribe(
      res => {console.log(res), this.getPeople(res)}
    )
  }

  getPeople(people: Array<string[]>){
    people.forEach((el) =>{
      let arr = (el + "").split(",");
      let p: Person = <Person>{id: +arr[0], name: arr[1], age: +arr[2], gender: arr[3]};
      this.people.push(p);
    })
  }

  parseChannels(channels: Array<string[]>){
    this.Channels = [];
    channels.forEach((el) =>{
      let arr = (el + "").split(",");
      let c: Channel = <Channel>{id: +arr[0], ownerId: +arr[1], name: arr[2], description: arr[3], subscribers: arr[4]};
      this.Channels.push(c);
    })
  }

  parseAllChannels(channels: Array<string[]>){
    channels.forEach((el) =>{
      let arr = (el + "").split(",");
      let c: Channel = <Channel>{id: +arr[0], ownerId: +arr[1], name: arr[2], description: arr[3], subscribers: arr[4]};
      this.channelDr.push(c);
    })
  }

  getChannel(){
    this.service.getChannels(this.person.id.toString()).subscribe(
      res => {
        console.log(res),
          this.channels = res;
          this.parseChannels(res);
      }
    )
  }

  getSubscribed(){
    this.service.getSubscribedChannels(this.username).subscribe(
      res => this.channels = res,
      error => console.log(error)
    )
  }

  getAllChannels(){
    this.service.getAllChannels().subscribe(
      res => {
        console.log(res),
        this.parseAllChannels(res)
      }
    )
  }

  subscribe(){
    console.log(this.chosenChannel)
    this.service.subscribeUser(this.username, this.chosenChannel.id.toString()).subscribe(
      res => console.log("ok"),
      error => console.log("error")
    )
  }

}
