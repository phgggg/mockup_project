export class EmailModel {
  toEmail: string;
  subject: string;
  message: string;

  constructor(toEmail: string, subject: string, message: string) {
    this.toEmail = toEmail;
    this.subject = subject;
    this.message = message;
  }

}
