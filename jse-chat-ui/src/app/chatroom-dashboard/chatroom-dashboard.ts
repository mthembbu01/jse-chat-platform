import { Component } from '@angular/core';
import {ChatService} from '../chat.service';

@Component({
  selector: 'app-chatroom-dashboard',
  imports: [],
  templateUrl: './chatroom-dashboard.html',
  styleUrl: './chatroom-dashboard.scss',
})
export class ChatroomDashboard {

  messages:any[]=[];
  constructor(private chat:ChatService){}

  ngOnInit(){

    this.chat.connect(message=>{

      this.messages.push(message);

    });

  }
}
