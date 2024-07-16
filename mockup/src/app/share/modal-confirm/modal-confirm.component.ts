import {Component, OnInit} from '@angular/core';
import {Action, BsModalRef} from 'ngx-bootstrap';
import {Router} from '@angular/router';
import {ProjectServiceService} from '../../_services/mock-up-service/project-service.service';
import {Observable} from 'rxjs';
import {ProjectModel} from '../../_models/project.model';

@Component({
  selector: 'app-modal-confirm',
  templateUrl: './modal-confirm.component.html',
  styleUrls: ['./modal-confirm.component.css']
})
export class ModalConfirmComponent implements OnInit {

  // project$: Observable<ProjectModel[]>;
  data = {};
  title = '';
  message = '';
  id: number;

  // noinspection JSAnnotator

  constructor(public modalRef: BsModalRef, private projectService: ProjectServiceService,
              private router: Router) {
  }

  ngOnInit() {
  }

  confirmDialog(): void {
    this.onDeleteProjetc();
    this.modalRef.hide();
    this.router.navigateByUrl('/ProjectComponent', {skipLocationChange: true}).then(() =>
      this.router.navigate(['/office/list-project']));
  }
  //
  decline(): void {
    this.modalRef.hide();
  }

  onDeleteProjetc() {
    console.log(`dididididid  ${this.id}`);
    this.projectService.deleteData(this.id).subscribe(res => {
      console.log(res);
    });
  }
}
