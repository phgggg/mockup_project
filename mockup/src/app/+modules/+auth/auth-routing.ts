import {ModuleWithProviders, NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {AuthComponent} from './auth.component';
import {RegisterComponent} from './register/register.component';
import {ProfileComponent} from './profile/profile.component';

const authRoutes: Routes = [
  {
    path: '',
    component: AuthComponent,
    data: {pageTitle: 'Auth'},
    resolve: {
      // isAuthenticated: HomeAuthResolver
    },
    children: [
      {
        path: 'login',
        component: LoginComponent
      },
      {
        path: 'register',
        component: RegisterComponent
      },
      {
        path: 'profile',
        component: ProfileComponent
      }
      // {
      //   path: 'resister'
      // }
    ]
  }
];

export const AuthRouting: ModuleWithProviders = RouterModule.forChild(authRoutes);
