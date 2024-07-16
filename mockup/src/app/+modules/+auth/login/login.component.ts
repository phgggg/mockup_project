import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Errors} from '../../../_models/errors';
import {CONSUME_API} from '../../../_services/consume-apis';
import {AuthRequestModel} from '../../../_models/auth/auth-request.model';
import {ToastrService} from 'ngx-toastr';
import {AuthenticationService} from '../../../_services/authentication.service';
import {ActivatedRoute, Router} from '@angular/router';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  isSubmitting = false;
  errors: Errors = {errors: {}};
  authForm: FormGroup;
  submitted = false;
  autRequest: AuthRequestModel = new AuthRequestModel();
  returnUrl: string;

  constructor(private formBuilder: FormBuilder, private authService: AuthenticationService,
              private toastr: ToastrService, private route: ActivatedRoute, private router: Router,
              private translateService: TranslateService) {
  }

  ngOnInit(): void {
    // use FormBuilder to create a form group
    this.authForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', [Validators.required]]
    });

    // reset login status
    this.authService.logout();

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/office/list-timesheet';
  }

  get f() {
    return this.authForm.controls;
  }

  submitForm() {
    this.submitted = true;

    if (this.authForm.invalid) {
      return;
    }

    let url = CONSUME_API.AUTH.logIn;

    this.autRequest.username = this.authForm.value.username;
    this.autRequest.password = this.authForm.value.password;

    this.authService.login(this.autRequest).subscribe(res => {
      if (res.errorCode == '00') {
        this.router.navigate([this.returnUrl]);
      } else if (res.errorCode == '401') {
        this.toastr.error(this.translateService.instant('login.error.unauthorized'), 'ERROR');
      } else {
        this.toastr.error(this.translateService.instant('login.error.err'), 'ERROR');
      }
    }, err => {
      this.toastr.error(this.translateService.instant('login.error.err'), 'ERROR');
    });
  }

}

