import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomePageComponent } from './home-page/home-page.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {MockUpRoutingModules} from './mock-up-routing.modules';
import { ProjectComponent } from './project/project.component';
import { AddOrUpdateProjectComponent } from './add-or-update-project/add-or-update-project.component';
import {BsDatepickerModule, BsDropdownModule, PaginationModule, TabsModule} from 'ngx-bootstrap';
import { TimesheetComponent } from './timesheet/timesheet.component';
import { TeamComponent } from './team/team.component';
import { PeojectDetailComponent } from './project-detail/peoject-detail.component';
import { NewTeamComponent } from './new-team/new-team.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { UserComponent } from './user/user.component';
import { ProjectDetailComponent } from './project/project-detail/project-detail.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

@NgModule({
  declarations: [
    HomePageComponent,
    ProjectComponent,
    AddOrUpdateProjectComponent,
    TimesheetComponent,
    TeamComponent,
    PeojectDetailComponent,
    NewTeamComponent,
    UserComponent,
    ProjectDetailComponent,
  ],
  imports: [
    CommonModule,
    MockUpRoutingModules,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    NgbModule,
    BsDatepickerModule.forRoot(),
    PaginationModule.forRoot(),
    // BrowserAnimationsModule,
    BsDropdownModule.forRoot()
  ]
})
export class MockUpModule { }
