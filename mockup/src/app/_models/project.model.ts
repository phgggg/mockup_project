import {TeamRequestModel} from './team-request.model';

export class ProjectModel {
  projectId?: number;
  projectName?: string;
  description?: string;
  createdDate?: Date;
  deadline?: Date;
  createdBy?: string;
  // teams: Array<TeamRequestModel> = [];

  constructor(projectId: number, projectName: string, description: string, createdDate: Date, deadline: Date,
              createdBy: string) {
    this.projectId = projectId;
    this.projectName = projectName;
    this.description = description;
    this.createdDate = createdDate;
    this.deadline = deadline;
    this.createdBy = createdBy;
    // this.teams = teams;
  }

  // constructor(projectId: number, projectName: string, description: string, createdDate: Date, deadline: Date,
  //             createdBy: string, teams: Array<TeamRequestModel> ) {
  //   this.projectId = projectId;
  //   this.projectName = projectName;
  //   this.description = description;
  //   this.createdDate = createdDate;
  //   this.deadline = deadline;
  //   this.createdBy = createdBy;
  //   this.teams = teams;
  //
  // }

}
