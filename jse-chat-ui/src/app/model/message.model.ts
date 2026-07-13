export class ChatMessage {
  username: string;
  text: string;
  timestamp: string;
  time: string;

  constructor(username: string, text: string, time: Date) {
    this.username = username;
    this.text = text;
    this.timestamp = time.toLocaleDateString('en-CA') + ' ' + time.toTimeString().split(' ')[0];;
    this.time = time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  }
}
