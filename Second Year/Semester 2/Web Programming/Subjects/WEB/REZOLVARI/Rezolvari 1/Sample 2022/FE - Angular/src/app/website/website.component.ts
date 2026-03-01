import { Component, OnInit } from '@angular/core';
import {WebsiteService} from "../service/website.service";
import {Project} from "../model/project";
import {Router} from "@angular/router";
import {User} from "../model/user";
import {devDTO} from "../model/adddto";

@Component({
  selector: 'app-website',
  templateUrl: './website.component.html',
  styleUrls: ['./website.component.css']
})
export class WebsiteComponent implements OnInit {


  username!: string;
  id!: string;
  //assetsSplit!: Array<string[]>;
  projects!: Project[];
  memberProjects!: Project[];
  devs!: User[];
  filterDev!: User;
  filteredDevs!: User[];

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

    this.getProjects();
    this.getDevs();
  }

  getProjects(){
    this.service.getProjects().subscribe(
      res => this.projects = res,
      error => console.log(error)
    )
  }

  getMemberProject(){
    this.service.getUserProjects(this.username).subscribe(
      res => this.memberProjects = res
    )
  }

  getDevs(){
    this.service.getSoftwareDevelopers().subscribe(
      res => this.devs = res
    )
  }

  filterDevSkills(skill: string){
    this.filteredDevs = this.devs.filter(s => s.skills.includes(skill));
  }

  addProjects(name: string,projects: string){
    this.service.addDev(<devDTO>{name: name, list: projects}).subscribe(
      res => console.log(res),
      error => console.log(error)
    )
  }
}
