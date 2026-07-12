import { Routes } from '@angular/router';
import {Login} from './login/login';
import {ChatroomDashboard} from './chatroom-dashboard/chatroom-dashboard';

export const routes: Routes = [
  {path: '', component: Login},
  {path: 'dashboard', component: ChatroomDashboard}
];
