import { Component, OnInit } from '@angular/core';
import {WebService} from "../website-lite/web.service";
import {Router} from "@angular/router";


import {ActivatedRoute, Params} from "@angular/router";
import {Location} from '@angular/common';

import {switchMap} from "rxjs/operators";
import {document} from "../model/document";


@Component({
  selector: 'app-update-document',
  templateUrl: './update-document.component.html',
  styleUrls: ['./update-document.component.css']
})
export class UpdateDocumentComponent implements OnInit {

  stringDocument!: string;
  document!: document;
  username!: string;
  documentId!: string;

  constructor(private web: WebService, private router: Router, private route: ActivatedRoute, private location: Location) { }

  ngOnInit(): void {
    if(sessionStorage.getItem("username") === null)
      this.router.navigate(['/error']);
    else
    { // @ts-ignore
      this.username = sessionStorage.getItem("username");
    }

    // this.route.paramMap.subscribe(params => {
    //    let id = params.get('id');
    //    this.product = this.products.find(product => product.id === +id);
    // })

    this.route.params.subscribe(
      params => {
        this.documentId = params['id'];
        //console.log(params['id'])
      }
    )

    console.log("Doc id: " + this.documentId)
    this.getDocument();
  }

  getDocument(){
    this.web.getDocument(this.documentId).subscribe(
      res => {
        this.stringDocument = res + '';
        var array = this.stringDocument.split(',');
        this.document = <document>{id: +array[0], websiteId: +array[1], name: array[2],
        keyword1: array[3], keyword2: array[4], keyword3: array[5], keyword4: array[6], keyword5: array[7]}
      }
    )
  }

  updateDocument(){
    console.log(this.document)
    this.web.updateDocument(this.document).subscribe(
      res => {console.log(res),this.router.navigate(['website'])},
      error => console.log(error)
    )
  }

}
