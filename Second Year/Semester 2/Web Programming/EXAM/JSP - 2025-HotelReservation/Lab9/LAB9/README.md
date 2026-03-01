# JSP Web Application Technology Stack & Setup Guide

## Technology Stack

### Core Technologies
- **Java 8+** with Servlet API 3.1+
- **JSP (JavaServer Pages)** for dynamic web content
- **SQL Server** for database (LocalDB or SQL Server Express on Windows)
- **Apache Tomcat 9.0+** as servlet container
- **Maven 3.6+** for project management and dependencies
- **Sessions** for user state management

### JSP Features Used
- **Servlets** for request handling and business logic
- **JSP pages** for view layer presentation
- **JDBC** with PreparedStatements for database operations
- **HttpSession** for user authentication and state tracking
- **RequestDispatcher** for page forwarding and redirects
- **Built-in validation** and error handling

### Database Architecture
- **SQL Server** as primary database
- **Multi-table structure** with proper relationships
- **Manual SQL schema creation** and data insertion
- **Sample data insertion** via SQL scripts

## Basic Application Structure

### Core Files
```
project/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           ├── servlets/
│       │           │   ├── LoginServlet.java     # Authentication logic
│       │           │   ├── HomeServlet.java      # Main application logic
│       │           └── YourEntityServlet.java # Entity-specific functionality
│       │           └── util/
│       │               └── DBUtil.java           # Database connection utility
│       └── webapp/
│           ├── login.jsp                         # Login interface
│           ├── home.jsp                          # Main application page
│           ├── your-entity.jsp                   # Entity-specific pages
│           └── WEB-INF/
│               └── web.xml                       # Servlet configuration
├── pom.xml                                       # Maven dependencies
├── setup_database.sql                            # Database schema and sample data
└── compile.bat                                   # Windows compilation script
```

## Database Setup Process

### Database Connection Utility (src/main/java/com/example/util/DBUtil.java)
```java
package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=your_app_db;trustServerCertificate=true;";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "yourpassword";

    static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQL Server JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
```

### Servlet Configuration (src/main/webapp/WEB-INF/web.xml)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="
           http://xmlns.jcp.org/xml/ns/javaee 
           http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <welcome-file-list>
        <welcome-file>login.jsp</welcome-file>
    </welcome-file-list>

    <servlet>
        <servlet-name>LoginServlet</servlet-name>
        <servlet-class>com.example.servlets.LoginServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>LoginServlet</servlet-name>
        <url-pattern>/login</url-pattern>
    </servlet-mapping>

    <servlet>
        <servlet-name>HomeServlet</servlet-name>
        <servlet-class>com.example.servlets.HomeServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>HomeServlet</servlet-name>
        <url-pattern>/home</url-pattern>
    </servlet-mapping>
</web-app>
```

### Maven Configuration (pom.xml)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.example</groupId>
    <artifactId>jsp-web-app</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>
    
    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
            <scope>provided</scope>
        </dependency>
        
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>jsp-api</artifactId>
            <version>2.2</version>
            <scope>provided</scope>
        </dependency>
        
        <dependency>
            <groupId>com.microsoft.sqlserver</groupId>
            <artifactId>mssql-jdbc</artifactId>
            <version>12.4.0.jre8</version>
        </dependency>
    </dependencies>
    
    <build>
        <finalName>jsp-web-app</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>8</source>
                    <target>8</target>
                </configuration>
            </plugin>
            
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.2.3</version>
                <configuration>
                    <webXml>src\main\webapp\WEB-INF\web.xml</webXml>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

## Deployment Workflow

### Complete Setup Process
1. **Install prerequisites**: Java 8+, Maven 3.6+, Apache Tomcat 9.0+, SQL Server
2. **Start SQL Server**: Ensure SQL Server service is running
3. **Create database**: Run `setup_database.sql` in SQL Server Management Studio
4. **Compile project**: Run `compile.bat` or `mvn clean package`
5. **Deploy to Tomcat**: Copy generated WAR file to Tomcat webapps directory
6. **Start Tomcat**: Access application at `http://localhost:8080/jsp-web-app`

