import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { User } from '../model/user.model';
import {ChatMessage} from '../model/message.model';
import {ChatService} from '../service/chat.service';

@Component({
  selector: 'app-chat-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chatroom-dashboard.html'
})
export class ChatroomDashboard implements OnInit {
  activeUser: User | null = null;
  newMessageText: string = '';

  // Dummy data mock structure arrays
  messages: ChatMessage[] = [
    new ChatMessage('System', 'Welcome to the Global Workspace Main Lobby channel.', new Date(2026, 7, 10, 9, 0, 0) ),
    new ChatMessage('Sarah_99', 'Hey guys! This dark-mode workspace is working flawlessly.', new Date(2026, 7, 10, 9, 14, 0) ),
    new ChatMessage('JohnDoe', 'Agreed, the styling looks exactly like Messenger!', new Date(2026, 7, 10, 9, 0, 15) ),
  ];

  constructor(
    private chatService: ChatService,
    private authService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Pull the strongly-typed active user session information
    this.activeUser = this.authService.getCurrentUser();

    // Fallback block safeguard step: If user refreshes or session wipes, redirect safely
    if (!this.activeUser) {
      console.warn('No active login state located. Redirecting back to authentication.');
      this.router.navigate(['/']);
    }
  }

  sendMessage(): void {

    if (this.newMessageText.trim() && this.activeUser) {
      const chatMessage = new ChatMessage(this.activeUser.username, this.newMessageText, new Date());
      // Add the message instance directly onto your conversation board stack array
      this.messages.push(chatMessage);

        console.log("Publishing: ", JSON.stringify(chatMessage))

      this.chatService.sendToApi(chatMessage).subscribe({
        next: (message: ChatMessage) => {
          this.messages.push(chatMessage);
          console.log('Successfully sent message:', message);
        },
        error: (err) => {
          console.error('API Fetch failed:', err);
        }
      });

      // Erase text string inside your message box component wrapper
      this.newMessageText = '';
    }
  }

  // onLogout(): void {
  //   this.authService.logout();
  //   this.router.navigate(['/']);
  // }
}
