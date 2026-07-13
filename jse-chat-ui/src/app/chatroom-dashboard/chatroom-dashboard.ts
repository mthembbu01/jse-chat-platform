import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { User } from '../model/user.model';
import {ChatService} from '../service/chat.service';
import { ChatMessage } from '../model/message.model';

// interface ChatMessage {
//   sender: string;
//   text: string;
//   time: string;
// }

@Component({
  selector: 'app-chat-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chatroom-dashboard.html'
})
export class ChatroomDashboard implements OnInit {
  activeUser: User | null = null;
  newMessageText: string = '';
  wsConnected: boolean = false;

  // Dummy data mock structure arrays
  messages: ChatMessage[] = [
    { username: 'System', text: 'Welcome to the Global Workspace Main Lobby channel.', time: '09:00 AM' },
    { username: 'Sarah_99', text: 'Hey guys! This dark-mode workspace is working flawlessly.', time: '09:14 AM' },
    { username: 'JohnDoe', text: 'Agreed, the styling looks exactly like Messenger!', time: '09:15 AM' }
  ];

  constructor(
    private userService: UserService,
    private chatService: ChatService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Pull the strongly typed active user session information
    this.activeUser = this.userService.getCurrentUser();

    // Fallback block safeguard step: If user refreshes or session wipes, redirect safely
    if (!this.activeUser) {
      console.warn('No active login state located. Redirecting back to authentication.');
      this.router.navigate(['/login']);
      return;
    }

    // Monitor WebSocket connection status
    this.chatService.getConnectionStatus().subscribe(status => {
      this.wsConnected = status;
      console.log('WebSocket connection status:', status);
    });

    // Connecting to the chat service to listen for incoming messages
    this.chatService.connect((message) => {
      console.log("Received message:", message);
      // Avoid duplicates: don't push if an identical message already exists
      const duplicate = this.messages.some(m => m.username === message.username && m.text === message.text && m.time === message.time);
      if (!duplicate) {
        this.messages.push(message);
      } else {
        console.debug('Duplicate message ignored');
      }
    });
  }

  sendMessage(): void {
    const trimmedText = this.newMessageText.trim();

    if (!trimmedText || !this.activeUser) {
      return;
    }

    // Reset the input immediately after submit so the text box clears right away.
    this.newMessageText = '';

    if (!this.wsConnected) {
      console.warn('WebSocket not connected. Using fallback REST API.');
      this.sendViaRest(trimmedText);
      return;
    }

    const now = new Date();
    const timeFormatted = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

    const message: ChatMessage = {
      username: this.activeUser.username,
      text: trimmedText,
      time: timeFormatted
    };

    // Send via WebSocket. Do NOT push locally here - the server will broadcast
    // the message back to all subscribers (including this client) so pushing
    // locally results in duplicates. We rely on the server broadcast to add
    // the message into `this.messages` when the subscription callback fires.
    this.chatService.send(message);
  }

  /**
   * Fallback method to send via REST API
   */
  private sendViaRest(messageText: string): void {
    if (!this.activeUser) return;

    const now = new Date();
    const timeFormatted = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

    this.chatService.sendViaRest({
      username: this.activeUser.username,
      text: messageText,
      time: timeFormatted
    }).subscribe({
      next: (response) => {
        console.log('Message sent via REST API:', response);
        // Avoid duplicates: don't push if an identical message already exists
        const duplicate = this.messages.some(m => m.username === response.username && m.text === response.text && m.time === response.time);
        if (!duplicate) {
          this.messages.push(response);
        } else {
          console.debug('Duplicate message ignored');
        }
      },
      error: (err) => {
        console.error('Error sending message via REST API:', err);
      }
    });
  }

  // onLogout(): void {
  //   this.userService.logout();
  //   this.router.navigate(['/']);
  // }
}
