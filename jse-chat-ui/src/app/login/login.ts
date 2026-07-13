import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {User} from '../model/user.model';
import {UserService} from '../service/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html'
})
export class Login {
  user: User = new User('');
  errorMessage: string | null = null;
  isLoading: boolean = false;

  router = inject(Router);

  constructor(private userService: UserService) {
  }

  onSubmit() {
    if (!this.user.username.trim()) return;

    this.isLoading = true;
    this.errorMessage = null;

    // Trigger the microservice API call
    this.userService.login(this.user.username).subscribe({
      next: (resolvedUser: User) => {
        this.isLoading = false;
        // Save the strongly typed backend response to app state
        this.userService.setCurrentUser(resolvedUser);
        console.log('Successfully fetched user:', resolvedUser);
        this.router.navigate(['dashboard']);

        // Ready for routing logic next, e.g., this.router.navigate(['/chat']);
      },
      error: (err) => {
        this.isLoading = false;
        if (err.status === 404) {
          this.errorMessage = 'Username not found in chat directory.';
        } else {
          this.errorMessage = 'Could not connect to authentication microservice.';
        }
        console.error('API Fetch failed:', err);
      }
    });
  }
}
