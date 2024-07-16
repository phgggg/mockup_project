import {Component, OnDestroy, OnInit} from '@angular/core';
import {ProjectServiceService} from '../../../../_services/mock-up-service/project-service.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';
import {ProjectModel} from '../../../../_models/project.model';

@Component({
  selector: 'app-project-detail',
  templateUrl: './project-detail.component.html',
  styleUrls: ['./project-detail.component.css']
})
export class ProjectDetailComponent implements OnInit, OnDestroy {

  titlee: 'Project Detail';

  projectSub$: Subscription;
  project: ProjectModel;


  id: number;
  constructor(private projectService: ProjectServiceService,
              private route: ActivatedRoute,
  ) { }

  ngOnInit() {
    this.id = + this.route.snapshot.paramMap.get('id'); // map :id
    this.loadData();
  }

  loadData() {
    console.log(`this.id ${this.id}`)    ;
    this.projectSub$ = this.projectService.searchProjectById(this.id).subscribe(project => {
      this.project = project['data'];
      console.log(project);
    });
  }

  ngOnDestroy(): void {
    this.projectSub$.unsubscribe();
    console.log(this.projectSub$.unsubscribe());
  }

}
