import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';
import {AuthenticationService} from '../../../../_services/authentication.service';

@Component({
  selector: 'app-web-header',
  templateUrl: './web-header.component.html',
  styleUrls: ['./web-header.component.scss']
})
export class WebHeaderComponent implements OnInit {

  menuHidden = true;


  constructor(
    private router: Router,
    private i18nService: TranslateService,
    private authService: AuthenticationService,

  ) {
  }

  ngOnInit() {
  }

  toggleMenu() {
    this.menuHidden = !this.menuHidden;
  }

  setLanguage(language: string) {
    this.i18nService.use(language);
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/auth/login'], {replaceUrl: true});
  }


  get currentLanguage(): string {
    return this.i18nService.currentLang;
  }

  get languages(): string[] {
    return this.i18nService.getLangs();
  }

  get username(): string | null {

    const user = JSON.parse(localStorage.getItem('currentUser'));
    return user ? user.username : null;
    return null;
  }

  // get roles(): string | null {
  //   //   const user = JSON.parse(localStorage.getItem('currentUser')).role);
  //   //   console.log(`qqqqqqqqqqqqqqqqqqqqqqqq ${user}`);
  //   //   return user ? user.token : null;
  //   //   return null;
  //   // }



}
