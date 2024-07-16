import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ProjectModel} from '../../_models/project.model';
import {catchError, tap} from 'rxjs/operators';
import {TeamRequestModel} from '../../_models/team-request.model';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {BaseHttpServiceService} from '../base-http-service.service';

@Injectable({
  providedIn: 'root'
})
export class TeamService extends BaseHttpServiceService {

  constructor(private http: HttpClient
  ) {
    super();
  }

  getData(pageSize: number, page: number): Observable<TeamRequestModel[]> {
    const url = `${environment.api_url}/team/search-all-team?pageSize=${pageSize}&page=${page}`;
    return this.http.get<TeamRequestModel[]>(url).pipe(
      tap(res => this.log(`get list team`)),
      catchError(this.handleError<TeamRequestModel[]>('get list team err', []))
    );
  }

  addData(team: TeamRequestModel): Observable<TeamRequestModel> {
    return this.http.post<TeamRequestModel>(`${environment.api_url }/team/new-team`, team).pipe(
      tap((newTeam: TeamRequestModel) => this.log(`add new newTeam id = ${newTeam.teamId}`)),
      catchError(this.handleError<TeamRequestModel>('add new newTeam err'))
    );
  }

  editData(team: TeamRequestModel): Observable<TeamRequestModel> {
    console.log(`edit team in service with team model: ${team}`);
    return this.http.post<TeamRequestModel>(`${environment.api_url}/team/update-team`, team).pipe(
      tap(res => this.log(`update team by id = ${team.teamId}`)),
      catchError(this.handleError<TeamRequestModel>('update team err'))
    );
  }

  searchTeamById(id: number): Observable<TeamRequestModel> {
    const url = `${environment.api_url}/team/search-teamId?id=${id}`;
    return this.http.get<TeamRequestModel>(url).pipe(
      tap(res => this.log(`search team by id = ${id}`)),
      catchError(this.handleError<TeamRequestModel>('search team by id err'))
    );
  }

  deleteData(id: number): Observable<TeamRequestModel> {
    const url = `${environment.api_url}/team/deleteTeam?id=${id}`;
    return this.http.delete<TeamRequestModel>(url).pipe(
      tap(res => this.log(`delete object id =  ${id}`)),
      catchError(this.handleError<TeamRequestModel>('delete object err'))
    );
  }


}
