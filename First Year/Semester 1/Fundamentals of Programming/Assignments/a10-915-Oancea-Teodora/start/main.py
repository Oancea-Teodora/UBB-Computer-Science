from entities.Player import Player, Computer
from Board_Strings import board_strings as strings

NL ="\n"

def check_winner():
    """Check if there is a winner.

    Returns:
      The winner (player1 or player2), or <None>, if no winner.
    """
    if player1.check_fleet_sunk():
        winner = player2
    elif player2.check_fleet_sunk():
        winner = player1
    else:
        winner = None
    return winner

def current_turn_string(player):
    """Return custom string indicating to the player who's turn it is
    Args:
      player (object): the Player who's turn it currently is.
    Returns:
      A customized string indicating current turn.
    """
    if player == player1:
        string = strings.TargettingStrings.target_str
    elif player == player2:
        string = strings.TargettingStrings.incoming_str
    return string

def turn(player_x, player_y):
    """Execute a turn, targetting the opposing player

    Targetting is preceded by:
      input: asking playuer to press ENTER key to continue
      print statement: letting player know who's turn it is
      input: asking playuer to press ENTER key to continue

    Args:
      player_x (object): the Player who's turn it is.
      player_y (object): the opposing Player against.
    """
    input(strings.continue_string)
    print(strings.Formatting.line_str2 + NL*2 + str(current_turn_string(player_x)))
    input(strings.continue_string)
    player_x.target(player_y)

def ready_to_play():
    """print a statement indicating that the game (i.e. first turn) is about
    to start; and display main player's Battlefiled"""
    print(strings.Formatting.line_str2 + NL*2 + strings.OpeningStatements.ready_str + NL)
    player1.battlefield.display()

def play_game():
    """Until there is a winner, alternate turns for player1 and player2."""
    while not check_winner():
        turn(player1, player2)
        if not check_winner():
            turn(player2, player1)

def end_game():
    """Declare the winner and print ending statements."""
    print(strings.ClosingStatements.winner_str.format(check_winner()))
    print(NL*2 + strings.ClosingStatements.final_str3)
    print(NL + strings.ClosingStatements.final_str1)
    input(NL + strings.ClosingStatements.final_str2)

#-----------------------------------------------------------------------------
#START
strings.OpeningStatements.intro_str()

player1 = Player()
player2 = Computer()

ready_to_play()
play_game()
end_game()