import {Injectable, NgZone} from '@angular/core';
import {Client, Message} from '@stomp/stompjs';
import {Observable, Subject} from 'rxjs';
import {ChatMessage} from '../model/message.model';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private stompClient!: Client;
  private messageSubject = new Subject<ChatMessage>();

  constructor(private zone: NgZone) {
    this.initializeWebSocketConnection();
  }

  private initializeWebSocketConnection() {
    this.stompClient = new Client({
      // Use raw WebSocket URL instead of HTTP wrapper
      brokerURL: `ws://${environment.hostUrl}/ws-chat`,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: (str) => {
        console.log('STOMP Debug:', str); // Watch this closely in F12 console
      }
    });

    this.stompClient.onConnect = (frame) => {
      console.log('✅ Connected to Spring Boot WebSocket Broker!');

      // Subscribe to your target queue topic channel
      this.stompClient.subscribe('/topic/messages', (message: Message) => {
        if (message.body) {
          // Push payload to our observable inside Angular's zone to trigger UI refresh
          this.zone.run(() => {
            this.messageSubject.next(JSON.parse(message.body));
          });
        }
      });
    };

    this.stompClient.onWebSocketError = (error) => {
      console.error('❌ Connection error on raw WebSocket level:', error);
    };

    this.stompClient.onStompError = (frame) => {
      console.error('❌ Broker reported error:', frame.headers['message']);
    };

    this.stompClient.activate();
  }

  // Expose stream for components to read incoming string packets
  getMessagesStream(): Observable<ChatMessage> {
    return this.messageSubject.asObservable();
  }
}
