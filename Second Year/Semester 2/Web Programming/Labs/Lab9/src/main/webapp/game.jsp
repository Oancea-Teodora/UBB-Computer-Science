<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.tictactoe.util.DatabaseUtil" %>
        <%@ page import="com.tictactoe.model.Game" %>
            <% // Check if user is logged in if (session.getAttribute("username")==null) {
                response.sendRedirect("login.jsp"); return; } String currentUser=(String)
                session.getAttribute("username"); Game game=DatabaseUtil.getCurrentGame(); %>
                <!DOCTYPE html>
                <html>

                <head>
                    <meta charset="UTF-8">
                    <title>Tic Tac Toe - Game</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            max-width: 600px;
                            margin: 20px auto;
                            padding: 20px;
                        }

                        .header {
                            text-align: center;
                            margin-bottom: 20px;
                        }

                        .game-info {
                            background-color: #f0f0f0;
                            padding: 15px;
                            border-radius: 5px;
                            margin-bottom: 20px;
                        }

                        .board {
                            display: grid;
                            grid-template-columns: repeat(3, 80px);
                            grid-template-rows: repeat(3, 80px);
                            gap: 5px;
                            margin: 20px auto;
                            justify-content: center;
                        }

                        .cell {
                            width: 80px;
                            height: 80px;
                            border: 2px solid #333;
                            background-color: #fff;
                            font-size: 36px;
                            font-weight: bold;
                            text-align: center;
                            line-height: 76px;
                            cursor: pointer;
                        }

                        .cell:hover {
                            background-color: #f0f0f0;
                        }

                        .cell.disabled {
                            cursor: not-allowed;
                            background-color: #e0e0e0;
                        }

                        .controls {
                            text-align: center;
                            margin-top: 20px;
                        }

                        .btn {
                            background-color: #4CAF50;
                            color: white;
                            padding: 10px 20px;
                            border: none;
                            border-radius: 4px;
                            cursor: pointer;
                            margin: 5px;
                        }

                        .btn:hover {
                            background-color: #45a049;
                        }

                        .btn-danger {
                            background-color: #f44336;
                        }

                        .btn-danger:hover {
                            background-color: #da190b;
                        }

                        .error {
                            color: red;
                            text-align: center;
                            margin: 10px 0;
                        }

                        .message {
                            color: green;
                            text-align: center;
                            margin: 10px 0;
                        }

                        .winner {
                            background-color: #4CAF50;
                            color: white;
                            text-align: center;
                            padding: 15px;
                            border-radius: 5px;
                            margin: 20px 0;
                            font-size: 18px;
                            font-weight: bold;
                        }
                    </style>
                </head>

                <body>
                    <div class="header">
                        <h1>Tic Tac Toe Game</h1>
                        <p>Welcome, <strong>
                                <%= currentUser %>
                            </strong>!</p>
                        <a href="logout" class="btn btn-danger">Logout</a>
                    </div>

                    <div class="game-info">
                        <p><strong>Players:</strong>
                            <%= game.getPlayer1() !=null ? game.getPlayer1() : "Waiting..." %> (X) vs
                                <%= game.getPlayer2() !=null ? game.getPlayer2() : "Waiting..." %> (O)
                        </p>
                        <p><strong>Current Turn:</strong>
                            <%= game.getCurrentPlayer() !=null ? game.getCurrentPlayer() : "N/A" %>
                        </p>
                        <p><strong>Players Connected:</strong>
                            <%= game.getPlayerCount() %>/2
                        </p>
                    </div>

                    <% if (request.getAttribute("error") !=null) { %>
                        <div class="error">
                            <%= request.getAttribute("error") %>
                        </div>
                        <% } %>

                            <% if (request.getAttribute("message") !=null) { %>
                                <div class="message">
                                    <%= request.getAttribute("message") %>
                                </div>
                                <% } %>

                                    <% if (game.isGameOver()) { %>
                                        <div class="winner">
                                            <% if ("tie".equals(game.getWinner())) { %>
                                                Game Over - It's a tie!
                                                <% } else { %>
                                                    Game Over - <%= game.getWinner() %> wins!
                                                        <% } %>
                                        </div>
                                        <% } %>

                                            <% if (game.getPlayerCount() < 2) { %>
                                                <div style="text-align: center; margin: 20px 0;">
                                                    <% if (game.getPlayer1()==null ||
                                                        (!game.getPlayer1().equals(currentUser) &&
                                                        game.getPlayer2()==null)) { %>
                                                        <form action="game" method="post" style="display: inline;">
                                                            <input type="hidden" name="action" value="join">
                                                            <input type="submit" value="Join Game" class="btn">
                                                        </form>
                                                        <% } else { %>
                                                            <p>Waiting for another player to join...</p>
                                                            <% } %>
                                                </div>
                                                <% } %>

                                                    <% if (game.canStart()) { %>
                                                        <div class="board">
                                                            <% String[][] board=game.getBoard(); for (int i=0; i < 3;
                                                                i++) { for (int j=0; j < 3; j++) { String
                                                                cellValue=board[i][j]; boolean
                                                                isMyTurn=currentUser.equals(game.getCurrentPlayer());
                                                                boolean canMove=!game.isGameOver() && isMyTurn &&
                                                                cellValue.isEmpty(); %>
                                                                <div class="cell <%= canMove ? "" : " disabled" %>"
                                                                    onclick="<%= canMove ? "makeMove(" + i + ", " + j
                                                                        + ")" : "" %>">
                                                                        <%= cellValue %>
                                                                </div>
                                                                <% } } %>
                                                        </div>
                                                        <% } %>

                                                            <div class="controls">
                                                                <% if (game.canStart() || game.isGameOver()) { %>
                                                                    <form action="game" method="post"
                                                                        style="display: inline;">
                                                                        <input type="hidden" name="action"
                                                                            value="reset">
                                                                        <input type="submit" value="Reset Game"
                                                                            class="btn"
                                                                            onclick="return confirm('Are you sure you want to reset the game?')">
                                                                    </form>
                                                                    <% } %>
                                                            </div>

                                                            <script>
                                                                function makeMove(row, col) {
                                                                    if (confirm('Make move at position (' + (row + 1) + ', ' + (col + 1) + ')?')) {
                                                                        var form = document.createElement('form');
                                                                        form.method = 'post';
                                                                        form.action = 'game';

                                                                        var actionInput = document.createElement('input');
                                                                        actionInput.type = 'hidden';
                                                                        actionInput.name = 'action';
                                                                        actionInput.value = 'move';
                                                                        form.appendChild(actionInput);

                                                                        var rowInput = document.createElement('input');
                                                                        rowInput.type = 'hidden';
                                                                        rowInput.name = 'row';
                                                                        rowInput.value = row;
                                                                        form.appendChild(rowInput);

                                                                        var colInput = document.createElement('input');
                                                                        colInput.type = 'hidden';
                                                                        colInput.name = 'col';
                                                                        colInput.value = col;
                                                                        form.appendChild(colInput);

                                                                        document.body.appendChild(form);
                                                                        form.submit();
                                                                    }
                                                                }

                                                                // Auto-refresh every 3 seconds to see other player's moves
                                                                setTimeout(function () {
                                                                    window.location.reload();
                                                                }, 3000);
                                                            </script>
                </body>

                </html>