import {Injectable} from '@angular/core';
import {BaseHttpServiceService} from '../base-http-service.service';
import {Observable} from 'rxjs';
import {ProjectModel} from '../../_models/project.model';
import {HttpClient} from '@angular/common/http';
import {catchError, tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProjectServiceService extends BaseHttpServiceService {
  private ROOT_URL = 'http://localhost:9999/api/projects';

  constructor(private http: HttpClient) {
    super();
  }

  getData(pageSize: number, page: number): Observable<ProjectModel[]> {
    const url = `${this.ROOT_URL}?pageSize=${pageSize}&page=${page}`;
    return this.http.get<ProjectModel[]>(url).pipe(
      tap(res => this.log(`get list project`)),
      catchError(this.handleError<ProjectModel[]>('get list project err', []))
    );
  }

  deleteData(id: number): Observable<ProjectModel> {
    const url = `${this.ROOT_URL}?id=${id}`;
    return this.http.delete<ProjectModel>(url).pipe(
      tap(res => this.log(`delete object id =  ${id}`)),
      catchError(this.handleError<ProjectModel>('delete object err'))
    );
  }

  editData(projectModel: ProjectModel): Observable<ProjectModel> {
    console.log(`edit project in service with project model: ${projectModel}`, this.httpOntions);
    return this.http.put<ProjectModel>(`${this.ROOT_URL}`, projectModel).pipe(
      tap(res => this.log(`update project by id = ${projectModel.projectId}`)),
      catchError(this.handleError<ProjectModel>('update project err'))
    );
  }

  addData(projectModel: ProjectModel): Observable<ProjectModel> {
    return this.http.post<ProjectModel>(`${this.ROOT_URL}`, projectModel).pipe(
      tap((newProject: ProjectModel) => this.log(`add new project id = ${newProject.projectId}`)),
      catchError(this.handleError<ProjectModel>('add new project err'))
    );
  }

  searchProjectById(id: number): Observable<ProjectModel> {
    const url = `${this.ROOT_URL}/searchProjectById?id=${id}`;
    return this.http.get<ProjectModel>(url).pipe(
      tap(res => this.log(`search projeect by id = ${id}`)),
      catchError(this.handleError<ProjectModel>('search project by id err'))
    );
  }


}
