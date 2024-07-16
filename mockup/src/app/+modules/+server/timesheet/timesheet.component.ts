import {Component, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import {Subscription} from 'rxjs';
import {TimesheetService} from '../../../_services/mock-up-service/timesheet.service';
import {ProjectModel} from '../../../_models/project.model';
import {TimesheetModel} from '../../../_models/timesheet/timesheet.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {AuthenticationService} from '../../../_services/authentication.service';

@Component({
  selector: 'app-timesheet',
  templateUrl: './timesheet.component.html',
  styleUrls: ['./timesheet.component.css']
})
export class TimesheetComponent implements OnInit {

  role: string;

  modalRef: BsModalRef;
  title = '';
  teamsheet$: TimesheetModel[];
  timeseetSub$: Subscription;
  timeseet: TimesheetModel;

  timesheetForm: FormGroup;
  submitted = false;
  success = false;

  totalItems = 6;
  currentPage = 1;
  smallnumPages = 0;
  itemsPerPage = 10;
  totalPage = 5;

  timeSheetModel$: TimesheetModel[];

  userInfor: any = '';

  constructor(private timesheetService: TimesheetService,
              private authenticationService: AuthenticationService,
              private modalService: BsModalService,
              private route: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.timesheetForm = this.formBuilder.group({
      task: ['', Validators.required],
      result: ['', Validators.required],
      note: ['', Validators.required]
    });
    this.role = JSON.parse(localStorage.getItem('currentUser')).roles;
    console.log('---------------------', this.role);
    if (this.role && this.role != null) {
      console.log('ROLE::', this.role);
      if (this.role === 'MANAGER') {
        this.loadData(this.itemsPerPage, this.currentPage);
      }
      else {
        this.getTimeSheetByUserid(this.itemsPerPage, this.currentPage);
      }
    }
    this.userInfor = JSON.parse(localStorage.getItem('currentUser'));
    console.log('----- curret USER :: ', this.userInfor);
    // this.loadProjectForm();

    // console.log(`role ${this.role}`);
  }

  loadData(pageSize: number, page: number) {
    this.timesheetService.getData(pageSize, page).subscribe(timesheet => {
      console.log('------- loadData::', timesheet);
      console.log(timesheet);
      this.teamsheet$ = timesheet;
      this.totalItems = timesheet['totalRow'];
      console.log(`totalRow ${timesheet['totalRow']}`);
      this.totalPage = timesheet['totalPage'];
      console.log(`totalPage ${this.totalPage}`);
      console.log(`load data project ${this.totalItems}`);

    });
  }

  getTimeSheetByUserid(pageSize: number, page: number) {
    this.timesheetService.getTimesheetByUser(pageSize, page - 1).subscribe(timesheet => {
      console.log('------- getTimeSheetByUserid::', timesheet);
      this.teamsheet$ = timesheet;
      this.totalItems = timesheet['totalRow'];
      console.log(`totalRow ${timesheet['totalRow']}`);
      this.totalPage = timesheet['totalPage'];
      console.log(`totalPage ${this.totalPage}`);
      console.log(`load data project ${this.totalItems}`);
    });
  }

  addData() {
    this.timeseetSub$ = this.timesheetService.addData(this.timesheetForm.value).subscribe(res => {
      this.timesheetForm.reset();
      console.log(res);
    });
  }


  // loadProjectForm() {
  //   this.timesheetForm = this.formBuilder.group({
  //     task: ['', Validators.required],
  //     result: ['', Validators.required],
  //     note: ['', Validators.required]
  //   });
  // }

  updateStatusTimeSheet(id: number, action: number) {
    this.timesheetService.updateStatus(id, action).subscribe(res => {
      console.log(res);
      this.ngOnInit();
    });
  }

  // pageChanged(event: any): void {
  //   console.log('Page changed to: ' + event.page);
  //   console.log('Number items per page: ' + event.itemsPerPage);
  //   console.log(this.teamsheet$['totalRow']);
  //   this.totalItems = this.teamsheet$['totalRow'];
  //   this.loadData(event.itemsPerPage, event.page);
  //
  // }
  pageChanged(page: number): void {
    this.currentPage = page;
    // console.log('Page changed to: ' + event.page);
    // console.log('Number items per page: ' + event.itemsPerPage);
    this.loadData(this.itemsPerPage, page);

  }

  openModalEditOrAdd(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template);
    this.title = 'New TimeSheet';
  }

  get f() {
    return this.timesheetForm.controls;
  }

  onSubmit() {
    this.submitted = true;
    if (this.timesheetForm.invalid) {
      console.log(`eeeeeeeeeeeeeee${this.timesheetForm.value}`);
      // alert('errr!! :-)\n\n' + JSON.stringify(this.projectForm.value, null, 9));
      return;
    }
    this.success = true;
    this.addData();
    this.modalRef.hide();
    this.loadData(this.itemsPerPage, this.currentPage);
  }

  // ngOnDestroy(): void {
  //   this.timeseetSub$.unsubscribe;
  //   console.log(this.timeseetSub$.unsubscribe);
  // }


}
