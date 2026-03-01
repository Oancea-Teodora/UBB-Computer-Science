import { Component, OnInit } from '@angular/core';
import {Project} from "../model/project";
import {RequestsService} from "../service/requests.service";
import {Developer} from "../model/dev";

@Component({
  selector: 'app-user-menu',
  templateUrl: './user-menu.component.html',
  styleUrls: ['./user-menu.component.css']
})
export class UserMenuComponent implements OnInit {
  errorMessage: string;
  projects: Array<Project>;
  devs: Array<Developer>;

  constructor(private requestsService: RequestsService) { }

  ngOnInit(): void {
  }

  getProjects(): void {
    this.requestsService.getProjects()
      .subscribe(
        projects => {
          this.projects = projects
          console.log(this.projects);
        },
        error => this.errorMessage = <any>error
      );
  }

  getProjectsUserIsMemberOf(): void {
    this.requestsService.getMemberProjects()
      .subscribe(
        projects => {
          this.projects = projects
          console.log(this.projects);
        },
        error => this.errorMessage = <any>error
      );
  }

  getDevs(): void {
    this.requestsService.getDevs()
      .subscribe(
        projects => {
          this.devs = projects
        },
        error => this.errorMessage = <any>error
      );
  }

  filterDevsBySkill(skill: string): void {
    this.requestsService.getDevs()
      .subscribe(
        projects => {
          this.devs = projects
          this.filterDevs(skill);
        },
        error => this.errorMessage = <any>error
      );
  }

  filterDevs(skill: string) {
    this.devs = this.devs.filter(d => {
      const arr =d.skills.split(",");
      return arr.indexOf(skill) > -1;
    })
  }

  assignDevsToProjects(devName: string, projects: string): void {
    this.requestsService.assignDevToProjects(devName, projects)
      .subscribe(_ => _);
  }
}
