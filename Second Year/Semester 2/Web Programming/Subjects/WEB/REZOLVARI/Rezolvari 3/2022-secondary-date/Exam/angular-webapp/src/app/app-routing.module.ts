import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginMenuComponent} from "./login-menu/login-menu.component";
import {UserMenuComponent} from "./user-menu/user-menu.component";

const routes: Routes = [
  { path: 'login', component: LoginMenuComponent },
  { path: 'user', component: UserMenuComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
