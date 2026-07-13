import {Injectable} from '@angular/core';
import {Client, IMessage} from '@stomp/stompjs';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {ChatMessage} from '../model/message.model';
import {User} from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private client!: Client;
  private privateApiUrl = `${environment.apiBaseUrl}/api/v1/chat` ;

  constructor(private http: HttpClient) {}

  connect(callback: (msg: any) => void) {
    this.client = new Client({
      brokerURL: `ws://${environment.hostUrl}/jse-chat`,
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
  sendToApi(message: ChatMessage): Observable<ChatMessage> {
    return this.http.post<ChatMessage>(`${this.privateApiUrl}/send`, message)
  }

  getDefaultChat() {
    return this.http.get<ChatMessage[]>(`${this.privateApiUrl}/default`)
  }
}
