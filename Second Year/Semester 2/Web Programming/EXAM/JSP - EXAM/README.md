# Tic Tac Toe Web Application

A simple JSP/Servlet web application that allows two players to play tic-tac-toe online.

## Features

- User authentication with username/password
- Session management (login/logout)
- Two-player tic-tac-toe game
- Only 2 players can play at a time (third player gets rejected)
- Simple and functional UI
- Basic input validation
- Confirmation dialogs for actions
- Game state stored in memory

## Requirements

- Java 8 or higher
- Apache Tomcat 8.5 or higher (or any servlet container)
- Maven 3.6 or higher (optional, for easy building)

## Default Test Users

The application comes with pre-configured test users:

- Username: `player1`, Password: `pass1`
- Username: `player2`, Password: `pass2`
- Username: `admin`, Password: `admin`

## How to Build and Run

### Option 1: Using Maven (Recommended)

1. Install Maven if not already installed
2. Open terminal/command prompt in the project directory
3. Compile the project:
   ```
   mvn clean compile
   ```
4. Run with embedded Tomcat:
   ```
   mvn tomcat7:run
   ```
5. Open your browser and go to: `http://localhost:8080/tictactoe`

### Option 2: Manual Compilation (Without Maven)

1. **Download Servlet API**: Download `servlet-api-3.1.0.jar` from [Maven Repository](https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api/3.1.0)
2. **Create lib directory**: Create a `lib` folder in the project root
3. **Place JAR**: Put the downloaded JAR file in the `lib` directory
4. **Run compilation script**: 
   - On Windows: Double-click `compile.bat` or run it from command prompt
   - On Linux/Mac: Create and run a similar shell script
5. **Manual WAR creation**:
   - Create a directory called `tictactoe`
   - Copy contents of `src/main/webapp` to `tictactoe`
   - Create `tictactoe/WEB-INF/classes` directory
   - Copy `target/classes/com` directory to `tictactoe/WEB-INF/classes/`
   - Create a ZIP file of the `tictactoe` directory and rename it to `tictactoe.war`
6. **Deploy**: Copy `tictactoe.war` to your Tomcat's `webapps` directory

### Option 3: Direct Development in IDE

1. Import the project into your IDE (Eclipse, IntelliJ IDEA, etc.)
2. Add servlet API to your project classpath
3. Configure your IDE to deploy to Tomcat
4. Run the project directly from your IDE

### Option 4: Build WAR with Maven and Deploy

1. Build the WAR file:
   ```
   mvn clean package
   ```
2. Copy the generated WAR file from `target/tictactoe.war` to your Tomcat's `webapps` directory
3. Start Tomcat
4. Access the application at: `http://localhost:8080/tictactoe`

## How to Play

1. **Login**: Use one of the test accounts to log in
2. **Join Game**: Click "Join Game" to join the current game session
3. **Wait for Player 2**: The game needs exactly 2 players to start
4. **Play**: Players take turns clicking on empty cells
   - Player 1 is always "X"
   - Player 2 is always "O"
   - The page refreshes automatically every 3 seconds to show opponent moves
5. **Win/Tie**: Game ends when someone gets 3 in a row or the board is full
6. **Reset**: Click "Reset Game" to start a new game
7. **Logout**: Click "Logout" to end your session

## Game Rules

- Only 2 players can be in a game at the same time
- If a third player tries to join, they get an error message
- Players must take turns (enforced by the application)
- Game automatically detects wins and ties
- When a player logs out, they are removed from the current game

## Technical Details

### Project Structure
```
src/
├── main/
│   ├── java/com/tictactoe/
│   │   ├── model/
│   │   │   ├── User.java          # User data model
│   │   │   └── Game.java          # Game logic and state
│   │   ├── servlet/
│   │   │   ├── LoginServlet.java  # Handles login
│   │   │   ├── LogoutServlet.java # Handles logout
│   │   │   └── GameServlet.java   # Handles game actions
│   │   └── util/
│   │       └── DatabaseUtil.java  # Simple file-based storage
│   └── webapp/
│       ├── WEB-INF/
│       │   └── web.xml            # Servlet configuration
│       ├── login.jsp              # Login page
│       └── game.jsp               # Main game page
├── pom.xml                        # Maven configuration
├── compile.bat                    # Windows compilation script
└── README.md                      # This file
```

### Technologies Used
- Java 8+
- JSP (JavaServer Pages)
- Servlets
- Maven (optional)
- HTML5/CSS3/JavaScript
- Simple file-based storage

## Limitations

- Game state is stored in memory (not persistent across server restarts)
- Only one game session at a time
- No real-time updates (uses page refresh every 3 seconds)
- Basic styling (minimal CSS)
- No user registration (uses predefined accounts)

## Troubleshooting

### Common Issues

1. **Compilation Errors**: Make sure servlet-api JAR is in your classpath
2. **404 Error**: Ensure the WAR file is properly deployed to Tomcat
3. **Page Not Loading**: Check that Tomcat is running and accessible
4. **Session Issues**: Clear browser cookies if login doesn't work

### Testing the Application

1. Open two different browsers (or incognito windows)
2. Login with `player1` in the first browser
3. Login with `player2` in the second browser
4. Both players join the game and start playing
5. Try logging in with a third user to see the rejection message

## Security Note

This is a simple educational application. In a production environment, you would want to:
- Hash passwords instead of storing them in plain text
- Use HTTPS
- Implement CSRF protection
- Use a proper database
- Add input sanitization
- Implement proper error handling 