import { Component, OnInit } from '@angular/core';
import {WebsiteService} from "../service/website.service";
import {keyword} from "../model/keyword";
import {Document} from "../model/document";

@Component({
  selector: 'app-website',
  templateUrl: './website.component.html',
  styleUrls: ['./website.component.css']
})
export class WebsiteComponent implements OnInit {

  filteredDocuments!: Document[];
  selectedDocument!: Document;
  documentText!: string;
  keywords: keyword[]=[];

  constructor(private service: WebsiteService) { }

  ngOnInit(): void {
    this.getKeywords();
  }

  addKeyword(key: string, value: string){
    this.service.addKeyword(<keyword>{key: key, value: value}).subscribe(
      res => console.log("res"),
      error => console.log("error")
    )
  }

  onSelect(d: Document): void{
    this.selectedDocument = d;
    console.log("selected: " + this.selectedDocument.listOfTemplates);
  }

  filterDocuments(filter: string){
    this.service.filterDocuments(filter).subscribe(
      res => this.parseDocuments(res),
      error => console.log(error)
    )
  }


  parseDocuments(docs: Array<string[]>){
    this.filteredDocuments=[];

    docs.forEach((el) => {
      let arr = (el + "").split(",");
      let d: Document = <Document>{id: +arr[0], title: arr[1], listOfTemplates: arr[2]};
      this.filteredDocuments.push(d);
    })
  }

  getKeywords(){
    this.service.getKeywords().subscribe(
      res => {this.parseKeywords(res), console.log(res)},
      error => console.log(error)
    )
  }

  parseKeywords(keys: Array<string[]>){
    this.keywords = [];

    keys.forEach((el) => {
      let arr = (el + "").split(",");
      let k: keyword = <keyword>{id: +arr[0], key: arr[1], value: arr[2]};
      console.log("key = " + k);
      this.keywords.push(k);
    })

    console.log("k: " + this.keywords);
  }

  renderDocument(){
    this.service.renderDocument(this.selectedDocument.id).subscribe(
      res => {this.documentText = res, this.replaceKeywords()},
      error => console.log(error)
    )
  }

  replaceKeywords(){
   // this.getKeywords();
    console.log(this.keywords);
    this.keywords.forEach((el) =>{
      console.log(el.key + " " + el.value);
      this.documentText += "";
      this.documentText = this.documentText.replace(`{{${el.key}}}`, el.value)
      //console.log(this.documentText + " >> " + ans);
    });
  }

}
