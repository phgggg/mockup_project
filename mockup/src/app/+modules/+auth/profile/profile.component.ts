import {Component, OnDestroy, OnInit, TemplateRef} from '@angular/core';
import {UserService} from '../../../_services/mock-up-service/user.service';
import {Subscription} from 'rxjs';
import {UserRequestModel} from '../../../_models/user-request.model';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../../../_services/authentication.service';
import {ImageService} from '../../../_services/image/image.service';
import {ImageModel} from '../../../_models/image/image.model';
import {BsModalRef, BsModalService} from 'ngx-bootstrap';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  profileForm: FormGroup;
  newFile: File;

  modalRef: BsModalRef;

  imageModel: ImageModel;

  imageUrl: '';

  role: string;
  title: 'Profile';
  user: UserRequestModel;

  constructor(private userService: UserService,
              private imageService: ImageService,
              private modalService: BsModalService,
              private authenticationService: AuthenticationService,
              private formBuiber: FormBuilder) {
    this.role = authenticationService.currentUserValue.roles;
  }

  ngOnInit() {
    this.profileForm = this.formBuiber.group({
      // password: ['', Validators.required],
    });
    this.getInfo();
  }

  getInfo(): void {
    this.userService.getInfo().subscribe(res => {
      console.log(`get info user ${res['data']}`);
      this.user = res['data'];
      if (this.user.imageId != null) {
        this.searchImageById(this.user.imageId);
      }
    });
  }

  onChangeFile(event) {
    const file = event.target.files[0];
    this.newFile = file;
    console.log(this.newFile);

    const uploadData = new FormData();
    uploadData.append('file', this.newFile);
    console.log(`eeeeeeeeeeeeee${uploadData}`);
    console.log(`eeeeeeeeeeeeee${ this.newFile.name}`);

    // uploadData.append('id', id);
    this.imageService.uploadFile(uploadData).subscribe(data => {
      console.log('success', data);
      this.user.imageId = data['data'].imageId;
      this.imageUrl = data['data'].imageUrl;
    });

  }
  updateUser() {
    this.userService.editData(this.user).subscribe(res => {
      console.log(res);
      this.searchImageById(this.user.imageId);
      // this.imageUrl = this.user.imageUrl;
      console.log(this.imageUrl);
      console.log('this.updateUser(this.user)', this.user);
    });
  }

  searchImageById(id: number) {
    this.imageService.searchImageById(id).subscribe(data => {
      console.log(data);
      this.imageModel = data;
      this.imageUrl = this.imageModel['data'].imageUrl;
      console.log(`this.imageUrl ${this.imageUrl}`);
    });
  }

  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template);
  }

  loadProfileForm() {
    this.profileForm = this.formBuiber.group({
      fullName: ['', Validators.required],
      phone: ['', Validators.required],
      skypeName: ['', Validators.required],
      facebookUrl: ['', Validators.required],
      address: ['', Validators.required],
      education: ['', Validators.required],
      university: ['', Validators.required],
      faculty: ['', Validators.required],
      graduationDate: ['', Validators.required],
      reason: ['', Validators.required],
      // imageId: ['', Validators.required],
      // levelId: ['', Validators.required],

    });
  }


  // onSubmit(): void {
  // }

}
