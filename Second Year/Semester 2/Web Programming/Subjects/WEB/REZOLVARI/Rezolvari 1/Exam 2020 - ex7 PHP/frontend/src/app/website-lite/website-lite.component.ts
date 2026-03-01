import { Component, OnInit } from '@angular/core';
import {WebService} from "./web.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-website-lite',
  templateUrl: './website-lite.component.html',
  styleUrls: ['./website-lite.component.css']
})
export class WebsiteLiteComponent implements OnInit {

  websites: Array<string[]> = [];
  webPage!: Array<string[]>;
  username!: string;
  document!: string;
  documents!: Array<string[]>;
  matchingDocs!: Array<string[]>;
  page!: number;

  constructor(private webService: WebService, private router: Router) {
  }

  ngOnInit(): void {
    console.log("Local : " + sessionStorage.getItem("username"))
    if(sessionStorage.getItem("username") === null)
      this.router.navigate(['/error']);
    else
      { // @ts-ignore
        this.username = sessionStorage.getItem("username");
      }
    this.loadWebsites()

    this.loadDocuments()
    this.page = 1;
    this.loadPaginated()
    //this.loadUsername()
    //console.log("username: " + this.username)
  }

  pressNext(){
    if(this.webPage.length == 0){
      this.page = 1;
    this.loadPaginated();}
  else {
      this.page += 1;
      this.loadPaginated();
    }
  }

  pressPrevious(){
    this.page -=1;
    if(this.page < 1)
      this.page = 1;
    this.loadPaginated();
  }

  loadPaginated(): void{
    this.webService.getPage(this.page, 2).subscribe(
      res => {this.webPage = res; console.log(res);}
    )
  }

  loadWebsites(): void{
    this.webService.getWebsites().subscribe(
      data => this.websites = data,
    );
    console.log(">>: " + this.websites)

  }

  loadDocuments(): void{
    this.webService.getDocuments().subscribe(
      data => this.documents = data,
    );
    console.log(">>: " + this.documents)

  }

  updateDocument(){
    this.document = this.document + ' '
    var array = this.document.split(',');
    //console.log(array + array[0]);
    this.router.navigate(['/update', +array[0]])
  }

  findMatches(array: string){
    console.log(array);
    this.webService.getMatches(array).subscribe(
      res => {console.log(res), this.matchingDocs = res},
      error => console.log(error)
    )
  }
//     this.router.navigate(['/actors/detail', actor.id]);

}
