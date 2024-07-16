import {Component, OnInit, TemplateRef, ViewEncapsulation} from '@angular/core';
import {ProjectServiceService} from '../../../_services/mock-up-service/project-service.service';
import {UserService} from '../../../_services/mock-up-service/user.service';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {ActivatedRoute, Router} from '@angular/router';
import {Form, FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ProjectModel} from '../../../_models/project.model';
import {UserRequestModel} from '../../../_models/user-request.model';
import {Subscription} from 'rxjs';
import {TeamService} from '../../../_services/mock-up-service/team.service';
import {TeamRequestModel} from '../../../_models/team-request.model';
import {NgbModal, NgbActiveModal, NgbModalOptions} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-new-team',
  templateUrl: './new-team.component.html',
  styleUrls: ['./new-team.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class NewTeamComponent implements OnInit {

  title: 'New Team';
  modalRef: BsModalRef;

  projectId: number;
  userId: number;
  // projectForm: FormGroup;

  myForm: FormGroup;
  submitted = false;
  success = false;

  project$: ProjectModel[];
  projectSub$: Subscription;
  project: ProjectModel;
  idProject: number;

  lstuser: Array<UserRequestModel> = [];
  projectModel: {} = ProjectModel;

  user$: UserRequestModel[];
  user: UserRequestModel;

  formArray: FormArray;

  constructor(
    private projectService: ProjectServiceService,
    private userService: UserService,
    private teamService: TeamService,
    private modalService: BsModalService,
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder
  ) {
  }

  ngOnInit() {
    this.loadDataProject(10, 1);
    this.loadDataUser(10, 1);
    this.myForm = this.formBuilder.group({
      userId: this.formBuilder.array([]),
      teamName: ['', Validators.required],
      project: ['fasle', Validators.required],
    });
  }

  get f() {
    return this.myForm.controls;
  }

  getUserById(id: number) {
    // this.userService
  }

  onChange(userId: number, isChecked: boolean) {
    this.formArray = this.myForm.controls.userId as FormArray;
    const lst: Array<UserRequestModel> = [];
    if (isChecked) {
      this.formArray.push(new FormControl(userId));

      for (let i = 0; i < this.formArray.value.length; i++) {
        console.log(`eeeeeeeeeeeeeeeeeeeeeee${this.formArray.value[i]}`);
        this.userService.getUserById(this.formArray.value[i]).subscribe(data => {
          this.user = data['data'];
          // const userModel: UserRequestModel = new UserRequestModel(this.formArray.value[i]);
          const userModel: UserRequestModel = new UserRequestModel(data['data'].userId, data['data'].userName, data['data'].email, data['data'].skypeName );
          lst.push(userModel);
        });

      }
      for (const user of lst) {
        const match = this.lstuser.filter((user) => user.userId === this.userId);
        console.log(`match ${match}`);
        match ? this.lstuser.splice(this.lstuser.indexOf(user), 1) : null;
        break;
      }
      this.lstuser = lst;
      console.log(this.formArray);
    } else {
      const index = this.formArray.controls.findIndex(x => x.value === userId);
      console.log(this.lstuser);
      console.log(`index ${index}`);
      this.formArray.removeAt(index);

      for (const user of this.lstuser) {
        const match = this.lstuser.filter((user) => user.userId === userId);
        match ? this.lstuser.splice(this.lstuser.indexOf(user), 1) : null;
        break;
      }
    }
    console.log(`lstUser ${this.lstuser.length}`);
    // for (const user of this.lstuser){
    //   console.log('user id', user.userId );
    // }
  }

  onChangeProject(projectId: number, isChecked: boolean) {
    if (isChecked) {
      this.projectId = projectId;
    }
  }


  loadDataProject(pageSize: number, page: number) {
    this.projectService.getData(pageSize, page).subscribe(data => {
      console.log(`load data project ${data}`);
      this.project$ = data['listData'];
      // this.totalItems = this.project$['totalRow'];
    });
  }

  loadDataUser(pageSize: number, page: number) {
    this.userService.getAllUser(pageSize, page - 1).subscribe(data => {
      console.log(`load data project ${data}`);
      this.user$ = data['listData'];
    });
  }

  getProjectById() {
    this.projectSub$ = this.projectService.searchProjectById(this.projectId).subscribe(project => {
        this.project = project['data'];
        console.log(project);
      }
    );
  }

  addTeam() {
    const teamName: string = this.myForm.controls.teamName.value;
    if (this.lstuser == null) {
      console.log(`this.listuser = 0`);
    }
    const teamModel: TeamRequestModel = new TeamRequestModel(teamName, this.projectModel, this.lstuser);
    this.teamService.addData(teamModel).subscribe(res => {
      console.log(res);
    });
  }


  onSubmit() {
    this.submitted = true;
    if (this.myForm.invalid) {
      console.log(`eeeeeeeeeeeeeee${this.myForm.value}`);
      alert('errr!! :-)\n\n' + JSON.stringify(this.myForm.value, null, 9));
      return;
    }
    this.success = true;
    alert('SUCCESS!! :-)\n\n' + JSON.stringify(this.myForm.value, null, 9));
    this.addTeam();
    // this.lstuser = null;
    // this.formArray.clear();
    // this.modalRef.hide();
  }

  onSubmitProject() {
    const project: ProjectModel = new ProjectModel(this.projectId, '', '', null, null, '');
    console.log(`projectModel ${project.projectId}`);
    this.projectModel = project;
    console.log(`projectModel ${this.projectModel}`);
    this.getProjectById();
    this.modalRef.hide();

  }

  onSubmitUser() {
    this.modalRef.hide();
    if (this.lstuser.length > 0) {
      this.lstuser = null;
      console.log(`this.lstuser.length = 0`);
    }
    if (this.formArray.length > 0) {
      this.formArray.clear();
    }
    console.log(`this.formArray.length ${this.formArray.length}`);
  }

  openModalDataProject(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template, {class: 'modal-xl'});
  }

}
