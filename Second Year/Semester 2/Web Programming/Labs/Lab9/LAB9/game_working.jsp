<%@ page language="java" contentType="text/html; charset=UTF-8" %>
    <% String board=(String) request.getAttribute("board"); String turn=(String) request.getAttribute("turn"); String
        mySymbol=(String) request.getAttribute("mySymbol"); Boolean myTurn=(Boolean) request.getAttribute("myTurn"); if
        (board==null || board.length() !=9) { board="         " ; } if (turn==null) turn="X" ; if (mySymbol==null)
        mySymbol="?" ; if (myTurn==null) myTurn=false; %>
        <html>

        <head>
            <title>X-O Game</title>
        </head>

        <body>
            <h2>Player: <%= mySymbol %> | Turn: <%= turn %>
            </h2>
            <table border="1" cellpadding="10">
                <% for (int row=0; row < 3; row++) { %>
                    <tr>
                        <% for (int col=0; col < 3; col++) { int idx=row * 3 + col; char cell=board.charAt(idx); if
                            (cell !=' ' ) { %>
                            <td style="text-align:center; font-size:24px;">
                                <%= cell %>
                            </td>
                            <% } else if (myTurn) { %>
                                <td>
                                    <form method="post" action="game">
                                        <input type="hidden" name="cell" value="<%= idx %>" />
                                        <input type="submit" value=" " style="width:40px; height:40px;" />
                                    </form>
                                </td>
                                <% } else { %>
                                    <td style="width:40px; height:40px;"></td>
                                    <% } } %>
                    </tr>
                    <% } %>
            </table>
            <p><a href="logout">Logout</a></p>
        </body>

        </html>