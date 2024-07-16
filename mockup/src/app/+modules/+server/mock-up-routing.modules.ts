import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {HomePageComponent} from './home-page/home-page.component';
import {ProjectComponent} from './project/project.component';
import {AddOrUpdateProjectComponent} from './add-or-update-project/add-or-update-project.component';
import {TimesheetComponent} from './timesheet/timesheet.component';
import {TeamComponent} from './team/team.component';
import {NewTeamComponent} from './new-team/new-team.component';
import {UserComponent} from './user/user.component';
import {ProjectDetailComponent} from './project/project-detail/project-detail.component';

const routes: Routes = [
  {
    path: 'home-page',
    component: HomePageComponent,
  },
  {
    path: 'list-project',
    component: ProjectComponent
  },
  {
    path: 'project/:id',
    component: AddOrUpdateProjectComponent
  },
  {
    path: 'project-detail/:id',
    component: ProjectDetailComponent
  },
  {
    path: 'list-timesheet',
    component: TimesheetComponent
  },
  {
    path: 'list-team',
    component: TeamComponent
  },
  {
    path: 'new-team',
    component: NewTeamComponent
  },
  {
    path: 'users-manager',
    component: UserComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MockUpRoutingModules {
}
