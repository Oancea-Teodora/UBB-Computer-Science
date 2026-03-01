import { Component, OnInit } from '@angular/core';
import {WebsiteService} from "../service/website.service";
import {Router} from "@angular/router";
import {Asset} from "../model/asset";

@Component({
  selector: 'app-website',
  templateUrl: './website.component.html',
  styleUrls: ['./website.component.css']
})
export class WebsiteComponent implements OnInit {

  username!: string;
  id!: string;
  //assetsSplit!: Array<string[]>;
  assets!: Asset[];
  assetsToAdd: Asset[]=[];
  page!: number;
  assetsPage!: Asset[];

  constructor(private service: WebsiteService, private router: Router) { }

  ngOnInit(): void {
    if(sessionStorage.getItem("username") === null)
      this.router.navigate(['/login']);
    else
    { // @ts-ignore
      this.username = sessionStorage.getItem("username");
      // @ts-ignore
      this.id = sessionStorage.getItem("id");
      console.log(this.username + " " + this.id);
    }
    console.log(this.username + " " + this.id);
    this.getAssets();
    console.log("assets: " + this.assets)
    this.page = 1;
    this.loadPaginated();
  }

  pressNext(){
    if(this.assetsPage.length == 0){
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
    this.service.getPage(this.page, 2, +this.id).subscribe(
      res => {this.assetsPage = res; console.log(res);}
    )
  }

  getAssets(){
    this.service.getAssets(this.id).subscribe(
      res => {console.log(res);
                   this.assets = res;},
      error => console.log(error)
    )
  }

  addAsset(name: string, description: string, value: string){
    console.log("name: " + name + description + value);
    let ass: Asset = <Asset>{id: 0, userid: +this.id, name: name, description: description, value: +value};
    this.assetsToAdd.push(ass)
  }

  addAssets(){
    this.service.addAssets(this.assetsToAdd).subscribe(
      res => {console.log(res), this.assetsToAdd=[], this.refreshPage()},
      error => console.log(error)
    )
  }

  refreshPage(): void{
    setTimeout(function() {
      window.location.reload();
    }, 1000);
  }

}
