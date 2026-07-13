import { of } from 'rxjs';

import { ChatroomDashboard } from './chatroom-dashboard';
import { UserService } from '../service/user.service';
import { ChatService } from '../service/chat.service';
import { Router } from '@angular/router';
import { User } from '../model/user.model';

describe('ChatroomDashboard', () => {
  let component: ChatroomDashboard;

  const mockUser: User = { username: 'alex' } as User;
  const userServiceSpy = jasmine.createSpyObj<UserService>('UserService', ['getCurrentUser']);
  const chatServiceSpy = jasmine.createSpyObj<ChatService>('ChatService', ['getConnectionStatus', 'connect', 'send', 'sendViaRest']);
  const routerSpy = jasmine.createSpyObj<Router>('Router', ['navigate']);

  beforeEach(async () => {
    userServiceSpy.getCurrentUser.and.returnValue(mockUser);
    chatServiceSpy.getConnectionStatus.and.returnValue(of(true));
    chatServiceSpy.connect.and.stub();
    chatServiceSpy.send.and.stub();
    chatServiceSpy.sendViaRest.and.returnValue(of({ username: 'alex', text: 'hello', time: '09:00 AM' }));

    component = new ChatroomDashboard(userServiceSpy, chatServiceSpy, routerSpy);
    component.activeUser = mockUser;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should clear the input after submitting via WebSocket', () => {
    component.wsConnected = true;
    component.newMessageText = '  hello world  ';

    component.sendMessage();

    expect(component.newMessageText).toBe('');
    expect(chatServiceSpy.send).toHaveBeenCalledWith({
      username: 'alex',
      text: 'hello world',
      time: jasmine.any(String)
    });
  });

  it('should clear the input after submitting via REST fallback', () => {
    component.wsConnected = false;
    component.newMessageText = '  fallback message  ';

    component.sendMessage();

    expect(component.newMessageText).toBe('');
    expect(chatServiceSpy.sendViaRest).toHaveBeenCalledWith({
      username: 'alex',
      text: 'fallback message',
      time: jasmine.any(String)
    });
  });
});
