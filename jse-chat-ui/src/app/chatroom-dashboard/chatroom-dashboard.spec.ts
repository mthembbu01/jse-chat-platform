import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChatroomDashboard } from './chatroom-dashboard';

describe('ChatroomDashboard', () => {
  let component: ChatroomDashboard;
  let fixture: ComponentFixture<ChatroomDashboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChatroomDashboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChatroomDashboard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
