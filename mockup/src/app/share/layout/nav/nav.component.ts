import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../../_services/authentication.service';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

  role = '';

  constructor(  private authenticationService: AuthenticationService) {
    // this.role = authenticationService.currentUserValue.roles;
  }
  ngOnInit() {

  }

  get token(): string | null {
    const user = JSON.parse(localStorage.getItem('currentUser'));
    console.log(`qqqqqqqqqqqqqqqqqqqqqqqq ${user}`);
    return user ? user.token : null;
    return null;
  }

  setRole() {
    // let jwt = 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJodW5nbnYiLCJjcmVhdGVkIjoxNTcyMTA0NjIxMTk0LCJyb2xlcyI6Ik1BTkFHRVIiLCJleHAiOjE1NzIxMDgyMjF9.krqI-eSLiOc6kBHkb8o71Ifu9OLL9FXIrNOcjkYZCTIXlNoOjEeKJ8sWPjp4UvYA3ngk7aqU_lImmxHFdxXfkg';
    const jwtData = this.token.split('.')[1];
    const decodedJwtJsonData = window.atob(jwtData);
    const decodedJwtData = JSON.parse(decodedJwtJsonData);
    this.role = decodedJwtData.roles;

    console.log('jwtData: ' + jwtData);
    console.log('decodedJwtJsonData: ' + decodedJwtJsonData);
    console.log('decodedJwtData: ' + decodedJwtData);
    console.log('Is Roles: ' + this.role);
  }

}
