# 🎵 Blindify

A Spring Boot application for hosting music blind tests (quiz games) using Spotify playlists.

## 📋 Overview

Blindify is a web-based blind test game that allows users to create and play musical quizzes using their Spotify playlists. The application manages game sessions with automated discovery and reveal phases, track playback control, and integrates with the Spotify Web API.

## ✨ Features

- **Spotify Integration**: Fetch tracks from Spotify playlists
- **Automated Blind Test**: Fully automated game flow with scheduled phases
- **Game Phases**: 
  - Discovery phase (players guess the track - configurable duration)
  - Reveal phase (answer is revealed with seek to specific timestamp)
  - Automatic progression to next track
- **Dual Control Modes**:
  - **Automated Mode**: Round orchestrator manages complete game flow
  - **Manual Mode**: Manual playback control (play, pause, next)
- **OAuth Authentication**: Secure Spotify user authentication
- **RESTful API**: Complete REST endpoints for game management

## 🏗️ Architecture

The project follows **Hexagonal Architecture** (Ports & Adapters):

```
├── domain/              # Business logic (core domain)
│   ├── blindtest/       # Blind test aggregates and use cases
│   ├── round/           # Round orchestration logic
│   ├── music/           # Music entities
│   └── port/            # Domain interfaces
├── application/         # Application services
├── infrastructure/      # External adapters
│   ├── spotify/         # Spotify API integration
│   ├── web/             # REST controllers
│   └── config/          # Spring configuration
```

### Key Components

- **Domain Layer**: Pure business logic, framework-agnostic
  - `Blindtest`: Main aggregate managing game state
  - `BlindtestRoundOrchestrator`: Orchestrates automated game rounds
  - `StartBlindtestUseCase`: Initializes new game sessions
  - `BlindtestPlaybackUseCase`: Manual playback control
- **Application Layer**: Use cases and orchestration
- **Infrastructure Layer**: 
  - Spotify adapters (authentication, catalog, playback)
  - Web controllers
  - Spring TaskScheduler integration
  - Configuration

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- Spotify Developer Account
- Active Spotify Premium account (for playback features)

### Spotify Application Setup

