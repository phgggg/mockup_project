import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BaseHttpServiceService {
  constructor() {
  }

  httpOntions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      console.log(`${operation} failed: ${error.message}`);
      return of(result as T);
    };
  }

  log(msg: any) {
    console.log(msg);
  }

  err(msg: any) {
    console.error(msg);
  }

  warn(msg: any) {
    console.warn(msg);
  }

}
