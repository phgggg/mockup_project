import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ProjectServiceService} from '../../../_services/mock-up-service/project-service.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs';
import {ProjectModel} from '../../../_models/project.model';

@Component({
  selector: 'app-add-or-update-project',
  templateUrl: './add-or-update-project.component.html',
  styleUrls: ['./add-or-update-project.component.css']
})
export class AddOrUpdateProjectComponent implements OnInit, OnDestroy {
  title = '';
  projectForm: FormGroup;
  submitted = false;
  success = false;

  id: number;
  projectSub$: Subscription;
  project: ProjectModel;


  constructor(private projectService: ProjectServiceService,
              private route: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.id = +this.route.snapshot.paramMap.get('id');
    console.log(this.id);
    if (this.id != null) {
      this.title = 'Edit Project';
      this.getProjectById();
    } else {
      this.title = 'Add Project';
    }
    this.loadProjectForm();

  }

  getProjectById() {
    this.projectSub$ = this.projectService.searchProjectById(this.id).subscribe(project => {
        this.project = project;
        console.log(project);
      }
    );
  }



  ngOnDestroy(): void {
    this.projectSub$.unsubscribe;
    console.log(this.projectSub$.unsubscribe);
  }


  loadProjectForm() {
    this.projectForm = this.formBuilder.group({
      projectName: ['', Validators.required],
      deadLine: ['', Validators.required],
      description: ['', Validators.required]
      // createdDate: ['', Validators.required]
    });
  }

  get f() {
    return this.projectForm.controls;
  }



}
