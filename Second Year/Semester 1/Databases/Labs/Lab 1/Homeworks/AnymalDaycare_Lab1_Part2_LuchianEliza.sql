alter table Expenses
add FK_Expenses_Room int

ALTER TABLE Expenses
ADD FOREIGN KEY (FK_Expenses_Room) REFERENCES Rooms(room_number);