import { Component, OnInit } from '@angular/core';
import {AssetsService} from "../services/assets.service";
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
  assets: Array<string[]> = [];
  finalAssets: Asset[] = [];
  assetsToAdd: Asset[] = [];
  //assetsSplit!: Array<string[]>;

  constructor(private service: AssetsService, private router: Router) { }

  ngOnInit(): void {
    if(sessionStorage.getItem("username") === null)
      this.router.navigate(['/login']);
    else
    { // @ts-ignore
      this.username = sessionStorage.getItem("username");
      // @ts-ignore
      this.id = sessionStorage.getItem("id");
    }

    this.loadAssets();
    console.log(this.assets);
    console.log("final: " + this.finalAssets)
    //let assetsSplit = (this.assets + ' ').split(",");
    //console.log("split: " + assetsSplit);
  }

  loadAssets(){
    this.service.getAssets(this.id).subscribe(
      res => {this.assets = res; console.log("res: " + res); this.parseAssets(res)},
      err => console.log(err)
    )
  }

  addAssets(){
    this.service.addAssets(this.assetsToAdd).subscribe(
      res => {console.log(res), this.assetsToAdd = [], this.refreshPage()},
      error => console.log(error)
    )
  }

  refreshPage(): void{
    setTimeout(function() {
      window.location.reload();
    }, 1000);
  }

  addAsset(name: string, descr: string, value: string){
    console.log("name: " + name + descr + value);
    let ass: Asset = <Asset>{id: 0, userid: +this.id, name: name, description: descr, value: +value};
    this.assetsToAdd.push(ass)
    console.log("Assets to add: " + this.assetsToAdd);
  }

  parseAssets(assets: Array<string[]>){
    console.log(">>" + assets);
    assets.forEach((element) => {
      console.log("asset: " + element);
       let arr = (element + "").split(",");
       let ass: Asset = <Asset>{id: +arr[0], userid: +arr[1], name: arr[2], description: arr[3], value: +arr[4]};
       console.log(ass)
       this.finalAssets.push(ass);

    })
    // for(let asset of this.assets){
    //   console.log("asset: " + asset)
    // }
  }
}
