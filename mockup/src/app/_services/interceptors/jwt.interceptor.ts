import { Injectable, Injector } from '@angular/core';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHeaders} from '@angular/common/http';
import { Observable } from 'rxjs';
import {AuthenticationService} from '../authentication.service';
import {environment} from '../../../environments/environment';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AuthenticationService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add auth header with jwt if user is logged in and request is to api url
    const currentUser = this.authenticationService.currentUserValue;
    const isLoggedIn = localStorage.getItem('currentUser') && JSON.parse(localStorage.getItem('currentUser')).token;
    const isApiUrl = request.url.startsWith(environment.api_url);

    // let ignore =
    //     //   typeof request.body === "undefined"
    //     //   || request.body === null
    //     //   || request.body.toString() === "[object FormData]" // <-- This solves your problem
    //     //   || request.headers.has("Content-Type");
    //     // //
    //     // if (ignore) {
    //     //   // const req =  request.clone();
    //     //   return next.handle(request);
    //     // }


    const headersConfig = {
      'Accept': 'application/json'
    };
    console.log(`isLoggedIn ${isLoggedIn}`)
    console.log(`isApiUrl ${isApiUrl}`)
    if (isLoggedIn && isApiUrl) {
      console.log(currentUser);
      headersConfig['Auth-Token'] = JSON.parse(localStorage.getItem('currentUser')).token;
      console.log(`currentUser.token`, JSON.parse(localStorage.getItem('currentUser')).token);
    }
    const req =  request.clone({ setHeaders: headersConfig });

    return next.handle(req);



    // let contentType = 'application/json';
    //
    // /**
    //  * But when the data of the body is an instance of FormData, we can assume that we are uploading an file.
    //  * Therefore, we need to change the contentType to multipart/form-data.
    //  */
    // if (request.body instanceof FormData) {
    //   // we are sending a file here
    //   const headersConfig = {
    //     'Content-Type': 'multipart/form-data'
    //   }
    //   headersConfig['Auth-Token'] = currentUser.token;
    //   const req =  request.clone({ setHeaders: headersConfig });
    //   return next.handle(req);
    // } else {
    //   const headersConfig = {
    //     'Content-Type': 'application/json',
    //     'Accept': 'application/json'
    //   };
    //   if (isLoggedIn && isApiUrl) {
    //     headersConfig['Auth-Token'] = currentUser.token;
    //   }
    //   const req =  request.clone({ setHeaders: headersConfig })
    //   return next.handle(req);
    // }
    //
    // const headersConfig = {
    //   'Content-Type': 'application/json',
    //   'Accept': 'application/json'
    // };
    //
    // if (isLoggedIn && isApiUrl) {
    //   headersConfig['Auth-Token'] = currentUser.token;
    // }
    //
    // const req =  request.clone({ setHeaders: headersConfig })
    //
    // return next.handle(req);
  }
}
