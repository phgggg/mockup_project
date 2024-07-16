import {Injectable} from '@angular/core';
import {ProjectModel} from '../../_models/project.model';
import {Observable} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {UserRequestModel} from '../../_models/user-request.model';
import {BaseHttpServiceService} from '../base-http-service.service';
import {EmailModel} from '../../_models/email/email.model';

@Injectable({
  providedIn: 'root'
})
export class UserService extends BaseHttpServiceService {

  constructor(private http: HttpClient) {
    super();
  }

  addData(user: UserRequestModel): Observable<UserRequestModel> {
    return this.http.post<UserRequestModel>(`${environment.api_url}/register`, user).pipe(
      tap((newUser: UserRequestModel) => this.log(`add new user id = ${newUser.userId}`)),
      catchError(this.handleError<UserRequestModel>('add new user err'))
    );
  }

  getInfo(): Observable<UserRequestModel> {
    return this.http.get<UserRequestModel>(`${environment.api_url}/info`).pipe(
      tap(res => this.log(`get info user `)),
      catchError(this.handleError<UserRequestModel>('get info user err'))
    );
  }

  editData(user: UserRequestModel): Observable<UserRequestModel> {
    console.log(`edit project in service with project model: ${user}`, this.httpOntions);
    return this.http.put<UserRequestModel>(`${environment.api_url}/updateProfile`, user).pipe(
      tap(res => this.log(`update profile by id = ${user.userId}`)),
      catchError(this.handleError<UserRequestModel>('update profile err'))
    );
  }

  getAllUserNotListId(): Observable<UserRequestModel[]> {
    return this.http.get<UserRequestModel[]>(`${environment.api_url}/get-allUser-not-listId`).pipe(
      tap(res => this.log(`getAllUserNotListId user`)),
      catchError(this.handleError<UserRequestModel[]>('getAllUserNotListId user err', []))
    );
  }

  updateActiveUser(userName: string): Observable<any> {
    return this.http.get<any>(`${environment.api_url}/updateActiveUser?userName=${userName}`).pipe(
      tap(res => this.log(`update user active ${userName}`)),
      catchError(this.handleError<any>('update user active err'))
    );
  }

  updateUser(id: number, role: number): Observable<any> {
    return this.http.post<any>(`${environment.api_url}/updateActiveUser?id=${id}&role=${role}`, {}).pipe(
      tap(res => this.log(`update user role ${id}`)),
      catchError(this.handleError<any>('update user role err'))
    );
  }

  getAllUser(pageSize: number, page: number): Observable<UserRequestModel[]> {
    return this.http.get<UserRequestModel[]>(`${environment.api_url}/findAllUser?pageSize=${pageSize}&page=${page}`).pipe(
      tap(res => this.log(`findAllUser user`)),
      catchError(this.handleError<UserRequestModel[]>('findAllUser user err', []))
    );
  }

  getUserById(id: number): Observable<UserRequestModel[]> {
    return this.http.get<UserRequestModel[]>(`${environment.api_url}/userById?id=${id}`).pipe(
      tap(res => this.log(`getUserById user`)),
      catchError(this.handleError<UserRequestModel[]>('getUserById user err', []))
    );
  }

  deleteUser(id: number): Observable<UserRequestModel> {
    return this.http.delete<UserRequestModel>(`${environment.api_url}/deleteUser?id=${id}`).pipe(
      tap(res => this.log(`deleteUser user`)),
      catchError(this.handleError<UserRequestModel>('deleteUser user err'))
    );
  }


}
