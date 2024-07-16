import {Component, ElementRef, HostListener, OnInit} from '@angular/core';
import {UserService} from '../../../_services/mock-up-service/user.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ProjectModel} from '../../../_models/project.model';
import {UserRequestModel} from '../../../_models/user-request.model';
import {AuthenticationService} from '../../../_services/authentication.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  title: 'Register';
  user: UserRequestModel;

  registerForm: FormGroup;
  submitted = false;
  success = false;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder
  ) {

    this.loadUserForm();
  }

  ngOnInit() {
  }

  loadUserForm() {
    const numericRegex = '^[a-zA-Z0-9]+$';
    this.registerForm = this.formBuilder.group({
      fullName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      userName: ['', Validators.required],
      passWord: ['', [Validators.required, Validators.minLength(6)]],
      address: ['', Validators.required],
      phone: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(12), Validators.pattern(numericRegex)]],
      facebookUrl: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    }, {
      validator: MustMatch('passWord', 'confirmPassword')
    });

  }


  obSubmit() {
    this.submitted = true;
    if (this.registerForm.invalid) {
      console.log(`eeeeeeeeeeeeeee${this.registerForm.value}`);
      alert('----------------------errr!! :-)\n\n' + JSON.stringify(this.registerForm.value, null, 9));
      return;
    }
    this.success = true;
    alert('SUCCESS!! :-)\n\n' + JSON.stringify(this.registerForm.value, null, 9));
    this.register();
    alert('Please check your email !!')
    this.router.navigate(['/']);
  }

  get f() {
    return this.registerForm.controls;
  }

  register(): void {
    if (this.registerForm.valid) {
      this.userService.addData(this.registerForm.value).subscribe(data => {
        this.user = data;
        console.log(`eeeeeeeeeeeeee${this.user['errorCode']}`);
        if (this.user['errorCode'] == -4 || this.user['errorCode'] == -3) {
          return alert(this.user['description']);
        }
      });
    }
  }

  // @HostListener('input', ['$event']) onInputChange(event) {
  //   const initalValue = this.element.nativeElement.value;
  //
  //   this.element.nativeElement.value = initalValue.replace(/[^0-9]*/g, '');
  //   if ( initalValue !== this.element.nativeElement.value) {
  //     event.stopPropagation();
  //   }
  // }

}

export function MustMatch(controlName: string, matchingControlName: string) {
  return (formGroup: FormGroup) => {
    const control = formGroup.controls[controlName];
    const matchingControl = formGroup.controls[matchingControlName];

    if (matchingControl.errors && !matchingControl.errors.mustMatch) {
      // return if another validator has already found an error on the matchingControl
      return;
    }

    // set error on matchingControl if validation fails
    if (control.value !== matchingControl.value) {
      matchingControl.setErrors({mustMatch: true});
    } else {
      matchingControl.setErrors(null);
    }
  };

}
