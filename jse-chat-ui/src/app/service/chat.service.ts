import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';

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

      console.log('Connected to WebSocket');
      //-- subscribe ONCE after connection
      this.client.subscribe(
        '/topic/default-chat-room',
        (message: IMessage) => {
        callback(JSON.parse(message.body));
      });

    };

    this.client.activate();
  }

  /**
   * Sends a message through the WebSocket connection.
   * @param message The message to send.
   */
  send(message: any) {

    this.client.publish({

      destination: "/app/chat.send",

      body: JSON.stringify(message)

    });

  }
}
