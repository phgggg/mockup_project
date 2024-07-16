import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {WebHomePageComponent} from './web-home-page/web-home-page.component';

const routes: Routes = [
  {
    path: 'home-page',
    component: WebHomePageComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class WebRoutingModules {
}
