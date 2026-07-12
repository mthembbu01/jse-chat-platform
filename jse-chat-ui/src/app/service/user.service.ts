// src/app/services/user.service.ts
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../model/user.model';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  // Base endpoint path pointing to your Spring Boot microservice port
  private privateApiUrl = environment.apiBaseUrl ;
  private currentUser: User | null = null;
  private errorMsg: string | null = '';

  constructor(private http: HttpClient) {}

  // Fetch the user model from the microservice GET endpoint
  login(username: string): Observable<User> {
    // Replaces {username} in path with the user input dynamically
    return this.http.get<User>(`${this.privateApiUrl}/${(username.trim())}`);
  }

  // Set local state once successfully verified from backend
  setCurrentUser(user: User): void {
    this.currentUser = user;
  }

  getCurrentUser(): User | null {
    return this.currentUser;
  }

  getErrorMsg(): string | null {
    return this.errorMsg;
  }
}
