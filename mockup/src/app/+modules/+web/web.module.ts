import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {WebRoutingModules} from './web-routing.modules';
import {WebHomePageComponent} from './web-home-page/web-home-page.component';


@NgModule({
  declarations: [
    WebHomePageComponent,
  ],
  imports: [
    CommonModule,
    WebRoutingModules,
    // ReactiveFormsModule,
    // HttpClientModule,
  ]
})
export class WebModule { }
