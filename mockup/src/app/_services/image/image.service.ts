import {Injectable} from '@angular/core';
import {BaseHttpServiceService} from '../base-http-service.service';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {catchError, tap} from 'rxjs/operators';
import {Observable} from 'rxjs';
import {ImageModel} from '../../_models/image/image.model';

@Injectable({
  providedIn: 'root'
})
export class ImageService extends BaseHttpServiceService {

  constructor(private http: HttpClient) {
    super();
  }


  uploadFile(formdata: FormData): Observable<ImageModel> {
    return this.http.post<ImageModel>(`${environment.api_url}/image`, formdata).pipe(
      tap(res => this.log('add image')),
      catchError(this.handleError<ImageModel>('add image error'))
    );
  }


  searchImageById(id: number): Observable<ImageModel> {
    const url = `${environment.api_url}/image?id=${id}`;
    return this.http.get<ImageModel>(url).pipe(
      tap(res => this.log(`searchImageById image id = ${id}`)),
      catchError(this.handleError<ImageModel>('searchImageById error'))
    );
  }


}
