import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {LoginComponent} from './+modules/+auth/login/login.component';
import {RegisterComponent} from './+modules/+auth/register/register.component';
import {ResetPasswordComponent} from './+modules/+auth/reset-password/reset-password.component';
import {AdminComponent} from './+modules/+server/admin/admin.component';
import {TrangChuComponent} from './+modules/+web/trang-chu/trang-chu.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ModalModule} from 'ngx-bootstrap';
import {ModalConfirmComponent} from './share/modal-confirm/modal-confirm.component';
import {JwtInterceptor} from './_services/interceptors/jwt.interceptor';
import {ErrorInterceptor} from './_services/interceptors/error.interceptor';
import {ToastrModule} from 'ngx-toastr';
import {ShareModule} from './share/share.module';


@NgModule({
  declarations: [
    AppComponent,
    AdminComponent,
    TrangChuComponent,
    // LoginComponent,
    // RegisterComponent,
    // ResetPasswordComponent,
    ModalConfirmComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ShareModule,
    BrowserAnimationsModule,
    ModalModule.forRoot(),
    ToastrModule.forRoot({
      timeOut: 1000,
      positionClass: 'toast-bottom-right',
      preventDuplicates: true
    })
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}
    ],
  bootstrap: [AppComponent],
  entryComponents: [ModalConfirmComponent]
})
export class AppModule {
}
