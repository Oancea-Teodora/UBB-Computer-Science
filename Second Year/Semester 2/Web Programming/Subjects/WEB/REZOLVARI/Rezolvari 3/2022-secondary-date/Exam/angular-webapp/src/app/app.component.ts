import { Component } from '@angular/core';
import {RequestsService} from "./service/requests.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'angular-webapp';

  constructor(private requestsService: RequestsService, private router: Router) {
  }

  onSubmitNameBtnClicked(user: string): void {
    this.requestsService.saveUser(user).subscribe(_ => this.router.navigate(['/user']));
  }
}
