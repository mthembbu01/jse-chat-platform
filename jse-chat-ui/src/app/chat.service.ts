import { Injectable } from '@angular/core';
import { Client } from '@stomp/stompjs';
// @ts-ignore
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private client!: Client;

  connect(callback: (msg:any)=>void) {

    this.client = new Client({

      webSocketFactory: () =>
        new SockJS('http://localhost:8090/jse-chat')

    });

    this.client.onConnect = () => {

      this.client.subscribe(
        '/default-chat-room',

        message => callback(
          JSON.parse(message.body))
      );

    };

    this.client.activate();

  }

}
