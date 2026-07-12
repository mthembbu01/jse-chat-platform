import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { User } from '../model/user.model';

interface ChatMessage {
  sender: string;
  text: string;
  time: string;
}

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
    { sender: 'System', text: 'Welcome to the Global Workspace Main Lobby channel.', time: '09:00 AM' },
    { sender: 'Sarah_99', text: 'Hey guys! This dark-mode workspace is working flawlessly.', time: '09:14 AM' },
    { sender: 'JohnDoe', text: 'Agreed, the styling looks exactly like Messenger!', time: '09:15 AM' }
  ];

  constructor(
    private authService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Pull the strongly-typed active user session information
    this.activeUser = this.authService.getCurrentUser();

    // Fallback block safeguard step: If user refreshes or session wipes, redirect safely
    if (!this.activeUser) {
      console.warn('No active login state located. Redirecting back to authentication.');
      this.router.navigate(['/login']);
    }
  }

  sendMessage(): void {
    if (this.newMessageText.trim() && this.activeUser) {
      const now = new Date();
      const timeFormatted = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

      // Add the message instance directly onto your conversation board stack array
      this.messages.push({
        sender: this.activeUser.username,
        text: this.newMessageText.trim(),
        time: timeFormatted
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
