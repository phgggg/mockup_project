import {UserRequestModel} from '../user-request.model';

export interface TimesheetModel {
  timesheetId: number;
  task: string;
  createdDate: Date;
  result: string;
  note: string;
  status: number;
  projectId: number;
  usersEntity: UserRequestModel;
}
