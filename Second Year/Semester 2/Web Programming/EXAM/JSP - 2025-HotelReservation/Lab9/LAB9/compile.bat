echo off
cd /d "C:\Users\teodo\Desktop\Teo\Facultate\Anul 2\Semestru 2\Web\Lab9\LAB9"

echo Compiling DBUtil...
javac -cp "target\XoGameApp\WEB-INF\lib\mysql-connector-j-8.2.0.jar" -d "target\XoGameApp\WEB-INF\classes" "src\main\java\com\example\util\DBUtil.java"

echo Compiling LoginServlet...
javac -cp "target\XoGameApp\WEB-INF\lib\mysql-connector-j-8.2.0.jar;C:\apache-tomcat-9.0.105\lib\servlet-api.jar;target\XoGameApp\WEB-INF\classes" -d "target\XoGameApp\WEB-INF\classes" "src\main\java\com\example\servlets\LoginServlet.java"

echo Compiling LogoutServlet...
javac -cp "target\XoGameApp\WEB-INF\lib\mysql-connector-j-8.2.0.jar;C:\apache-tomcat-9.0.105\lib\servlet-api.jar;target\XoGameApp\WEB-INF\classes" -d "target\XoGameApp\WEB-INF\classes" "src\main\java\com\example\servlets\LogoutServlet.java"

echo Compiling GameServlet...
javac -cp "target\XoGameApp\WEB-INF\lib\mysql-connector-j-8.2.0.jar;C:\apache-tomcat-9.0.105\lib\servlet-api.jar;target\XoGameApp\WEB-INF\classes" -d "target\XoGameApp\WEB-INF\classes" "src\main\java\com\example\servlets\GameServlet.java"

echo Compiling ResetServlet...
javac -cp "target\XoGameApp\WEB-INF\lib\mysql-connector-j-8.2.0.jar;C:\apache-tomcat-9.0.105\lib\servlet-api.jar;target\XoGameApp\WEB-INF\classes" -d "target\XoGameApp\WEB-INF\classes" "src\main\java\com\example\servlets\ResetServlet.java"

echo Done!
pause 