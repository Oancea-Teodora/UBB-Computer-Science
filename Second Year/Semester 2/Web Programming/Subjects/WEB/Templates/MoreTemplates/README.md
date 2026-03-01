# Web-Programming

Write a web application in PHP/JSP/ASP.NET which uses the following 2
database tables:
- table Users: id (int), username (string), password (string)
- table Students: id (int), name (string), grade (int)

The user should authenticate prior to using the application. After the
user is succesfully authenticated, a web page with all the students will be displayed. 
In this application, user must be able to:

- add a student   
- update a student    
- delete a student 
- filter students based on their names

--------------------------------------------------------------------------
Obs: Non-functional requirements:
- add different validation logic for input fields
- do not force the user to input an ID for an item if he wants to delete/edit/insert it; 
this should happen automatically (e.g. the user clicks an item from a list, 
and a page/modal prepopulated with the data for that particular item is opened, where the user can edit it)
- add confirmation when the user deletes/cancels an item
- do a bare minimum CSS that at least aligns the various input fields
