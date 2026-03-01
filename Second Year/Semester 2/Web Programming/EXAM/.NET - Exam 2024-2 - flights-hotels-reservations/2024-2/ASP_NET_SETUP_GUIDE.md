# ASP.NET Web Application Technology Stack & Setup Guide

## Technology Stack

### Core Technologies
- **ASP.NET 8.0** with MVC pattern
- **Entity Framework Core 9.0.6** for ORM
- **SQL Server** for database (LocalDB or SQL Server Express)
- **C#** with modern language features
- **HTML/CSS/JavaScript** for frontend
- **Sessions** for user state management

### ASP.NET Features Used
- **Entity Framework Core** with Code-First migrations
- **Dependency Injection** for service management
- **Model-View-Controller (MVC)** architecture
- **Session Management** for user state tracking
- **JSON Serialization** for complex session objects
- **Built-in validation** and error handling

### Database Architecture
- **SQL Server** as primary database
- **Entity Framework Code-First** approach
- **Multi-table structure** with proper relationships
- **Auto-generated migrations** for schema management
- **Sample data insertion** via migration seeding

## Basic Application Structure

### Core Files
```
project/
├── Controllers/
│   ├── HomeController.cs       # Main application logic and session management
│   └── YourEntityController.cs # Entity-specific functionality
├── Models/
│   ├── YourEntity.cs           # Your domain entity models
│   └── ErrorViewModel.cs       # Error handling model
├── Data/
│   └── AppDbContext.cs         # Entity Framework database context
├── Helpers/
│   └── SessionHelper.cs        # Session management utilities
├── Views/                      # Razor views
├── Migrations/                 # Entity Framework migrations
├── wwwroot/                    # Static files (CSS, JS, images)
├── Program.cs                  # Application startup configuration
├── appsettings.json            # Configuration settings
└── ProjectName.csproj          # Project file with dependencies
```

## Database Setup Process

### Application Startup Configuration (Program.cs)
```csharp
using YourApp.Data;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllersWithViews();
builder.Services.AddDbContext<AppDbContext>(opts => 
    opts.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));
builder.Services.AddSession();

var app = builder.Build();

if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();
app.UseSession();
app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();
```

### Database Context (Data/AppDbContext.cs)
```csharp
using YourApp.Models;
using Microsoft.EntityFrameworkCore;

namespace YourApp.Data
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> opts) : base(opts) { }

        public DbSet<YourEntity> YourEntities { get; set; }
        
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Seed sample data for June 2025
            modelBuilder.Entity<YourEntity>().HasData(
                new YourEntity { Id = 1, Name = "Sample Item 1", Date = "2025-06-01", Quantity = 10 },
                new YourEntity { Id = 2, Name = "Sample Item 2", Date = "2025-06-05", Quantity = 15 },
                new YourEntity { Id = 3, Name = "Sample Item 3", Date = "2025-06-10", Quantity = 20 }
            );
        }
    }
}
```

### Connection String (appsettings.json)
```json
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=localhost;Database=YourAppDB;TrustServerCertificate=True;Integrated Security=SSPI"
  }
}
```

## Deployment Workflow

### Complete Setup Process
1. **Create project**: `dotnet new mvc -n YourAppName`
2. **Install packages**: Add Entity Framework packages to .csproj
3. **Configure database**: Update connection string in appsettings.json
4. **Create models**: Define your entity classes
5. **Add migration**: `dotnet ef migrations add InitialCreate`
6. **Update database**: `dotnet ef database update`
7. **Run application**: `dotnet run`

### Prerequisites
- **.NET 8.0 SDK** installed
- **SQL Server** (LocalDB, Express, or full version)
- **Entity Framework Tools**: `dotnet tool install --global dotnet-ef`

### Windows Start Script (start.bat)
```batch
@echo off
echo Setting up ASP.NET Application...

echo Applying database migrations...
dotnet ef database update

if errorlevel 1 (
    echo Error applying migrations. Please check your connection string.
    pause
    exit /b 1
)

echo Starting application...
echo Access the application at: https://localhost:5001
dotnet run
```

