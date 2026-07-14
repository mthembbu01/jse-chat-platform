# JSE Chat UI - Angular Frontend

A modern, real-time chat application built with Angular 20, featuring WebSocket support for instant messaging and a sleek dark-mode interface inspired by Messenger.

## Features

- **Real-time Messaging**: WebSocket-powered instant messaging with fallback to REST API
- **Dark Mode UI**: Modern dark-themed interface matching Messenger's design language
- **User Authentication**: Simple login system with session management
- **Global Chat Room**: Connect and chat with other users in a shared workspace
- **Message History**: View all messages in the chat room with usernames and timestamps
- **Responsive Design**: Mobile-friendly interface using Bootstrap 5
- **Auto-reconnection**: Automatic WebSocket reconnection with fallback support
- **Message Timestamps**: All messages display precise local time formatting

## Prerequisites

- **Node.js**: v18 or higher
- **npm**: v9 or higher
- **Angular CLI**: v20.2.2
- **Backend Server**: Running on `http://localhost:8090/chat`

## Installation

### 1. Install Dependencies

```bash
cd jse-chat-ui
npm install
```

### 2. Configure API Base URL

Edit `src/environments/environment.ts`:

```typescript
export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:8090/chat/api/v1'
};
```
## Running the Application

### Development Server

```bash
npm start
```

or

```bash
ng serve
```

Navigate to `http://localhost:4200/`. The app will automatically reload when you modify source files.

## Project Structure

```
jse-chat-ui/
├── src/
│   ├── app/
│   │   ├── app.ts                    # Root component
│   │   ├── app.routes.ts             # Route definitions
│   │   ├── app.config.ts             # App configuration
│   │   ├── login/                    # Login component
│   │   │   ├── login.ts              # Component logic
│   │   │   ├── login.html            # Login template
│   │   │   └── login.scss            # Login styles
│   │   ├── chatroom-dashboard/       # Chat dashboard component
│   │   │   ├── chatroom-dashboard.ts # Component logic (message handling)
│   │   │   ├── chatroom-dashboard.html # Chat template
│   │   │   └── chatroom-dashboard.scss # Chat styles
│   │   ├── service/
│   │   │   ├── chat.service.ts       # WebSocket & REST API integration
│   │   │   └── user.service.ts       # User session management
│   │   ├── model/
│   │   │   ├── user.model.ts         # User interface
│   │   │   └── message.model.ts      # Chat message interface
│   │   └── model/
│   ├── main.ts                       # Application entry point
│   ├── styles.scss                   # Global styles
│   └── environments/                 # Environment configurations
├── public/
│   └── favicon.ico
├── angular.json                      # Angular CLI configuration
├── package.json                      # Dependencies
├── tsconfig.json                     # TypeScript configuration
└── README.md
```

## Key Services

### ChatService (`src/app/service/chat.service.ts`)

Manages WebSocket connection and REST fallback:

- **`connect(callback)`**: Establishes WebSocket connection and listens for messages
- **`send(message)`**: Sends message via WebSocket to `/app/send` endpoint
- **`sendViaRest(message)`**: REST fallback endpoint for sending messages
- **`getConnectionStatus()`**: Observable stream of WebSocket connection status

### UserService (`src/app/service/user.service.ts`)

Handles user session and authentication:

- **`login(username)`**: Authenticate user by username
- **`setCurrentUser(user)`**: Store authenticated user in session
- **`getCurrentUser()`**: Retrieve current user from session

Tests run in watch mode by default. Use `--watch=false` for single run:

Run tests with a specific browser:
`

Coverage reports are generated in `coverage/` directory.

## Component Deep Dive

### Login Component

- Accepts username input
- Calls `UserService.login()` to authenticate
- Stores user session and navigates to chat dashboard
- Redirects to login if session is lost

### Chatroom Dashboard Component

**Features**:
- Real-time message display with sender information
- User badge showing current logged-in user
- Message input with send button
- **Auto-clearing input**: Text clears immediately upon submit
- Duplicate message prevention using username + text + timestamp matching
- WebSocket status indicator

**Message Flow**:
1. User types and clicks send
2. Input clears immediately (optimistic UI)
3. Message sent via WebSocket (or REST if WS down)
4. Server broadcasts message to all subscribers
5. UI receives message and adds to display

## WebSocket Integration

### Connection Details

- **Endpoint**: `http://localhost:8090/chat/ws-chat`
- **Library**: STOMP over SockJS
- **Topics**: 
  - `/topic/chat/messages` - Chat messages
- **Destination**: `/app/send` - Send message endpoint

### Fallback Mechanism

If WebSocket connection fails:
1. Component detects disconnection via `wsConnected` flag
2. Falls back to REST API (`POST /api/v1/send`)
3. Automatically reconnects to WebSocket with 5-second retry delay

## Dependencies

### Core
- `@angular/core@^20.2.0`
- `@angular/common@^20.2.0`
- `@angular/forms@^20.2.0`
- `@angular/platform-browser@^20.2.0`

### WebSocket & Real-time
- `@stomp/stompjs@^7.3.0`
- `sockjs-client@^1.6.1`

### UI Framework
- `bootstrap@^5.3.8`
- `bootstrap-icons@^1.13.1`
- `font-awesome@^4.7.0`

### Utilities
- `rxjs@~7.8.0`
- `tslib@^2.3.0`

## Configuration

### CORS Settings

The backend should be configured with CORS enabled to allow requests from `http://localhost:4200`.

### API Base URL

Update the API base URL in `src/environments/environment.ts`:

```typescript
export const environment = {
  apiBaseUrl: 'http://localhost:8090/chat/api/v1',
};
```

### WebSocket URL

The WebSocket URL is hardcoded in `chat.service.ts`. Update if needed:

```typescript
webSocketFactory: () => {
  return new (window as any).SockJS('http://localhost:8090/chat/ws-chat');
}
```
