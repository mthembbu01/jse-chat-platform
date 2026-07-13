import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import {ChatMessage} from '../model/message.model';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private client!: Client;
  private connectedSubject = new Subject<boolean>();

  private baseUrl: string =  environment.apiBaseUrl;

  constructor(private http: HttpClient){}

  connect(callback: (msg: any) => void) {
    // Only connect if not already connected
    if (this.client && this.client.connected) {
      console.warn('Already connected to WebSocket');
      return;
    }

    // @ts-ignore
    this.client = new Client({
      // Use SockJS for WebSocket with fallback to HTTP polling/streaming
      webSocketFactory: () => {
        // SockJS is loaded via CDN in index.html and available on window
        return new (window as any).SockJS('http://localhost:8090/chat/ws-chat');
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: (msg: string) => {
        console.log('[STOMP Debug]', msg);
      },
      onConnect: () => {
        console.log('✓ Connected to WebSocket');
        this.connectedSubject.next(true);

        // Subscribe to the messages topic
        this.client.subscribe(
          '/topic/chat/messages',
          (message: IMessage) => {
            console.log('Received message:', message.body);
            try {
              callback(JSON.parse(message.body));
            } catch (e) {
              console.error('Error parsing message:', e);
              callback(message.body);
            }
          },
          {
            'id': 'sub-messages'
          }
        );

        // Subscribe to notifications
        this.client.subscribe(
          '/topic/chat/notifications',
          (notification: IMessage) => {
            console.log('Received notification:', notification.body);
          }
        );
      },
      onStompError: (frame: any) => {
        console.error('✗ STOMP Error:', frame);
        console.error('  Error Header:', frame.headers['message']);
        console.error('  Error Body:', frame.body);
        this.connectedSubject.next(false);
      },
      onWebSocketError: (event: Event) => {
        console.error('✗ WebSocket Error:', event);
        this.connectedSubject.next(false);
      },
      onDisconnect: () => {
        console.warn('⚠ Disconnected from WebSocket');
        this.connectedSubject.next(false);
      }
    });

    this.client.activate();
  }

  /**
   * Check if connected
   */
  isConnected(): boolean {
    return this.client && this.client.connected;
  }

  /**
   * Get connection status as observable
   */
  getConnectionStatus(): Observable<boolean> {
    return this.connectedSubject.asObservable();
  }

  /**
   * Sends a message through the WebSocket connection
   * @param message The message to send
   */
  public send(message: ChatMessage): void {
    if (!this.isConnected()) {
      console.error('WebSocket not connected. Cannot send message.');
      return;
    }

    console.log('Sending message via WebSocket...:', message);

    this.client.publish({
      destination: "/app/send",
      body: JSON.stringify(message),
      headers: {
        'content-type': 'application/json'
      }
    });
  }

  /**
   * Sends a message through REST API (fallback)
   * @param message The message to send
   */
  public sendViaRest(message: ChatMessage): Observable<ChatMessage> {
    return this.http.post<ChatMessage>(`${this.baseUrl}/send`, message);
  }

  /**
   * Disconnect from WebSocket
   */
  public disconnect(): void {
    if (this.client && this.client.connected) {
      this.client.deactivate();
      console.log('Disconnected from WebSocket');
    }
  }
}
