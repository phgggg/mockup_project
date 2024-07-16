import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from './layout/header/header.component';
import {FooterComponent} from './layout/footer/footer.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {NavComponent} from './layout/nav/nav.component';
import {PaginationComponent} from './pagination/pagination.component';
import {NavTopComponent} from './layout/nav/nav-top/nav-top.component';
import {WebHeaderComponent} from './layout/web/web-header/web-header.component';
import {WebFooterComponent} from './layout/web/web-footer/web-footer.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {TranslateLoader, TranslateModule, TranslateService} from '@ngx-translate/core';
import {ToastrModule} from 'ngx-toastr';
import {ListErrorsComponent} from './layout/list-errors/list-errors.component';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {BsDatepickerModule, BsDropdownModule, PaginationModule} from 'ngx-bootstrap';
import { RedirecToComponent } from './redirec-to/redirec-to.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';


export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    PageNotFoundComponent,
    NavComponent,
    PaginationComponent,
    NavTopComponent,
    WebHeaderComponent,
    WebFooterComponent,
    ListErrorsComponent,
    RedirecToComponent,
    // OnlynumberDirective,
    // OnlyNumericDirective
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    NgbModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      },
      isolate: false
    }),
  ],
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    NgbModule,
    TranslateModule,
    ToastrModule,
    // BsDatepickerModule,
    // PaginationModule,

    HeaderComponent,
    FooterComponent,
    PageNotFoundComponent,
    NavComponent,
    PaginationComponent,
    NavTopComponent,
    WebHeaderComponent,
    WebFooterComponent,
    ListErrorsComponent,

    // WebHomePageComponent,
    // ModalConfirmComponent
  ],
})
export class ShareModule {

  constructor(private translate: TranslateService) {
    translate.addLangs(['en', 'vi']);
    // this language will be used as a fallback when a translation isn't found in the current language
    translate.setDefaultLang('en');

    // the lang to use, if the lang isn't available, it will use the current loader to get them
    translate.use('en');

    const browserLang = translate.getBrowserLang();
    translate.use(browserLang.match(/en|vi/) ? browserLang : 'en');
  }
}
