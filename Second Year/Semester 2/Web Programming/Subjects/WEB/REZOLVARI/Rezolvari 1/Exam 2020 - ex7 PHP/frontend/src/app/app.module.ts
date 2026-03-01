import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { FormsModule } from "@angular/forms";
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { WebsiteLiteComponent } from './website-lite/website-lite.component';
import {PageNotFoundComponent} from "./page-not-found/page-not-found.component";
import { UpdateDocumentComponent } from './update-document/update-document.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    WebsiteLiteComponent,
    PageNotFoundComponent,
    UpdateDocumentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot([ {path: "login", component: LoginComponent},
      {path: "website", component: WebsiteLiteComponent},
      {path: "error", component: PageNotFoundComponent},
      {path: "update/:id", component: UpdateDocumentComponent}
    ], )
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
