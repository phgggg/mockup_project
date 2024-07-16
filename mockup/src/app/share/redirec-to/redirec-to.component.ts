import {Component, OnInit} from '@angular/core';
import {UserService} from '../../_services/mock-up-service/user.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-redirec-to',
  templateUrl: './redirec-to.component.html',
  styleUrls: ['./redirec-to.component.css']
})
export class RedirecToComponent implements OnInit {

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    const userName = this.route.snapshot.paramMap.get('userName');
    this.userService.updateActiveUser(userName).subscribe(res => {
      console.log(res);
    });
    this.router.navigate(['/']);
  }

  ngOnInit() {

  }

}
