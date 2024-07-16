import {Component, OnInit} from '@angular/core';
import {ProjectServiceService} from '../../../_services/mock-up-service/project-service.service';
import {Observable, Subscription} from 'rxjs';
import {ProjectModel} from '../../../_models/project.model';

@Component({
  selector: 'app-peoject-detail',
  templateUrl: './peoject-detail.component.html',
  styleUrls: ['./peoject-detail.component.css']
})
export class PeojectDetailComponent implements OnInit {

  project$: Observable<ProjectModel[]>;

  constructor(private projectService: ProjectServiceService) {
    // this.loadData();

  }

  ngOnInit() {
  }
  //
  // loadData() {
  //   this.project$ = this.projectService.getData(10, 1);
  // }

}
