import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {PageNotFoundComponent} from './share/page-not-found/page-not-found.component';
import {AdminComponent} from './+modules/+server/admin/admin.component';
import {TrangChuComponent} from './+modules/+web/trang-chu/trang-chu.component';
import {RedirecToComponent} from './share/redirec-to/redirec-to.component';


const routes: Routes = [
  {
    path: '',
    redirectTo: '/web/home-page',
    pathMatch: 'full'
  },
  {
    path: 'office',
    component: AdminComponent,
    children: [
      {
        path: '',
        loadChildren: () => import('./+modules/+server/mock-up.module').then(m => m.MockUpModule)
      }
    ]
  },

  {
    path: 'web',
    component: TrangChuComponent,
    children: [
      {
        path: '',
        loadChildren: () => import('./+modules/+web/web.module').then(m => m.WebModule)
      },
      {
        path: 'confirm/:userName',
        component: RedirecToComponent,
      }
    ]
  },

  {
    path: 'auth',
    component: TrangChuComponent,
    children: [
      {
        path: '',
        loadChildren: () =>
          import('./+modules/+auth/auth.module').then(m => m.AuthModule
          )
      }
    ]
  },

  {
    path: '**',
    component: PageNotFoundComponent
  },


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
