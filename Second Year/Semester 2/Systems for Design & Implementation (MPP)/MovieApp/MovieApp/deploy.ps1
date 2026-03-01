# MovieApp Infrastructure as Code Deployment Script (PowerShell)
# This script automates the deployment of the entire MovieApp stack on Render
# Similar to docker-compose up but for cloud infrastructure

param(
    [switch]$Help
)

if ($Help) {
    Write-Host "MovieApp Infrastructure as Code Deployment" -ForegroundColor Cyan
    Write-Host "Usage: .\deploy.ps1" -ForegroundColor White
    Write-Host "This script deploys the complete MovieApp infrastructure using Render Blueprint" -ForegroundColor Gray
    exit 0
}

# Configuration
$RepoUrl = "https://github.com/Oancea-Teodora/MovieManagementApp.git"
$RenderDashboard = "https://dashboard.render.com"

function Write-ColorText {
    param([string]$Text, [string]$Color = "White")
    Write-Host $Text -ForegroundColor $Color
}

function Write-Header {
    Write-ColorText "🚀 MovieApp Infrastructure as Code Deployment" "Cyan"
    Write-ColorText "=============================================" "Cyan"
    Write-Host ""
}

function Show-Infrastructure-Config {
    Write-ColorText "📋 Infrastructure Configuration:" "Blue"
    Write-Host "  - PostgreSQL Database (Free tier)"
    Write-Host "  - Backend API Service (Docker-based)"
    Write-Host "  - Frontend Web Service (Docker-based)"
    Write-Host "  - Auto-scaling: 1-1 instances per service"
    Write-Host "  - Region: Frankfurt"
    Write-Host "  - Health checks enabled"
    Write-Host ""
}

function Test-RenderCLI {
    Write-ColorText "🔍 Checking Render CLI..." "Blue"
    try {
        $null = Get-Command render -ErrorAction Stop
        Write-ColorText "✅ Render CLI found" "Green"
        return $true
    }
    catch {
        Write-ColorText "❌ Render CLI not found" "Red"
        Write-ColorText "💡 Install it with: npm install -g @render/cli" "Yellow"
        Write-ColorText "   Then authenticate with: render auth" "Yellow"
        return $false
    }
}

function Test-Blueprint {
    Write-ColorText "🔍 Validating Blueprint..." "Blue"
    
    if (-not (Test-Path "render.yaml")) {
        Write-ColorText "❌ render.yaml not found" "Red"
        exit 1
    }
    
    $content = Get-Content "render.yaml" -Raw
    if ($content -match "movieapp-database" -and 
        $content -match "movieapp-backend" -and 
        $content -match "movieapp-frontend") {
        Write-ColorText "✅ Blueprint validation passed" "Green"
    }
    else {
        Write-ColorText "❌ Blueprint validation failed - missing required services" "Red"
        exit 1
    }
}

function Deploy-Infrastructure {
    Write-ColorText "🏗️ Deploying Infrastructure..." "Blue"
    
    if (Test-RenderCLI) {
        Write-ColorText "🔄 Deploying with Render CLI..." "Yellow"
        try {
            render blueprint deploy
        }
        catch {
            Write-ColorText "⚠️ CLI deployment failed, showing manual instructions..." "Yellow"
            Show-Manual-Instructions
        }
    }
    else {
        Show-Manual-Instructions
    }
}

function Show-Manual-Instructions {
    Write-ColorText "📋 Manual Deployment Instructions:" "Yellow"
    Write-Host "1. Go to $RenderDashboard"
    Write-Host "2. Click 'New +' -> 'Blueprint'"
    Write-Host "3. Connect to your GitHub repository: $RepoUrl"
    Write-Host "4. Select 'render.yaml' as your blueprint file"
    Write-Host "5. Click 'Apply' to deploy all services"
    Write-Host ""
    Write-ColorText "🔗 Direct Blueprint URL:" "Green"
    Write-Host "$RenderDashboard/create?type=blueprint`&repo=$RepoUrl"
}

function Show-Deployment-Status {
    Write-ColorText "📊 Expected Deployment Results:" "Blue"
    Write-Host ""
    Write-ColorText "Database Service:" "Green"
    Write-Host "  - Name: movieapp-database"
    Write-Host "  - Type: PostgreSQL"
    Write-Host "  - Status: Will be provisioned automatically"
    Write-Host ""
    Write-ColorText "Backend Service:" "Green"
    Write-Host "  - Name: movieapp-backend"
    Write-Host "  - URL: https://movieapp-backend.onrender.com"
    Write-Host "  - Health Check: /health endpoint"
    Write-Host "  - Features: JWT auth, Socket.IO, File upload"
    Write-Host ""
    Write-ColorText "Frontend Service:" "Green"
    Write-Host "  - Name: movieapp-frontend"
    Write-Host "  - URL: https://movieapp-frontend.onrender.com"
    Write-Host "  - Health Check: /health endpoint"
    Write-Host "  - Features: React SPA, Material UI, Real-time updates"
    Write-Host ""
}

function Show-Post-Deployment {
    Write-ColorText "🎯 Post-Deployment Verification:" "Blue"
    Write-Host ""
    Write-Host "1. 🔍 Monitor deployment progress in Render dashboard"
    Write-Host "2. ✅ Test health endpoints:"
    Write-Host "   - Backend: https://movieapp-backend.onrender.com/health"
    Write-Host "   - Frontend: https://movieapp-frontend.onrender.com/health"
    Write-Host "3. 🧪 Test application functionality:"
    Write-Host "   - User registration/login"
    Write-Host "   - Create/edit movies and directors"
    Write-Host "   - Real-time updates (Socket.IO)"
    Write-Host "   - File upload/download"
    Write-Host "4. 📊 Check database connectivity"
    Write-Host "5. 🔒 Verify HTTPS is enabled (automatic on Render)"
    Write-Host ""
    Write-ColorText "🎉 Infrastructure as Code deployment complete!" "Green"
}

function Main {
    Write-Header
    Show-Infrastructure-Config
    
    Write-ColorText "Starting deployment workflow..." "Blue"
    Write-Host ""
    
    # Step 1: Validate blueprint
    Test-Blueprint
    Write-Host ""
    
    # Step 2: Deploy infrastructure
    Deploy-Infrastructure
    Write-Host ""
    
    # Step 3: Show expected results
    Show-Deployment-Status
    Write-Host ""
    
    # Step 4: Post-deployment instructions
    Show-Post-Deployment
    
    Write-Host ""
    Write-ColorText "🚀 Deployment script completed successfully!" "Green"
    Write-ColorText "💡 This demonstrates Infrastructure as Code principles:" "Yellow"
    Write-Host "   - Declarative configuration (render.yaml)"
    Write-Host "   - Automated provisioning"
    Write-Host "   - Service dependencies"
    Write-Host "   - Environment management"
    Write-Host "   - Scalability configuration"
}

# Run the deployment
Main 