### Prerequisites
- **Java Development Kit (JDK) 8+** installed
- **Apache Maven 3.6+** installed and configured in PATH
- **Apache Tomcat 9.0+** installed
- **SQL Server** (LocalDB, Express, or full version) running on port 1433

### Windows Compilation Script (compile.bat)
```batch
@echo off
echo Compiling JSP Web Application...

echo Checking Maven installation...
mvn --version
if errorlevel 1 (
    echo Error: Maven is not installed or not in PATH
    pause
    exit /b 1
)

echo Cleaning previous builds...
mvn clean

echo Compiling and packaging application...
mvn package

if errorlevel 1 (
    echo Error during compilation. Please check the output above.
    pause
    exit /b 1
)

echo Build successful! WAR file created in target/ directory
echo Copy target/jsp-web-app.war to your Tomcat webapps directory
echo Access the application at: http://localhost:8080/jsp-web-app
pause
```

## Key Technical Patterns

### Servlet Pattern (LoginServlet.java)
```java
package com.example.servlets;

import com.example.util.DBUtil;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String credential = req.getParameter("namedm");

        if (username == null || credential == null || 
            username.isEmpty() || credential.isEmpty()) {
            req.setAttribute("error", "Please enter both username and credential.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM authors WHERE name = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String docs = rs.getString("documentList");
                String movies = rs.getString("movieList");
                
                boolean valid = false;
                if (docs != null && docs.contains(credential)) valid = true;
                if (movies != null && movies.contains(credential)) valid = true;
                
                if (valid) {
                    HttpSession session = req.getSession();
                    session.setAttribute("username", username);
                    session.setAttribute("userId", rs.getInt("id"));
                    resp.sendRedirect("home");
                    return;
                } else {
                    req.setAttribute("error", "Invalid credentials.");
                    req.getRequestDispatcher("/login.jsp").forward(req, resp);
                    return;
                }
            } else {
                req.setAttribute("error", "User not found.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
                return;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}
```

### Authentication Check Pattern (HomeServlet.java)
```java
package com.example.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.sendRedirect("login");
            return;
        }

        String username = (String) session.getAttribute("username");
        req.setAttribute("username", username);
        req.getRequestDispatcher("/home.jsp").forward(req, resp);
    }
}
```

### Database Operations Pattern
```java
// SELECT operations
try (Connection conn = DBUtil.getConnection()) {
    PreparedStatement ps = conn.prepareStatement(
        "SELECT * FROM your_entity WHERE created_date BETWEEN ? AND ?");
    ps.setString(1, "2025-06-01");
    ps.setString(2, "2025-06-30");
    ResultSet rs = ps.executeQuery();
    
    while (rs.next()) {
        // Process results
    }
}


## Development Checklist

 **Sample data in ALL tables with June 2025 dates**  
 **Minimalistic design - no unnecessary features**  
 **Structured approach - organized file structure**  
 **Requirements focused - only what's asked for**  
 **Simple implementation - no overcomplication**

## Important Development Guidelines

**You must always have some inserts in the database for each entity, and for any date fields, you must use year 2025, June.**

**Please keep it simple and minimalistic.**

**Do not show other unuseful things and information that is not required in the subject. Keep it simple!**

**You must use minor professional CSS and styling and have a structured, arranged approach. Do not add extra unessential information that is not asked. Keep it as simple as you can while fulfilling all the requirements.**

**Most important is to fulfill the requirements while keeping it simple and do not overcomplicate, it's only a basic app. Respect all the requirements.**

This technology stack provides a solid foundation for JSP web applications with SQL Server backend on Windows development environment that can be quickly adapted to any problem domain while maintaining professional standards and simplicity. 