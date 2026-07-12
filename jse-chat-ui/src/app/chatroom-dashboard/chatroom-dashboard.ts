import {Component, OnInit} from '@angular/core';
import {ChatService} from '../chat.service';

@Component({
  selector: 'app-chatroom-dashboard',
  imports: [],
  templateUrl: './chatroom-dashboard.html',
  styleUrl: './chatroom-dashboard.scss',
})
export class ChatroomDashboard implements OnInit {

  messages:any[]=[];
  constructor(private chat:ChatService){}

  ngOnInit(){

    this.chat.connect(message=>{
      this.messages.push(message);
    });

  }
}
