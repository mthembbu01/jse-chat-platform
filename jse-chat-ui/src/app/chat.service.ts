import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private client!: Client;

  connect(callback: (msg: any) => void) {
    this.client = new Client({
      brokerURL: 'ws://localhost:8090/jse-chat',
      reconnectDelay: 5000,
      debug: console.log
    });

    this.client.onConnect = () => {

      console.log('Connected');

      this.client.subscribe('/topic/default-chat-room', message => {
        callback(JSON.parse(message.body));
      });

    };

    this.client.activate();
  }
}
