export class ChatMessage {
  username: string;
  text: string;
  time: string;

  constructor(username: string, text: string, time: string) {
    this.username = username;
    this.text = text;
    this.time = time;
  }
}
