import {Component, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import {ProjectServiceService} from '../../../_services/mock-up-service/project-service.service';
import {Observable, Subscription} from 'rxjs';
import {ProjectModel} from '../../../_models/project.model';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {ModalConfirmComponent} from '../../../share/modal-confirm/modal-confirm.component';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {TeamRequestModel} from '../../../_models/team-request.model';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {
  title = 'List Project';
  project$: ProjectModel[];
  modalRef: BsModalRef;

  projectForm: FormGroup;
  submitted = false;
  success = false;

  projectSub$: Subscription;
  project: ProjectModel;
  id: number;
  teamNumber: number;

  totalItems = 6;
  currentPage = 1;
  smallnumPages = 0;
  itemsPerPage = 10;
  totalPage = 5;


  constructor(private projectService: ProjectServiceService,
              private modalService: BsModalService,
              private route: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder) {
    // this.loadProjectForm();
  }

  ngOnInit() {
    this.projectForm = this.formBuilder.group({
      projectName: ['', Validators.required],
      deadline: ['', Validators.required],
      description: ['', Validators.required],
      // teamName: ['', Validators.required]
    });
    this.loadData(this.itemsPerPage, this.currentPage);
  }
  //
  // pageChanged(event: any): void {
  //   this.currentPage = page;
  //   console.log('Page changed to: ' + event.page);
  //   console.log('Number items per page: ' + event.itemsPerPage);
  //   this.loadData(event.itemsPerPage, event.page);
  //
  // }
  pageChanged(page: number): void {
    this.currentPage = page;
    // console.log('Page changed to: ' + event.page);
    // console.log('Number items per page: ' + event.itemsPerPage);
    this.loadData(this.itemsPerPage, page);

  }

  loadData(pageSize: number, page: number) {

    this.projectService.getData(pageSize, page).subscribe(data => {
      console.log(`load data project ${data}`);
      this.project$ = data;
      this.teamNumber = data['team'];
      this.totalItems = this.project$['totalRow'];
      console.log(`totalRow ${this.project$['totalRow']}`);
      this.totalPage = this.project$['totalPage'];
      console.log(`totalPage ${this.totalPage}`);
      console.log(`load data project ${this.totalItems}`);
    });
  }


  getProjectById() {
    this.projectSub$ = this.projectService.searchProjectById(this.id).subscribe(project => {
        this.project = project['data'];
        console.log(project);
      }
    );
  }

  // deleteProject(id: number) {
  //   console.log('dsadsadsáđâ');
  //   this.projectService.deleteData(id).subscribe(res => {
  //     console.log(res);
  //     this.loadData();
  //   });
  // }

  public openModal(projectId: number) {
    this.modalRef = this.modalService.show(ModalConfirmComponent, {
      initialState: {
        id: projectId,
        title: 'confirm',
        message: 'Are you sure ?',
        data: {},
        cancelBtnName: 'Cancel',
        okBtnName: 'Ok',
      }
    });

  }


  openModalEditOrAdd(projectId: number, template: TemplateRef<any>) {
    this.id = projectId;
    console.log(`idddd = ${this.id}`);
    this.modalRef = this.modalService.show(template);
    if (this.id != null) {
      this.title = 'Edit Project';
      this.getProjectById();
    } else {
      this.title = 'Add Project';
    }
    // this.loadProjectForm();

  }

  // loadProjectForm() {
  //   this.projectForm = this.formBuilder.group({
  //     projectName: ['', Validators.required],
  //     deadline: ['', Validators.required],
  //     description: ['', Validators.required],
  //     // teamName: ['', Validators.required]
  //   });
  // }

  get f() {
    return this.projectForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.projectForm.invalid) {
      console.log(`eeeeeeeeeeeeeee${this.projectForm.value}`);
      // alert('errr!! :-)\n\n' + JSON.stringify(this.projectForm.value, null, 9));
      return;
    }
    this.success = true;
    alert('SUCCESS!! :-)\n\n' + JSON.stringify(this.projectForm.value, null, 9));
    this.addOrUpdate();
    this.projectForm.reset();
    this.modalRef.hide();
    this.title = 'List Project';
    this.loadData(5, 1);
  }

  addOrUpdate(): void {
    console.log(`id update ${this.id}`);
    console.log(`name------${this.projectForm.controls.projectName.value}`);
    const projectName: string = this.projectForm.controls.projectName.value;
    const deadline: Date = this.projectForm.controls.deadline.value;
    const description: string = this.projectForm.controls.description.value;
    const teamName: string = this.projectForm.controls.teamName.value;
    // const teamModel: TeamRequestModel = new TeamRequestModel(teamName, 0, null, '');
    // const lstTeam: Array<TeamRequestModel> = [];
    // lstTeam.push(teamModel);
    // console.log(`dddddddddddddddddddddddddddd ${lstTeam}`);

    // const projectModel: ProjectModel = new ProjectModel(this.id, projectName, description, null, deadline, '', lstTeam);
    const projectModel: ProjectModel = new ProjectModel(this.id, projectName, description, null, deadline, '');

    if (this.id != null && this.id !== 0) {
      this.projectService.editData(projectModel).subscribe(res => {
        console.log(res);
        this.ngOnInit();
      });
    } else {
      // this.projectForm.reset();
      this.projectService.addData(projectModel).subscribe(res => {
        console.log(res);
        this.ngOnInit();
      });
    }

  }

  // ngOnDestroy(): void {
  //   this.projectSub$.unsubscribe;
  //   console.log(this.projectSub$.unsubscribe);
  // }

}
