export class BaseModelModel {
  pageSize: number;
  page: number;

  constructor(pageSize: number, page: number) {
    this.pageSize = pageSize;
    this.page = page;
  }
}
