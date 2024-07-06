from entities.Board import Battlefield, ComputerBattlefield, new_line
from entities.Ship import Ship
from Board_Strings.board_strings import continue_string, Formatting, DisplayStrings, ShipPLacementStrings, TargettingStrings, ErrorStrings
from Exceptions.InputExceptions import BusyCoordinateException, TargettedCoordinateException, NotEnoughRoomException
import random

class Player:
    player_count = 0

    def __init__(self):
        Player.player_count += 1
        self.id = Player.player_count
        self.battlefield = Battlefield(10, 10)
        self.fleet = self.generate_ships()
        self.fleet_sunk = False
        # self.targetted_coordinates = []
        # self.hit_coordinates = []
        # self.active_targets = []

    def __repr__(self):
        return "Player" + str(self.id)

    def shoot_board(self, coordinate):
        """
        Update the player's battlefield grid based on the shot coordinate.
        :param coordinate: The coordinate to shoot.
        """
        battlefield = self.battlefield
        grid = battlefield.grid

        if coordinate in grid:
            # Check if the coordinate corresponds to an empty sea or a ship
            if grid[coordinate] == Battlefield.states[0]:
                grid[coordinate] = Battlefield.states[6]  # Mark as a miss
            elif grid[coordinate] == Battlefield.states[5]:
                grid[coordinate] = Battlefield.states[7]  # Mark as a hit
            else:
                raise ValueError("Invalid coordinate state")
        else:
            raise ValueError("Invalid coordinate")

    def gen_coords(self, ship_type):
        """
        Generate coordinates for a ship of a given type, based on user input.
        :param ship_type:
        :return:
        """
        self.battlefield.display_wrapped("Your")
        ship_size = Ship.types[ship_type]
        coordinates = []

        # Prompt for user input of starting coordinate of a given ship type.
        # Raise exception if a ship already exist at input coordinate.
        input_str = ShipPLacementStrings.gen_coords_input1_str
        input1 = input(input_str.format("starting", str(ship_type), ship_size * "+"))
        start_coordinate = (input1[0].upper(), int(input1[1:]))
        if self.battlefield.grid[start_coordinate] == Battlefield.states[5]:
            raise BusyCoordinateException
        coordinates.append(start_coordinate)

        print(new_line)
        # Backup copy of grid to be used to restore it back from temporary changes
        copy_grid = self.battlefield.grid.copy()
        self.battlefield.grid[start_coordinate] = "*"

        # Customizing string for next selection
        input2_str_addon = ShipPLacementStrings.gen_coords_input2_str_addon
        input2_str = input_str.format("ending", str(ship_type), ship_size * "+").replace("enter", "choose").replace(":",
                                                                                                                    ".") + "\n" + input2_str_addon + new_line * 2

        # Obtain valid options for ship placement from starting coordinate.
        # Raise exception if there is not enough room for any ship.
        options = self.battlefield.coord_opts(start_coordinate, ship_type)
        if not options:
            self.battlefield.grid.update(copy_grid)
            raise NotEnoughRoomException

        # Display options to user and prompt user to select option using a numerical identifier displayed
        num = 1
        for key in options.keys():
            last_coord = options[key][-1]
            input2_str += str(num) + " ({}): ".format(key[:-2]) + Battlefield.coord_to_str(last_coord) + "\n"
            self.battlefield.grid[last_coord] = num
            num += 1
        self.battlefield.display()
        print(new_line)
        input2 = input(input2_str)

        # Restore original grid
        self.battlefield.grid.update(copy_grid)

        # Add the rest of the coordinates (except for starting) from the chosen
        #  option to a list & return sorted list
        for coord in list(options.values())[int(input2) - 1][1:]:
            coordinates.append(coord)
        coordinates.sort()
        return coordinates

    def generate_ships(self):
        """
        Generate a fleet of ships for a player, based on user input.
        :return:
        """
        fleet = {}
        for ship_type in Ship.types.keys():
            error_str = ErrorStrings.error_str
            while True:
                try:
                    ship = Ship(self.gen_coords(ship_type), ship_type, self.battlefield)
                    break
                except ValueError:
                    print(error_str.format(ErrorStrings.value_error_str))
                except KeyError:
                    print(error_str.format(ErrorStrings.key_error_str))
                except IndexError:
                    print(error_str.format(ErrorStrings.index_error_str))
                except BusyCoordinateException:
                    print(error_str.format(ErrorStrings.busy_coord_error_str))
                except NotEnoughRoomException:
                    print(error_str.format(ErrorStrings.not_enough_room_error_str))
            fleet[ship_type] = ship
        return fleet

    def check_fleet_sunk(self):
        """Check if the entire fleet has been sunk. If so, update fleet_sunk attribute to True.

        Returns:
          fleet_sunk (bool): (True if all ships have been sunk).
        """
        sunk_ships = 0
        for ship in self.fleet.values():
            if ship.sunk:
                sunk_ships += 1
        if sunk_ships == len(self.fleet):
            self.fleet_sunk = True
        return self.fleet_sunk

    def display_ships2(self):
        """Display the player's ships."""
        player_str = "Player's"  # Define player_str before using it
        print(DisplayStrings.display_ships_intro.format(player_str))

        for ship in self.fleet.values():
            print(str(ship))
    def display_ships(self):
        """Print a listing of all of the non-sunk ships of the player, \
            and a visual representation of each ship."""


        if self.id == 1:
            player_str = "YOUR"
        elif self.id == 2:
            player_str = "ENEMY"

        # Obtain non-sunk ships.
        ships = [ship for ship in self.fleet.values() if not ship.sunk]

        # print caption statement
        print(new_line + Formatting.line_str1 + new_line)
        print(DisplayStrings.display_ships_intro.format(player_str))

        # print each afloat ship's name, followed by a representation of
        #  correct size (with "+" character")
        num = 1
        for ship in ships:
            print(DisplayStrings.display_ships_str_main.format(
                num, ship.type, "+" * ship.size), end="\n")
            num += 1
        print(new_line)

    @staticmethod
    def display_targetting_results(player, coordinate, target_hit):
        """Display Battlefield and print statement explaining retults of targetting attempt

        Args:
          coordinate (tuple): (a coordinate).
          target_hit (bool): True if a target was hit.
        """
        if target_hit:
            result_str = new_line * 2 + Formatting.line_wrap3(TargettingStrings.ship_hit_str.format(str(coordinate))) + new_line * 2
        else:
            result_str = new_line * 2 + TargettingStrings.empty_waters_str.format(str(coordinate)) + new_line * 2

        print(new_line * 2 + TargettingStrings.target_complete + new_line)
        player.battlefield.display()
        print(result_str)

    def target(self, player):
        """target the opposing player's battlefield based on coordinates
            input by the user (intended for player1).
        Args:
          player (object): the player being targetted
        """
        # Track targetting attempt, and whether last attempt was successful
        shot = 0
        last_shot_hit = False
        # if last attempt successful (target hit), the targetting repeats.
        while shot < 1 or last_shot_hit:
            battlefield = player.battlefield
            grid = player.battlefield.grid
            while True:
                player.display_ships()
                input(continue_string)
                battlefield.display_wrapped("Enemy")
                error_str = ErrorStrings.error_str
                try:
                    input1 = (input(TargettingStrings.target_cords_str))
                    coordinate = (input1[0].upper(), int(input1[1:]))
                    if grid[coordinate] == Battlefield.states[6] or grid[coordinate] == Battlefield.states[7] or grid[
                        coordinate] == Battlefield.states[9]:
                        raise TargettedCoordinateException
                    break
                except ValueError:
                    print(error_str.format(ErrorStrings.value_error_str))
                except KeyError:
                    print(error_str.format(ErrorStrings.key_error_str))
                except IndexError:
                    print(error_str.format(ErrorStrings.index_error_str))
                except TargettedCoordinateException:
                    print(error_str.format(ErrorStrings.targetted_coord_error_str))

            shot += 1

            if not grid[coordinate]:
                grid[coordinate] = Battlefield.states[6]
                self.display_targetting_results(player, coordinate, False)
                last_shot_hit = False

            # If a target was hit, update coordinate status on the grid to show
            #  a hit coordiante
            elif grid[coordinate] == Battlefield.states[5]:
                grid[coordinate] = Battlefield.states[7]
                self.display_targetting_results(player, coordinate, True)
                last_shot_hit = True

                for ship in player.fleet.values():
                    if coordinate in ship.coordinates:
                        ship.check_sunk()
                if player.check_fleet_sunk():
                    break

                input(continue_string)
                print(Formatting.line_str2 + new_line * 2 + TargettingStrings.target_str)
                input(continue_string)