1. **Create a Spotify Application**
   
   Go to [Spotify Developer Dashboard](https://developer.spotify.com/dashboard) and create a new app:
   
   - **App Name**: Blindify (or your choice)
   - **App Description**: Music blind test game
   - **Redirect URI**: See step 3 below

2. **Get Your Credentials**
   
   After creating the app, note your:
   - `Client ID`
   - `Client Secret`

3. **Configure Redirect URI with ngrok**

   ⚠️ **Why ngrok is required**: Spotify's OAuth flow requires an HTTPS redirect URI, but local development runs on HTTP (`http://localhost:8080`). To work around this limitation while keeping your development environment local, we use **ngrok** to create an HTTPS tunnel to your local application.

   **Setup Steps:**
   
   a. Install [ngrok](https://ngrok.com/):
   ```bash
   # macOS
   brew install ngrok
   
   # Or download from https://ngrok.com/download
   ```
   
   b. Start your Spring Boot application on localhost:
   ```bash
   mvn spring-boot:run
   ```
   Your app is now running on `http://localhost:8080` (HTTP only).
   
   c. In a **separate terminal**, create an HTTPS tunnel with ngrok:
   ```bash
   ngrok http 8080
   ```
   
   You'll see output like:
   ```
   Forwarding  https://abc123.ngrok-free.app -> http://localhost:8080
   ```
   
   d. Copy the **HTTPS URL** (e.g., `https://abc123.ngrok-free.app`)
   
   e. In your Spotify app settings, add this redirect URI:
   ```
   https://abc123.ngrok-free.app/auth/spotify/callback
   ```
   
   🔄 **How it works**: 
   - Spotify redirects to `https://abc123.ngrok-free.app/auth/spotify/callback` (HTTPS ✅)
   - ngrok tunnels this request to your local `http://localhost:8080/auth/spotify/callback`
   - Your local Spring Boot app receives the OAuth callback
   - Everything continues to run locally!

   ⚠️ **Important**: 
   - Keep the ngrok terminal window open while developing
   - The free ngrok URL changes each time you restart ngrok
   - When the URL changes, update your Spotify app's redirect URI and environment variable

### Environment Variables

Set the following environment variables with your Spotify credentials:

```bash
# Your Spotify App credentials
export SPOTIFY_CLIENT_ID=your_client_id_here
export SPOTIFY_CLIENT_SECRET=your_client_secret_here

# Redirect URI - Use your ngrok HTTPS URL
export SPOTIFY_REDIRECT_URI=https://your-ngrok-url.ngrok-free.app/auth/spotify/callback
```

**Example with actual ngrok URL:**
```bash
export SPOTIFY_CLIENT_ID=a1b2c3d4e5f6
export SPOTIFY_CLIENT_SECRET=x1y2z3a4b5c6
export SPOTIFY_REDIRECT_URI=https://abc123.ngrok-free.app/auth/spotify/callback
```

💡 **Tip**: Create a shell script to set these variables for easier setup:
```bash
#!/bin/bash
# setup-env.sh
export SPOTIFY_CLIENT_ID=your_client_id_here
export SPOTIFY_CLIENT_SECRET=your_client_secret_here
export SPOTIFY_REDIRECT_URI=https://your-current-ngrok-url.ngrok-free.app/auth/spotify/callback
```

Then run: `source setup-env.sh` before starting your app.

### Application Configuration

The `application.properties` file is already configured to use these environment variables:

```properties
spring.profiles.active=spotify

spotify.auth-url=https://accounts.spotify.com/api/token
spotify.api-base-url=https://api.spotify.com/v1
spotify.client-id=${SPOTIFY_CLIENT_ID}
spotify.client-secret=${SPOTIFY_CLIENT_SECRET}
spotify.redirect-uri=${SPOTIFY_REDIRECT_URI}

blindtest.discovery-time-sec=20
blindtest.reveal-time-sec=10
```

⚠️ **Security Note**: The `application.properties` file is in `.gitignore` to protect your credentials.

### Build & Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Run tests
mvn test
```

## 📡 API Endpoints

### Authentication

```http
GET /auth/spotify/login
```
Redirects to Spotify authorization page

```http
GET /auth/spotify/callback?code={code}
```
OAuth callback endpoint

### Blind Test Management

```http
POST /blindtest/start
Content-Type: application/json

{
  "playlistId": "spotify_playlist_id",
  "tracks": 10
}
```
Start a new blind test session with **automated round orchestration**

```http
GET /blindtest/state
```
Get current blind test state (track index, total tracks, finished status)

```http
GET /blindtest/round-phase
```
Get current round phase (DISCOVERY, REVEAL, IDLE)

### Manual Playback Control *(Optional)*

These endpoints allow manual control when not using automated mode:

```http
POST /blindtest/playback/play
```
Play current track

```http
POST /blindtest/playback/pause
```
Pause playback

```http
POST /blindtest/playback/next
```
Move to next track

## 🎮 How to Play

### Prerequisites

Before starting, ensure you have:
1. ✅ Spotify Developer app created with credentials
2. ✅ ngrok installed and running
3. ✅ Environment variables set with your ngrok HTTPS URL
4. ✅ Spotify Premium account
5. ✅ An active Spotify device (desktop, mobile, or web player)

### Workflow

1. **Terminal 1 - Start Spring Boot**:
   ```bash
   # Set environment variables (or source your setup script)
   export SPOTIFY_CLIENT_ID=your_client_id
   export SPOTIFY_CLIENT_SECRET=your_client_secret
   export SPOTIFY_REDIRECT_URI=https://your-ngrok-url.ngrok-free.app/auth/spotify/callback
   
   # Start the application
   mvn spring-boot:run
   ```
   App running on `http://localhost:8080` ✅

2. **Terminal 2 - Start ngrok tunnel**:
   ```bash
   ngrok http 8080
   ```
   
   Copy the HTTPS URL from the output:
   ```
   Forwarding  https://abc123.ngrok-free.app -> http://localhost:8080
   ```
   
   ngrok tunnel active ✅

3. **First Time - Authenticate with Spotify**:
   
   Open in your browser (use your **ngrok HTTPS URL**):
   ```
   https://your-ngrok-url.ngrok-free.app/auth/spotify/login
   ```
   
   Follow the Spotify OAuth flow. After authorization, you'll be redirected back and see:
   ```
   Spotify authentication successful. You can close this page.
   ```
   
   Authentication complete ✅

4. **Start a Blind Test**:
   ```bash
   curl -X POST https://your-ngrok-url.ngrok-free.app/blindtest/start \
     -H "Content-Type: application/json" \
     -d '{"playlistId": "37i9dQZF1DXcBWIGoYBM5M", "tracks": 5}'
   ```
   
   💡 **Finding playlist IDs**:
   - Open Spotify → Right-click playlist → Share → Copy Playlist Link
   - Extract ID from URL: `spotify.com/playlist/37i9dQZF1DXcBWIGoYBM5M`
   
   The game starts automatically with orchestrated rounds! 🎵

5. **Monitor Game Progress**:
   ```bash
   # Check current state
   curl https://your-ngrok-url.ngrok-free.app/blindtest/state
   
   # Check current phase
   curl https://your-ngrok-url.ngrok-free.app/blindtest/round-phase
   ```

### Alternative: Manual Control

```bash
# Play current track
curl -X POST https://your-ngrok-url.ngrok-free.app/blindtest/playback/play

# Pause
curl -X POST https://your-ngrok-url.ngrok-free.app/blindtest/playback/pause

# Next track
curl -X POST https://your-ngrok-url.ngrok-free.app/blindtest/playback/next
```

### Development Tips

- **ngrok Web Interface**: Visit `http://localhost:4040` to inspect all HTTP requests going through the tunnel
- **Keep ngrok running**: Don't close the ngrok terminal while developing
- **ngrok URL changed?**: Update `SPOTIFY_REDIRECT_URI` environment variable, update Spotify app settings, restart Spring Boot, and re-authenticate

## 🧪 Testing

The project includes comprehensive test coverage:

- **Unit Tests**: Domain logic and individual components
- **Integration Tests**: Full application flow
- **Test Profile**: Uses fake adapters to avoid Spotify API calls

Run tests with:
```bash
mvn test
```

### Test Profiles

- `spotify`: Production profile with real Spotify integration
- `test`: Test profile with mock adapters

## 🛠️ Technologies

- **Spring Boot 3.5.9**: Application framework
- **Java 21**: Programming language
- **Lombok**: Reduce boilerplate code
- **PostgreSQL**: Database (configured, not yet implemented)
- **RestTemplate**: HTTP client for Spotify API
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework

## 📦 Domain Model

### Key Entities

- **Blindtest**: Main aggregate managing game state and track progression
- **BlindtestTrack**: Individual track in the game with playback metadata
- **Music**: Music metadata from Spotify (ID, title, duration, etc.)
- **BlindtestSettings**: Game configuration (discovery and reveal timing)
- **RoundOrchestrator**: Interface for game flow control
- **BlindtestRoundOrchestrator**: Automated round orchestration implementation

### Use Cases

- **StartBlindtestUseCase**: Initialize a new blind test session
- **BlindtestPlaybackUseCase**: Manual playback control
- **BlindtestRoundOrchestrator**: Automated game rounds with scheduling

### States

- `CREATED`: Initial state
- `PLAYING`: Active game
- `PAUSED`: Temporarily paused
- `FINISHED`: Game completed

### Round Phases

- `DISCOVERY`: Players try to guess the track (configurable duration)
- `REVEAL`: Track title/artist revealed, playback at specific timestamp
- `IDLE`: Between rounds or waiting state

### Reveal Timestamp Logic

The `BlindtestTrack.computeRevealSecond()` method determines when to start playback during the reveal phase:

- Starts **after** the discovery phase
- Targets approximately 30% through the track
- Ensures enough time remains for the reveal duration
- Falls back to track midpoint for very short tracks

## 🎯 Game Flow

The blind test follows an automated flow managed by the `BlindtestRoundOrchestrator`:

### Round Sequence

1. **Start**: `POST /blindtest/start` triggers the orchestrator
2. **Discovery Phase** (20 seconds default):
   - Phase set to `DISCOVERY`
   - Track plays from the beginning
   - Scheduler queues pause after discovery duration
3. **Transition** (1 second):
   - Playback pauses
   - System prepares for reveal
4. **Reveal Phase** (10 seconds default):
   - Phase set to `REVEAL`
   - Playback seeks to computed reveal timestamp (~30% into track)
   - Track resumes playing
   - Scheduler queues round completion
5. **Round Complete**:
   - Playback pauses
   - Phase set to `IDLE`
   - Track marked as played
   - Move to next track
   - If more tracks exist, repeat from step 2
   - If finished, game ends

### Scheduling

- Uses Spring's `TaskScheduler` for precise timing
- All transitions are automatically scheduled at round start
- Recursive orchestration for seamless track progression

## 🔧 Configuration Options

| Property | Default | Description |
|----------|---------|-------------|
| `blindtest.discovery-time-sec` | 20 | Discovery phase duration |
| `blindtest.reveal-time-sec` | 10 | Reveal phase duration |

## 🚨 Error Handling

The application includes comprehensive error handling:

- `400 BAD_REQUEST`: Invalid configuration
- `404 NOT_FOUND`: No active blind test
- `409 CONFLICT`: Invalid state transition
- `422 UNPROCESSABLE_ENTITY`: Business rule violation
- `503 SERVICE_UNAVAILABLE`: Spotify API issues

## 📝 License

This project is provided as-is for educational purposes.

## 👤 Author

**Cyril JCB**

---

## ⚠️ Important Notes

### Why ngrok is Required

**Spotify's HTTPS Requirement**: Spotify OAuth requires redirect URIs to use HTTPS. Since local development runs on `http://localhost:8080`, we cannot use it directly for the OAuth callback. 

**Solution**: ngrok creates a secure HTTPS tunnel to your local HTTP server:
- Your app runs locally on `http://localhost:8080` (unchanged)
- ngrok provides an HTTPS URL like `https://abc123.ngrok-free.app`
- Spotify redirects to the HTTPS ngrok URL
- ngrok forwards the request to your local app
- **Result**: Spotify's HTTPS requirement is satisfied while keeping development local! ✅

**Alternative**: Deploy your app to a server with HTTPS (Heroku, AWS, etc.), but ngrok is perfect for local development.

### Automated vs Manual Control

- **Automated Mode** (Recommended): The `RoundOrchestrator` is automatically triggered when you call `POST /blindtest/start`. This provides a seamless game experience with timed phases.

- **Manual Mode**: You can still use the `/blindtest/playback/*` endpoints for manual control, but this bypasses the automated orchestration.

### Spotify Requirements

- **Active Device Required**: An active Spotify device (desktop, mobile, or web player) must be running for playback features to work.

- **Spotify Premium**: Required for playback control via the Spotify Web API.

- **OAuth Scopes**: The application requests:
  - `user-read-playback-state`: Read current playback state
  - `user-modify-playback-state`: Control playback

### Scheduling Behavior

- Game phases are scheduled using Spring's `TaskScheduler`
- All timings are configurable via `application.properties`
- Scheduled tasks are non-blocking and run in a dedicated thread pool

## 🔍 Troubleshooting

### Common Issues

**"No Spotify devices available"**
- Solution: Open Spotify on any device (desktop app, mobile, or web player) and ensure playback is active
- The device must be logged in with the same account you authenticated with

**"Spotify authentication failed" or "redirect_uri mismatch"**
- Check that your `SPOTIFY_REDIRECT_URI` environment variable matches exactly what's configured in your Spotify app settings
- If using ngrok, ensure you've updated the Spotify app with your current ngrok URL
- Remember: ngrok free tier URLs change on restart

**"Invalid access token" or authentication expired**
- Re-authenticate by visiting `/auth/spotify/login`
- The refresh token mechanism will handle automatic renewal for subsequent requests

**ngrok URL changed**
- Update your `SPOTIFY_REDIRECT_URI` environment variable
- Update the redirect URI in your Spotify Developer Dashboard
- Restart your Spring Boot application
- Re-authenticate via `/auth/spotify/login`

**Playback issues**
- Verify your Spotify account is Premium
- Check that the Spotify app/web player is active and not paused
- Try manually playing a song in Spotify to "wake up" the device

### Debug Endpoints

```bash
# Check if a playlist is accessible (Spotify profile)
curl http://localhost:8080/debug/spotify/playlist/{playlist_id}
```

### Logs

Enable debug logging in `application.properties`:
```properties
logging.level.com.cyriljcb.blindify=DEBUG
logging.level.org.springframework.web=DEBUG
```

