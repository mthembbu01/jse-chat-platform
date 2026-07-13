import {ChangeDetectorRef, Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {UserService} from '../service/user.service';
import {User} from '../model/user.model';
import {ChatMessage} from '../model/message.model';
import {ChatService} from '../service/chat.service';
import {WebsocketService} from '../service/websocket.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-chat-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, ],
  templateUrl: './chatroom-dashboard.html'
})
export class ChatroomDashboard implements OnInit {
  activeUser: User | null = null;
  newMessageText: string = '';
  private streamSubscription!: Subscription;
  private cdr = inject(ChangeDetectorRef);

  // Dummy data mock structure arrays
  messages: ChatMessage[] = [];

  constructor(
    private chatService: ChatService,
    private authService: UserService,
    private wsService: WebsocketService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    // Pull the strongly-typed active user session information
    this.activeUser = this.authService.getCurrentUser();

    // Fallback block safeguard step: If user refreshes or session wipes, redirect safely
    if (!this.activeUser) {
      console.warn('No active login state located. Redirecting back to authentication.');
      this.router.navigate(['/']);
    }

    function parseDate(timestamp: string) {
      return new Date(timestamp.replace(' ', 'T'));
    }

    this.chatService.getDefaultChat().subscribe({
      next: (chat: any[]) => {
        this.messages = chat.map(rawMessage => {
          return new ChatMessage(rawMessage.username, rawMessage.text, parseDate(rawMessage.timestamp));
        });
        console.log('Successfully updated chat');
      },
      error: (err) => {
        console.error('Chat dashboard -> API Fetch failed:', err);
      }
    });

    setTimeout(() => {
      this.cdr.detectChanges();
    }, 100);


    // Read broadcast payloads automatically
    this.streamSubscription = this.wsService.getMessagesStream().subscribe({
      next: (message: { username: string, text: string, timestamp: string, }) => {
        this.addMessageToChat(message);
        setTimeout(() => {
          this.cdr.detectChanges();
        }, 100);
      },
      error: (err) => console.error('Subscription read breakdown:', err)
    });
  }

  private addMessageToChat(message: { username: string; text: string; timestamp: string }) {
    console.log('Appending message from consumer:', message);
    let newMessage = new ChatMessage(message.username, message.text, new Date(message.timestamp.replace(' ', 'T')));
    this.messages = [...this.messages, newMessage];
  }

  sendMessage(): void {

    if (this.newMessageText.trim() && this.activeUser) {
      const chatMessage = new ChatMessage(this.activeUser.username, this.newMessageText, new Date());
      // Add the message instance directly onto your conversation board stack array
      console.log("Publishing: ", JSON.stringify(chatMessage))
      this.newMessageText = '';
      this.chatService.sendToApi(chatMessage).subscribe({
        next: (message: ChatMessage) => {
          console.log('Successfully sent message:', message);
        },
        error: (err) => {
          console.error('API Fetch failed:', err);
        }
      });
    }
  }

  // onLogout(): void {
  //   this.authService.logout();
  //   this.router.navigate(['/']);
  // }
}
