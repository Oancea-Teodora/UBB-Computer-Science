import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {WebsiteLiteComponent} from "./website-lite/website-lite.component";

const routes: Routes = [
  {path: "login", component: LoginComponent},
  {path: "website", component: WebsiteLiteComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, /*{ useHash: true}*/)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
