import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {TimesheetModel} from '../../_models/timesheet/timesheet.model';
import {environment} from '../../../environments/environment';
import {BaseHttpServiceService} from '../base-http-service.service';

@Injectable({
  providedIn: 'root'
})
export class TimesheetService extends BaseHttpServiceService {

  constructor(private  http: HttpClient
  ) {
    super();
  }

  getData(pageSize: number, page: number): Observable<TimesheetModel[]> {
    return this.http.get<TimesheetModel[]>(`${environment.timesheet_url}/list/?pageSize=${pageSize}&page=${page}`).pipe(
      tap(res => this.log(`get list Timesheet`)),
      catchError(this.handleError<TimesheetModel[]>('get list Timesheet err', []))
    );
  }


  editData(timesheet: TimesheetModel): Observable<TimesheetModel> {
    console.log(`edit project in service with project model: ${timesheet}`, this.httpOntions);
    return this.http.put<TimesheetModel>(`${environment.timesheet_url}`, timesheet).pipe(
      tap(res => this.log(`update profile by id = ${timesheet.timesheetId}`)),
      catchError(this.handleError<TimesheetModel>('update profile err'))
    );
  }

  addData(timesheet: TimesheetModel): Observable<TimesheetModel> {
    return this.http.post<TimesheetModel>(`${environment.api_url}/timesheet`, timesheet).pipe(
      tap((newTimeSheet: TimesheetModel) => this.log(`add new timesheet id = ${newTimeSheet.timesheetId}`)),
      catchError(this.handleError<TimesheetModel>('add new timesheet err'))
    );
  }

  deleteData(id: number): Observable<TimesheetModel> {
    const url = `${environment.timesheet_url}?id=${id}`;
    return this.http.delete<TimesheetModel>(url).pipe(
      tap(res => this.log(`delete object id =  ${id}`)),
      catchError(this.handleError<TimesheetModel>('delete timesheet err'))
    );
  }

  getTimesheetByUser(pageSize: number, page: number): Observable<TimesheetModel[]> {
    return this.http.get<TimesheetModel[]>(`${environment.timesheet_url}/listByUser/?pageSize=${pageSize}&page=${page}`).pipe(
      tap(res => this.log(`get getTimesheetByUser susses`)),
      catchError(this.handleError<TimesheetModel[]>('get getTimesheetByUser err', []))
    );
  }

  updateStatus(id: number, status: number): Observable<TimesheetModel> {
    return this.http.put<TimesheetModel>(`${environment.timesheet_url}/updateStatus?id=${id}&status=${status}`, {}).pipe(
      tap(res => this.log(` updateStatus by id = ${id}`)),
      catchError(this.handleError<TimesheetModel>(' updateStatus err'))
    );
  }
}
