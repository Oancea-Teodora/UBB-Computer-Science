import textwrap
import os

dir_path = os.path.dirname(__file__)
new_line = "\n"
#used with input prompt (asking user to press ENTER key to continue).
continue_string = "\n\n>>>Please press ENTER key to continue<<<"

def exctract_str(text_file_name):
    """Extract and return contents of a provided text file

    Args:
      text_file_name (str) : name (with extention) of text file within directory
       of this module.
    Returns:
      string containint entire contents of provided text file
    """
    with open(os.path.join(dir_path, text_file_name), 'r') as filename:
        text = filename.read()
    return text

class Formatting:

    #Seperator line string for visual formatting
    str_base = "="

    line_str1 = str_base*86 # makes a line of 86 "="
    line_str2 = line_str1.replace("=", "-") # makes a line of 86 "-"
    line_str3 = line_str1.replace("=", "*") # makes a line of 86 "*"

    side_str1 = line_str1[:int(len(line_str1)/2)-2].replace("=",">") # makes a line of 42 ">" and 42 "="
    side_str2 = line_str1[:int(len(line_str1)/2)-2].replace("=","<") # makes a line of 42 "<" and 42 "="
    side_str3 = line_str1[:int(len(line_str1)/2)-2].replace("="," ") # makes a line of 42 " " and 42 "="

    #Formatting methods:
    @classmethod
    def line_wrap1(cls, str):
        """
        This method takes a string and wraps it in a line of "="
        :param str:
        :return:
        """
        return cls.line_str1 + new_line + str + new_line + cls.line_str1

    @classmethod
    def line_wrap2(cls, str):
        """
        This method takes a string and wraps it in a line of "-"
        :param str:
        :return:
        """
        return cls.line_str2 + new_line + str + new_line + cls.line_str2

    @classmethod
    def line_wrap3(cls, str):
        """
        This method takes a string and wraps it in a line of "*"
        :param str:
        :return:
        """
        return cls.line_str3 + new_line + str + new_line + cls.line_str3

    @classmethod
    def side_wrap(cls, str):
        """
        This method takes a string and wraps it in a line of ">" and "<"
        :param str:
        :return:
        """
        half = int((len(str)/2))
        return cls.side_str1[:-half] + str + cls.side_str2[:-half]

    @classmethod
    def center_wrap(cls, str):
        """
        This method takes a string and wraps it in a line of " " and "=" (centered)
        :param str:
        :return:
        """
        half = int((len(str)/2))
        return cls.side_str3[:-half] + str + cls.side_str3[:-half]


class OpeningStatements:
    """Opening statements and formatting"""
    #Opening statements.
    header_str ="""BATTLEFIELD GAME"""
    welcome_str = "WELCOME!"

    #Introductory statements, describing the game.
    game_desc_str1 = exctract_str("game_desc_str1.txt")
    game_desc_str2 = exctract_str("game_desc_str2.txt")
    game_desc_str3 = exctract_str("game_desc_str3.txt")
    game_desc_str4 = exctract_str("game_desc_str4.txt")
    place_ships_str = "\n\nIt is time to place your ships! Are you ready?"
    ready_str = "Your ships are all set! Here is your Battlefield:"

    @classmethod
    def intro_str(cls):
        """
        Print introductory statements, describing the game.
        :return:
        """
        print(new_line + Formatting.line_wrap1(Formatting.center_wrap(cls.header_str)))
        print(new_line * 2 + Formatting.line_wrap3(Formatting.center_wrap(cls.welcome_str)))
        input(continue_string)
        print(textwrap.dedent(new_line + Formatting.line_str1 + cls.game_desc_str1))
        input(continue_string)
        print(textwrap.dedent(new_line + Formatting.line_str1 + cls.game_desc_str2))
        input(continue_string)
        print(textwrap.dedent(new_line + Formatting.line_str1 + cls.game_desc_str3))
        input(continue_string)
        print(textwrap.dedent(new_line + Formatting.line_str1 + cls.game_desc_str4))
        input(continue_string)
        print(textwrap.dedent(new_line + Formatting.line_str1 + cls.place_ships_str))
        input(continue_string)


class ClosingStatements:
    """Closing Statements, and formatting."""

    winner_str_raw = "{} HAS WON!!!"
    winner_str = Formatting.line_wrap3(Formatting.center_wrap(winner_str_raw))

    final_str1_raw = "THANK YOU FOR PLAYING!"
    final_str1 = new_line + Formatting.line_wrap3(Formatting.center_wrap((final_str1_raw)))

    final_str2_raw = "\n\n>>>Please press ENTER key to Exit<<<"
    final_str2 = new_line + Formatting.center_wrap(final_str2_raw)

    final_str3 = new_line + Formatting.line_wrap1(Formatting.center_wrap(OpeningStatements.header_str))

    final_str4_raw = "THE END."
    final_str4 = new_line + Formatting.line_wrap1(Formatting.center_wrap(final_str4_raw))


class DisplayStrings:
    """Used for Battlefield or Ship display captions"""

    #Battlefield caption, for display functionality of Battlefiled class.
    enemy_battlefield_str = "Enemy Battlefield: "
    player_battlefield_str = "Your Battlefield: "
    battlefield_str = "{} Battlefield: "

    #Used when displaying the ships of either player.
    display_ships_intro = "The following are {} ships:"
    display_ships_str_main = "{}. {}\t\t{}"


class ShipPLacementStrings:
    """Used for interactive ship placement"""

    gen_coords_input1_str = "Please enter {0} coordinate for the position of {1} ({2}): "
    gen_coords_input2_str_addon = "Enter the *NUMBER* corresponding to the coordinates option of your choice : "


class TargettingStrings:
    """Used during targettinhg"""

    target_str = "Now it's your turn to target the enemy!"
    target_cords_str = "Please enter target coordinates!"
    target_complete = "We've fired our missles to target!"
    incoming_str = "Now it's the Enemy's turn to target your Battlefield. Brace for impact!"
    incoming_complete = "Enemy missles have landed!"
    ship_hit_str = "Target at {} - Ship hit at target!!!"
    empty_waters_str = "Empty waters hit. Target at {}. No ships at target."


class ErrorStrings:
    """
    Used for error handling and user input validation.
    """

    error_str = new_line * 2 + "INCORRECT INPUT! \n{} Please try again!"
    value_error_str_first_split = "Make sure to enter exact coordinates (for starting coordinate)"
    value_error_str_second_split = "or exact choice number (for ending coordinate)."
    value_error_str = value_error_str_first_split + new_line + value_error_str_second_split
    key_error_str = "Make sure your coordinates are in range!"
    index_error_str_first_split = "Make sure to enter exact coordinates (for starting coordinate)"
    index_error_str_second_split = "or exact choice number (for ending coordinate)."
    index_error_str = index_error_str_first_split + new_line + index_error_str_second_split
    busy_coord_error_str = "There is already a ship in that location!!"
    targetted_coord_error_str = "These Coordinates have already been targetted!!"
    not_enough_room_error_str = "There is no enough room for this ship at that coordinate!"
