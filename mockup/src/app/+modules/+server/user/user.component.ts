import {Component, OnInit, TemplateRef} from '@angular/core';
import {UserService} from '../../../_services/mock-up-service/user.service';
import {UserRequestModel} from '../../../_models/user-request.model';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';
import {RolesModel} from '../../../_models/roles.model';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  modalRef: BsModalRef;
  user$: UserRequestModel[];
  role = '';
  userId: number;
  message = '';

  totalItems = 6;
  currentPage = 1;
  smallnumPages = 0;
  itemsPerPage = 10;
  totalPage = 5;

  constructor(private userService: UserService,
              private modalService: BsModalService) {
  }

  ngOnInit() {
    this.loadDataUser(10, 1);
  }

  loadDataUser(pageSize: number, page: number) {
    this.userService.getAllUser(pageSize, page - 1).subscribe(data => {
      this.user$ = data['listData'];
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
    this.loadDataUser(this.itemsPerPage, page);
  }

  deleteUser(id: number) {
    console.log(`idddddddddddddd ${id}`)
    this.userService.deleteUser(id).subscribe(res => {
      console.log(res);
      this.loadDataUser(10, 1);
    });
  }

  updateRole(id: number, role: number){
    this.userService.updateUser(id, role).subscribe(res=>{
      console.log(res);
      this.loadDataUser(10, 1);
    })
  }

  openConfirmDialog(userId: number, template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template);
    this.userId = userId;
    this.message = 'Are You Sure !!!!';
  }

  confirmDialog(): void {
    this.deleteUser(this.userId);
    this.modalRef.hide();
    // this.router.navigateByUrl('/ProjectComponent', {skipLocationChange: true}).then(() =>
    //   this.router.navigate(['/office/list-project']));
  }

  decline(): void {
    this.modalRef.hide();
  }


}
