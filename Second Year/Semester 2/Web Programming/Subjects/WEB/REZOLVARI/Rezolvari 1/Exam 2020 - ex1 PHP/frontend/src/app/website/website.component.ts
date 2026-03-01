import { Component, OnInit } from '@angular/core';
import {WebService} from "../service/web.service";
import {Router} from "@angular/router";
import {Journal} from "../model/journal";
import {Article} from "../model/article";

@Component({
  selector: 'app-website',
  templateUrl: './website.component.html',
  styleUrls: ['./website.component.css']
})
export class WebsiteComponent implements OnInit {

  username!: string;
  id!: string;

  journals: Journal[]=[];
  chosenJournal!: Journal;
  journalAdd!: Journal;

  userArticles: Article[]=[];

  lastArticle: Article = <Article>{id:0,user:"",journalid:0,summary:"",date:0};
  articles: Article[]=[];

  constructor(private service: WebService, private router: Router) { }

  ngOnInit(): void {
    if(sessionStorage.getItem("username") === null)
      this.router.navigate(['/login']);
    else
    { // @ts-ignore
      this.username = sessionStorage.getItem("username");
      // @ts-ignore
      this.id = sessionStorage.getItem("id");
    }

    this.loadJournals();
    this.notifyNewArticle();

  }

  loadJournals(){
    this.service.getJournals().subscribe(
      res => {this.parseJournals(res)},
      error => console.log(error)
    )
  }


  loadUserArticles(){
    this.service.getUserJournals(this.username, this.chosenJournal.id).subscribe(
      res => {this.parseUserArticles(res)},
      error => console.log(error)
    )
  }

  loadArticles(){
    this.service.getArticles().subscribe(
      res => {this.parseArticles(res)},
      error => console.log(error)
    )
  }

  parseArticles(journals: Array<string[]>){
    this.articles=[];
    journals.forEach((el) =>{
      let arr = (el + "").split(",");
      let a: Article = <Article>{id: +arr[0], user: arr[1], journalid: +arr[2], summary: arr[3], date: +arr[4]};
      this.articles.push(a)
    })
  }


  parseJournals(journals: Array<string[]>){
    journals.forEach((el) =>{
      let arr = (el + "").split(",");
      let j: Journal = <Journal>{id: +arr[0], name: arr[1]};
      this.journals.push(j);
    })
  }

  parseUserArticles(journals: Array<string[]>){
    journals.forEach((el) =>{
      let arr = (el + "").split(",");
      let a: Article = <Article>{id: +arr[0], user: arr[1], journalid: +arr[2], summary: arr[3], date: +arr[4]};
      this.userArticles.push(a)
    })
  }

  addArticle(s: string, j: string){
    //let a: Article = <Article>{id: 0, user: this.username, summary: s, date:0, journalid: this.journalAdd.id}
    //console.log(a);
    this.service.addArticle(s,j,this.username).subscribe(
      res => console.log(res),
      error => console.log(error)
    )
  }

  getLastArticle(){
    //console.log("Last article: " + this.articles[this.articles.length-1].summary);
    return this.articles[this.articles.length-1];
  }

  notifyNewArticle(){
    //this.lastArticle = this.getLastArticle();
    //console.log("het>");
    setInterval(
      () =>{
        this.loadArticles();
        let last = this.getLastArticle();
        console.log("last in memory: " + this.lastArticle);
        console.log(last)
        if(last != undefined && last.user!=this.username && this.lastArticle.id != last.id) {
          alert("new article added by " + last.user +" : summary: " + last.summary);
          //if (this.lastArticle != last)
            this.lastArticle = last;
        }
        if(this.lastArticle == undefined)
          this.lastArticle = last;
      }, 5000
    )
  }
}
