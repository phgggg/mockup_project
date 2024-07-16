import {RolesModel} from './roles.model';

export class UserRequestModel {
  userId: number;
  userName: string;
  passWord: string;
  fullName: string;
  email: string;
  phone: string;
  skypeName: string;
  facebookUrl: string;
  address: string;
  education: string;
  university: string;
  faculty: string;
  graduationDate: Date;
  active: string;
  reason: string;
  imageId: number;
  levelId: number;
  roles: Array<RolesModel>;

  constructor(userId: number, fullName: string, email: string, skype: string) {
    this.userId = userId;
    this.fullName = fullName;
    this.email = email;
    this.skypeName = skype;
  }


}