### Project File Configuration (.csproj)
```xml
<Project Sdk="Microsoft.NET.Sdk.Web">
  <PropertyGroup>
    <TargetFramework>net8.0</TargetFramework>
    <Nullable>enable</Nullable>
    <ImplicitUsings>enable</ImplicitUsings>
  </PropertyGroup>

  <ItemGroup>
    <PackageReference Include="Microsoft.EntityFrameworkCore" Version="9.0.6" />
    <PackageReference Include="Microsoft.EntityFrameworkCore.Design" Version="9.0.6" />
    <PackageReference Include="Microsoft.EntityFrameworkCore.SqlServer" Version="9.0.6" />
    <PackageReference Include="Microsoft.EntityFrameworkCore.Tools" Version="9.0.6" />
  </ItemGroup>
</Project>
```

## Key Technical Patterns

### Model Definition
```csharp
using System.ComponentModel.DataAnnotations;

public class YourEntity
{
    [Key]
    public int Id { get; set; }
    
    [Required]
    public string Name { get; set; }
    
    public string Date { get; set; }
    public int Quantity { get; set; }
}
```

### Controller Pattern
```csharp
public class HomeController : Controller
{
    private readonly AppDbContext _db;

    public HomeController(AppDbContext db)
    {
        _db = db;
    }

    [HttpPost]
    public IActionResult ProcessForm(string param1, string param2)
    {
        if (string.IsNullOrEmpty(param1) || string.IsNullOrEmpty(param2))
        {
            ModelState.AddModelError(string.Empty, "All fields are required.");
            return View("Index");
        }

        HttpContext.Session.SetString("Param1", param1);
        HttpContext.Session.SetString("Param2", param2);

        return RedirectToAction("Index", "YourEntity");
    }
}
```

### Session Helper
```csharp
using System.Text.Json;

public static class SessionHelper
{
    public static void SetObjectAsJson(this ISession session, string key, object value)
    {
        session.SetString(key, JsonSerializer.Serialize(value));
    }

    public static T GetObjectFromJson<T>(this ISession session, string key)
    {
        var value = session.GetString(key);
        return value == null ? default : JsonSerializer.Deserialize<T>(value);
    }
}
```

### Database Operations
```csharp
// SELECT operations
var entities = _db.YourEntities.Where(e => e.Date == date).ToList();

// INSERT operations
var entity = new YourEntity { Name = name, Date = date, Quantity = quantity };
_db.YourEntities.Add(entity);
_db.SaveChanges();

// UPDATE operations
var entity = _db.YourEntities.Find(id);
entity.Quantity--;
_db.SaveChanges();

// DELETE operations
var entity = _db.YourEntities.Find(id);
_db.YourEntities.Remove(entity);
_db.SaveChanges();
```

## Entity Framework Commands
```bash
# Install EF Tools
dotnet tool install --global dotnet-ef

# Add migration
dotnet ef migrations add InitialCreate

# Update database
dotnet ef database update
```

## Common Issues & Solutions

### 1. **Connection String Issues**
   - Ensure SQL Server is running
   - Use `TrustServerCertificate=True` for local development

### 2. **Migration Errors**
   - Delete Migrations folder and recreate: `dotnet ef migrations add InitialCreate`

### 3. **Session Management**
   - Ensure `app.UseSession()` is called in Program.cs
   - Add `builder.Services.AddSession()` in service configuration

## Development Best Practices

- **Use Entity Framework migrations** for database schema management
- **Implement proper validation** on models and controllers
- **Keep sample data realistic** with June 2025 dates
- **Maintain clean separation** between Models, Views, and Controllers
- **Use dependency injection** for database context

## Windows-Specific Configuration

### LocalDB Connection
```json
{
  "ConnectionStrings": {
    "DefaultConnection": "Server=localhost;Database=YourAppDB;TrustServerCertificate=True;Integrated Security=SSPI"
  }
}

## Development Checklist

✅ **Sample data in ALL tables with June 2025 dates**  
✅ **Minimalistic design - no unnecessary features**  
✅ **Structured approach - organized file structure**  
✅ **Requirements focused - only what's asked for**  
✅ **Simple implementation - no overcomplication**

## Important Development Guidelines

**You must always have some inserts in the database for each entity, and for any date fields, you must use year 2025, June.**

**Please keep it simple and minimalistic.**

**Do not show other unuseful things and information that is not required in the subject. Keep it simple!**

**You must use minor professional CSS and styling and have a structured, arranged approach. Do not add extra unessential information that is not asked. Keep it as simple as you can while fulfilling all the requirements.**

**Most important is to fulfill the requirements while keeping it simple and do not overcomplicate, it's only a basic app. Respect all the requirements.**

This technology stack provides a solid foundation for ASP.NET web applications with SQL Server backend that can be quickly adapted to any problem domain while maintaining professional standards and simplicity. 