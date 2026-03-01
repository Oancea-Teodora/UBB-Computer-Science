<%@ page language="java" contentType="text/html; charset=UTF-8" %>
    <% String board=(String) request.getAttribute("board"); String turn=(String) request.getAttribute("turn"); String
        mySymbol=(String) request.getAttribute("mySymbol"); Boolean myTurn=(Boolean) request.getAttribute("myTurn"); if
        (board==null || board.length() !=9) { board="_________" ; } if (turn==null) turn="X" ; if (mySymbol==null)
        mySymbol="?" ; if (myTurn==null) myTurn=false; %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>X-O Game</title>
            <% if (!myTurn) { %>
                <meta http-equiv="refresh" content="1">
                <% } %>
        </head>

        <body>
            <% String resultMsg = (String) request.getAttribute("resultMessage"); %>
            <% if (resultMsg != null) { %>
            <script>
                alert("<%= resultMsg %>");
            </script>
            <% } %>

        <h2>Player: <%= mySymbol %> | Turn: <%= turn %>
            </h2>
            <p><strong>Username:</strong>
                <%= session.getAttribute("username") %>
            </p>

            <table border="1" cellpadding="10">
                <% for (int row=0; row < 3; row++) { %>
                    <tr>
                        <% for (int col=0; col < 3; col++) { int idx=row * 3 + col; char cell=board.charAt(idx); if
                            (cell !='_' ) { %>
                            <td style="text-align:center; font-size:24px;">
                                <%= cell %>
                            </td>
                            <% } else if (myTurn) { %>
                                <td>
                                    <form method="post" action="game">
                                        <input type="hidden" name="cell" value="<%= idx %>" />
                                        <input type="submit" value="Click"
                                            style="width:50px; height:50px; font-size:12px; background-color:#e0e0e0; border:2px solid #333;" />
                                    </form>
                                </td>
                                <% } else { %>
                                    <td style="width:50px; height:50px; background-color:#f0f0f0;"></td>
                                    <% } %>
                                        <% } %>
                    </tr>
                    <% } %>
            </table>

            <p><a href="logout">Logout</a></p>
        </body>

        </html>