class Computer(Player):
    def __init__(self):
        Player.player_count += 1
        self.id = Player.player_count
        self.battlefield = ComputerBattlefield(10, 10)
        self.fleet = self.generate_ships()
        self.fleet_sunk = False
        self.targetted_coordinates = []
        self.hit_coordinates = []
        self.active_targets = []

    def __repr__(self):
        return "Computer"


    def gen_coords(self, ship_type):
        """
        Generate coordinates for a ship of a given type randomly.
        :param ship_type:
        :return:
        """
        coordinates = []

        # Randomly generate starting cooriundate. Raise exception if a ship
        # already exist at randomly chosen coordinate
        start_coordinate = (self.battlefield.rows[random.randint(0, len(self.battlefield.rows) - 1)],
                            random.randint(1, len(self.battlefield.columns)))
        if self.battlefield.grid[start_coordinate] == Battlefield.states[5]:
            raise BusyCoordinateException
        coordinates.append(start_coordinate)

        # Obtain valid options for ship placement from starting coordinate.
        #  Raise exception if there is not enough room for any ship.
        options = list(self.battlefield.coord_opts(start_coordinate, ship_type).values())
        if not options:
            raise NotEnoughRoomException

        # Add the rest of the coordinates (except for starting) from the chosen
        #  option to a list & return sorted list
        for coord in options[random.randint(0, len(options) - 1)][1:]:
            coordinates.append(coord)
        coordinates.sort()
        return coordinates

    def generate_ships(self):
        """
        Generate a fleet of ships for a player, based on randomly generated coordinates.
        :return:
        """

        fleet = {}
        for ship_type in Ship.types.keys():
            while True:
                try:
                    ship = Ship(
                        self.gen_coords(ship_type), ship_type, self.battlefield)
                    break
                except BusyCoordinateException as a:
                    pass
                except NotEnoughRoomException as b:
                    pass
            fleet[ship_type] = ship
        return fleet

    def non_adjascent_targets_inner(self, coordinate, player, direction_func, target_rows_or_columns, largest_ship_size):
        """
        Evaluate non-adjascent partially hit coordinates (active targets) for potentially belonging to a single ship
        """
        options = []
        row_index = player.battlefield.row_index
        current_coord = coordinate

        if direction_func == player.battlefield.coord_right:
            target_columns = target_rows_or_columns
            index_difference = target_columns[target_columns.index(coordinate[-1]) + 1] - coordinate[-1]
        elif direction_func == player.battlefield.coord_down:
            target_row_indices = target_rows_or_columns
            index_difference = target_row_indices[target_row_indices.index(row_index(coordinate[0])) + 1] - row_index(
                coordinate[0])
        seperation = index_difference - 1

        if (largest_ship_size - 2) >= seperation >= 1:
            for num in range(seperation):
                next_coord = direction_func(current_coord)
                if next_coord in self.targetted_coordinates and next_coord not in self.hit_coordinates:
                    options = None
                    break
                elif next_coord not in self.targetted_coordinates:
                    options.append(next_coord)
                current_coord = next_coord
        return options

    def non_adjascent_targets(self, coordinate, player):
        """Evaluate non-adjascent partially hit coordinates (active targets)\
             for potentially belonging to a single ship
        Args:
          coordinate (tuple): a coordinate.
          player (object): current player
        Returns:
          A list of coordinate options for targetting
        """
        battlefield = player.battlefield  ######

        options = []
        row_index = battlefield.row_index
        row = battlefield.get_row(coordinate)
        column = battlefield.get_column(coordinate)

        targets_in_row = [target for target in self.active_targets if target in row]
        targets_in_column = [target for target in self.active_targets if target in column]

        largest_ship_size = 0
        for ship in self.fleet.values():
            if not ship.sunk:
                if ship.size >= largest_ship_size:
                    largest_ship_size = ship.size


        if len(targets_in_row) > 1:
            target_columns = [target[-1] for target in targets_in_row]
            target_columns.sort()
            if target_columns.index(coordinate[-1]) < len(target_columns) - 1:
                row_options = self.non_adjascent_targets_inner(coordinate, player, battlefield.coord_right,
                                                               target_columns, largest_ship_size)
                options.extend([option for option in row_options if option not in options])

        # Same as above - except for column
        if len(targets_in_column) > 1:
            target_row_indices = [row_index(target) for target in targets_in_column]
            target_row_indices.sort()
            if target_row_indices.index(row_index(coordinate[0])) < len(target_row_indices) - 1:
                column_options = self.non_adjascent_targets_inner(coordinate, player, battlefield.coord_down,
                                                                  target_row_indices, largest_ship_size)
                options.extend([option for option in column_options if option not in options])

        return options

    def adjascent_targets(self, direction_func, coord, player):

        options = []
        range_value = player.battlefield.get_range_value(direction_func, coord)
        current_coord = coord
        if direction_func(coord) and direction_func(coord) in self.hit_coordinates:
            for num in range(range_value):
                next_coord = direction_func(current_coord)
                if next_coord in self.targetted_coordinates and next_coord not in self.hit_coordinates:
                    break
                elif next_coord not in self.targetted_coordinates:
                    if next_coord not in options:
                        options.append(next_coord)
                    break
                else:
                    pass
                current_coord = next_coord
        return options


    def target_options(self, player):
        """
        Evaluate target options for the computer
        :param player:
        :return:
        """

        battlefield = player.battlefield
        target_options = []
        coord_up = battlefield.coord_up
        coord_down = battlefield.coord_down
        coord_left = battlefield.coord_left
        coord_right = battlefield.coord_right

        # If there are "active targets":
        if self.active_targets:
            options = []
            # Options - return only if there are non-adjascent "active targets" within the same row or column.
            for coordinate in self.active_targets:
                options.extend(self.non_adjascent_targets(coordinate, player))
            if options:
                target_options.extend([option for option in options if option not in target_options])
                return target_options


            else:
                for coordinate in self.active_targets:
                    if coord_up(coordinate):
                        options.extend(self.adjascent_targets(coord_up, coordinate, player))
                    if coord_down(coordinate):
                        options.extend(self.adjascent_targets(coord_down, coordinate, player))
                    if coord_left(coordinate):
                        options.extend(self.adjascent_targets(coord_left, coordinate, player))
                    if coord_right(coordinate):
                        options.extend(self.adjascent_targets(coord_right, coordinate, player))

                if options:
                    target_options.extend([option for option in options if option not in target_options])
                    return target_options

                # If "active targets" are neither adjascent, nor in the same
                #  row or column, return options on all 4 sides of the target(s)
                else:
                    for coordinate in self.active_targets:
                        if coord_up(coordinate):
                            if coord_up(
                                    coordinate) not in self.targetted_coordinates and coord_up not in target_options:
                                options.append(coord_up(coordinate))
                        if coord_down(coordinate):
                            if coord_down(
                                    coordinate) not in self.targetted_coordinates and coord_down not in target_options:
                                options.append(coord_down(coordinate))
                        if coord_left(coordinate):
                            if coord_left(
                                    coordinate) not in self.targetted_coordinates and coord_left not in target_options:
                                options.append(coord_left(coordinate))
                        if coord_right(coordinate):
                            if coord_right(
                                    coordinate) not in self.targetted_coordinates and coord_right not in target_options:
                                options.append(coord_right(coordinate))
                    target_options.extend([option for option in options if option not in target_options])
                    return target_options
        # If there are no active targets:
        else:
            available_targets = [coordinate for coordinate in battlefield.coordinates if
                                 not battlefield.grid[coordinate] or battlefield.grid[coordinate] == Battlefield.states[
                                     5]]
            # Create preferred options lists, top be filled based on different criteriu
            options = []
            options_preferred_A = []
            options_preferred_B = []
            options_preferred_C = []
            options_preferred_D = []
            options_preferred_E = []
            options_preferred_F = []
            options_preferred_G = []

            # Temporary collection 1 (all preferred lists)
            preferred_lists_temp_1 = [options_preferred_A, options_preferred_B,
                                      options_preferred_C, options_preferred_D, options_preferred_E,
                                      options_preferred_F, options_preferred_G]

            # Temporary collection 2 (select preferred lists)
            preferred_lists_temp_2 = [options_preferred_A, options_preferred_B,
                                      options_preferred_C, options_preferred_D]

            # Check available targets for size clearance, based on the smallest
            # enemy ship afloat, and add all coordinates that have enough room to hide a ship.
            for coordinate in available_targets:
                for ship in player.fleet.values():
                    if not ship.sunk:
                        if ship.size <= battlefield.horizontal_target_size(coordinate,
                                                                           self.targetted_coordinates) or ship.size <= battlefield.vertical_target_size(
                                coordinate, self.targetted_coordinates):
                            if coordinate not in options:
                                options.append(coordinate)

            for option in options:
                # Efficient Targetting System for widely distributed targetting.

                # Check if there are 2x2, 2x1, 1x2, or 1x1 available targets
                up_2x = (coord_up(option) and coord_up(option) in available_targets) and (
                            coord_up(coord_up(option)) and coord_up(coord_up(option)) in available_targets)
                # Check if there are 2x2, 2x1, 1x2, or 1x1 available targets
                down_2x = (coord_down(option) and coord_down(option) in available_targets) and (
                            coord_down(coord_down(option)) and coord_down(coord_down(option)) in available_targets)
                # Check if there are 2x2, 2x1, 1x2, or 1x1 available targets
                left_2x = (coord_left(option) and coord_left(option) in available_targets) and (
                            coord_left(coord_left(option)) and coord_left(coord_left(option)) in available_targets)
                # Check if there are 2x2, 2x1, 1x2, or 1x1 available targets
                right_2x = (coord_right(option) and coord_right(option) in available_targets) and (
                            coord_right(coord_right(option)) and coord_right(coord_right(option)) in available_targets)


                up_1x = (coord_up(option) and coord_up(option) in available_targets)
                down_1x = (coord_down(option) and coord_down(option) in available_targets)
                left_1x = (coord_left(option) and coord_left(option) in available_targets)
                right_1x = (coord_right(option) and coord_right(option) in available_targets)

                # Combinattions of the above. Sorted from high priority
                # high (far from targetted coordinates) to low (near targetted)
                A = (up_2x and down_2x and left_2x and right_2x)
                B = (up_2x and down_2x and left_1x and right_1x) or (up_1x and down_1x and left_2x and right_2x)
                C = (up_1x and down_1x and left_1x and right_1x)
                D = (up_2x and down_2x and (left_1x or right_1x)) or ((up_1x or down_1x) and left_2x and right_2x)
                E = ((up_2x and down_2x) or (left_2x and right_2x))
                F = ((up_1x and down_1x) or (left_1x and right_1x))
                G = (up_1x or down_1x or left_1x or right_1x)

                if A and option not in options_preferred_A:
                    options_preferred_A.append(option)
                elif B and option not in options_preferred_B:
                    options_preferred_B.append(option)
                elif C and option not in options_preferred_C:
                    options_preferred_C.append(option)
                elif D and option not in options_preferred_D:
                    options_preferred_D.append(option)
                elif E and option not in options_preferred_E:
                    options_preferred_E.append(option)
                elif F and option not in options_preferred_F:
                    options_preferred_F.append(option)
                elif G and option not in options_preferred_G:
                    options_preferred_G.append(option)

            preferred_lists_1 = [x for x in preferred_lists_temp_1 if x]
            preferred_lists_2 = [x for x in preferred_lists_temp_2 if x]

            if preferred_lists_1:
                alternative_num = random.randint(0, 1)
                if alternative_num == 0 or not preferred_lists_2:
                    target_options.extend([option for option in preferred_lists_1[0] if option not in target_options])
                else:
                    # Select from a random list
                    target_options.extend(
                        [option for option in preferred_lists_2[random.randint(0, len(preferred_lists_2) - 1)] if
                         option not in target_options])
                return target_options

            # If still no options returned, return all unique options left
            else:
                target_options.extend([option for option in options if option not in target_options])
                return target_options

    def target(self, player):
        """target the opposing player's battlefield based on coordinates
            input by AI algorhythm.

        Args:
          player (object): the player being targetted
        """
        # Track targetting attempt, and whether last attempt was successful
        shot = 0
        last_shot_hit = False
        # if last attempt successful (target hit), the targetting repeats.
        while shot < 1 or last_shot_hit:
            battlefield = player.battlefield
            grid = player.battlefield.grid
            # Display user ships, user battlefield,
            rows = battlefield.rows
            columns = battlefield.columns
            player.display_ships()
            input(continue_string)
            battlefield.display_wrapped("Your")
            input(continue_string)
            while True:
                try:
                    # Request options from AI algorhythm, and select random option
                    options = self.target_options(player)
                    if options:
                        coordinate = options[random.randint(0, len(options) - 1)]
                    # Should there be no options, fire at a random coordinate.
                    else:
                        coordinate = (rows[random.randint(0, len(rows) - 1)], random.randint(1, len(columns)))
                    # Raise exception if input coordinate has already been targetted.
                    # Targetting is attempted again.
                    if grid[coordinate] == Battlefield.states[6] or grid[coordinate] == Battlefield.states[7] or grid[
                        coordinate] == Battlefield.states[9]:
                        raise TargettedCoordinateException
                    break
                except Exception as e:
                    pass

            shot += 1
            self.targetted_coordinates.append(coordinate)

            if not grid[coordinate]:
                grid[coordinate] = Battlefield.states[6]
                self.display_targetting_results(player, coordinate, False)
                last_shot_hit = False

            elif grid[coordinate] == Battlefield.states[5]:
                grid[coordinate] = Battlefield.states[7]
                print(new_line * 2 + TargettingStrings.incoming_complete + new_line)
                battlefield.display()
                print(new_line * 2 + Formatting.line_wrap3(
                    TargettingStrings.ship_hit_str.format(
                        str(coordinate))) + new_line * 2)
                last_shot_hit = True
                self.hit_coordinates.append(coordinate)
                self.active_targets.append(coordinate)

                for ship in player.fleet.values():
                    if coordinate in ship.coordinates:
                        ship.check_sunk()
                        if ship.sunk:
                            for coord in ship.coordinates:
                                self.active_targets.remove(coord)
                if player.check_fleet_sunk():
                    break

                # Print a statement indicating that another turn is coming (and wait for continue inputs).
                input(continue_string)
                print(Formatting.line_str2 + new_line * 2 + TargettingStrings.incoming_str)
                input(continue_string)
