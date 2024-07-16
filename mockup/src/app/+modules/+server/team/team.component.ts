import {Component, OnInit, TemplateRef} from '@angular/core';
import {TeamService} from '../../../_services/mock-up-service/team.service';
import {TeamRequestModel} from '../../../_models/team-request.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {ActivatedRoute, Router} from '@angular/router';
import {ProjectModel} from '../../../_models/project.model';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.css']
})
export class TeamComponent implements OnInit {
  title: 'List Team';
  idTeam: number;
  message: string;


  modalRef: BsModalRef;

  teamForm: FormGroup;
  team$: TeamRequestModel[];

  totalItems = 6;
  currentPage = 1;
  smallnumPages = 0;
  itemsPerPage = 10;
  totalPage = 5;

  constructor(
    private teamService: TeamService,
    private modalService: BsModalService,
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder
  ) {
  }

  ngOnInit() {
    this.loadData(10, 1);
  }

  loadData(pageSize: number, page: number): void {
    this.teamService.getData(pageSize, page).subscribe(data => {
      this.team$ = data['listData'];
      this.totalItems = data['totalRow'];
      console.log(`totalRow ${data['totalRow']}`);
      this.totalPage = data['totalPage'];
      console.log(`totalPage ${this.totalPage}`);
      console.log(`load data project ${this.totalItems}`);
    });
  }

  pageChanged(page: number): void {
    this.currentPage = page;
    // console.log('Page changed to: ' + event.page);
    // console.log('Number items per page: ' + event.itemsPerPage);
    this.loadData(this.itemsPerPage, page);
  }

  deleteTeam(id: number) {
    this.teamService.deleteData(id).subscribe(res => {
      console.log(res);
      this.ngOnInit();
    });
  }

  openConfirmDialog(teamId: number, template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template);
    this.idTeam = teamId;
    this.message = 'Are You Sure !!!!';

  }
  confirmDialog(): void {
    this.deleteTeam(this.idTeam);
    this.modalRef.hide();
    // this.router.navigateByUrl('/ProjectComponent', {skipLocationChange: true}).then(() =>
    //   this.router.navigate(['/office/list-project']));
  }

  decline(): void {
    this.modalRef.hide();
  }

  // loadTeamForm() {
  //   this.teamForm = this.formBuilder.group({
  //     teamName: ['', Validators.required],
  // deadline: ['', Validators.required],
  // description: ['', Validators.required]
  // });
  // }


}
