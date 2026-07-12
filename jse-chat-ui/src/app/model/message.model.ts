export class ChatMessage {
  sender: string;
  text: string;
  time: string;

  constructor(sender: string, text: string, time: string) {
    this.sender = sender;
    this.text = text;
    this.time = time;
  }
}
