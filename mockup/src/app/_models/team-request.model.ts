import {UserRequestModel} from './user-request.model';
import {ProjectModel} from './project.model';

export class TeamRequestModel {
  teamId: number;
  teamName: string;
  quantity: number;
  createdBy: string;
  createdDate: Date;
  projectId: string;
  modifiedDate: string;
  modifiedBy: string;
  projectEntity: ProjectModel;
  usersEntities: Array<UserRequestModel> = [];

  constructor(teamName: string, projectEntity: ProjectModel, usersEntities: Array<UserRequestModel>) {
    this.teamName = teamName;
    this.projectEntity = projectEntity;
    this.usersEntities = usersEntities;
  }
}
