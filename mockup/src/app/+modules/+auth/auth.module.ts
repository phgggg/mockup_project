import {NgModule} from '@angular/core';
import {AuthComponent} from './auth.component';
import {LoginComponent} from './login/login.component';
import {AuthRouting} from './auth-routing';
import {AuthenticationService} from '../../_services/authentication.service';
import {ShareModule} from '../../share/share.module';
import { RegisterComponent } from './register/register.component';
import { ProfileComponent } from './profile/profile.component';

@NgModule({
  imports: [ShareModule, AuthRouting],
  declarations: [
    AuthComponent,
    LoginComponent,
    RegisterComponent,
    ProfileComponent,
  ],
  exports: [],
  providers: [AuthenticationService]
})
export class AuthModule {
}
