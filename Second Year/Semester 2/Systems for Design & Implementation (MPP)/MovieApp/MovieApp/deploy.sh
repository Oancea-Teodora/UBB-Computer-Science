#!/bin/bash

# MovieApp Infrastructure as Code Deployment Script
# This script automates the deployment of the entire MovieApp stack on Render
# Similar to docker-compose up but for cloud infrastructure

set -e

echo "🚀 MovieApp Infrastructure as Code Deployment"
echo "============================================="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
REPO_URL="https://github.com/Oancea-Teodora/MovieManagementApp.git"
RENDER_API="https://api.render.com/v1"

echo -e "${BLUE}📋 Infrastructure Configuration:${NC}"
echo "  - PostgreSQL Database (Free tier)"
echo "  - Backend API Service (Docker-based)"
echo "  - Frontend Web Service (Docker-based)"
echo "  - Auto-scaling: 1-1 instances per service"
echo "  - Region: Frankfurt"
echo "  - Health checks enabled"
echo ""

# Function to check if render CLI is installed
check_render_cli() {
    if command -v render &> /dev/null; then
        echo -e "${GREEN}✅ Render CLI found${NC}"
        return 0
    else
        echo -e "${RED}❌ Render CLI not found${NC}"
        echo -e "${YELLOW}💡 Install it with: npm install -g @render/cli${NC}"
        echo -e "${YELLOW}   Then authenticate with: render auth${NC}"
        return 1
    fi
}

# Function to validate render.yaml
validate_blueprint() {
    echo -e "${BLUE}🔍 Validating Blueprint...${NC}"
    
    if [ ! -f "render.yaml" ]; then
        echo -e "${RED}❌ render.yaml not found${NC}"
        exit 1
    fi
    
    # Check if the blueprint has required services
    if grep -q "movieapp-database" render.yaml && \
       grep -q "movieapp-backend" render.yaml && \
       grep -q "movieapp-frontend" render.yaml; then
        echo -e "${GREEN}✅ Blueprint validation passed${NC}"
    else
        echo -e "${RED}❌ Blueprint validation failed - missing required services${NC}"
        exit 1
    fi
}

# Function to deploy infrastructure
deploy_infrastructure() {
    echo -e "${BLUE}🏗️  Deploying Infrastructure...${NC}"
    
    if check_render_cli; then
        echo -e "${YELLOW}🔄 Deploying with Render CLI...${NC}"
        render blueprint deploy
    else
        echo -e "${YELLOW}📋 Manual Deployment Instructions:${NC}"
        echo "1. Go to https://dashboard.render.com"
        echo "2. Click 'New +' -> 'Blueprint'"
        echo "3. Connect to your GitHub repository: $REPO_URL"
        echo "4. Select 'render.yaml' as your blueprint file"
        echo "5. Click 'Apply' to deploy all services"
        echo ""
        echo -e "${GREEN}🔗 Direct Blueprint URL:${NC}"
        echo "https://dashboard.render.com/create?type=blueprint&repo=$REPO_URL"
    fi
}

# Function to show deployment status
show_status() {
    echo -e "${BLUE}📊 Expected Deployment Results:${NC}"
    echo ""
    echo -e "${GREEN}Database Service:${NC}"
    echo "  - Name: movieapp-database"
    echo "  - Type: PostgreSQL"
    echo "  - Status: Will be provisioned automatically"
    echo ""
    echo -e "${GREEN}Backend Service:${NC}"
    echo "  - Name: movieapp-backend"
    echo "  - URL: https://movieapp-backend.onrender.com"
    echo "  - Health Check: /health endpoint"
    echo "  - Features: JWT auth, Socket.IO, File upload"
    echo ""
    echo -e "${GREEN}Frontend Service:${NC}"
    echo "  - Name: movieapp-frontend"
    echo "  - URL: https://movieapp-frontend.onrender.com"
    echo "  - Health Check: /health endpoint"
    echo "  - Features: React SPA, Material UI, Real-time updates"
    echo ""
}

# Function to show post-deployment steps
show_post_deployment() {
    echo -e "${BLUE}🎯 Post-Deployment Verification:${NC}"
    echo ""
    echo "1. 🔍 Monitor deployment progress in Render dashboard"
    echo "2. ✅ Test health endpoints:"
    echo "   - Backend: https://movieapp-backend.onrender.com/health"
    echo "   - Frontend: https://movieapp-frontend.onrender.com/health"
    echo "3. 🧪 Test application functionality:"
    echo "   - User registration/login"
    echo "   - Create/edit movies and directors"
    echo "   - Real-time updates (Socket.IO)"
    echo "   - File upload/download"
    echo "4. 📊 Check database connectivity"
    echo "5. 🔒 Verify HTTPS is enabled (automatic on Render)"
    echo ""
    echo -e "${GREEN}🎉 Infrastructure as Code deployment complete!${NC}"
}

# Main deployment workflow
main() {
    echo -e "${BLUE}Starting deployment workflow...${NC}"
    echo ""
    
    # Step 1: Validate blueprint
    validate_blueprint
    echo ""
    
    # Step 2: Deploy infrastructure
    deploy_infrastructure
    echo ""
    
    # Step 3: Show expected results
    show_status
    echo ""
    
    # Step 4: Post-deployment instructions
    show_post_deployment
    
    echo ""
    echo -e "${GREEN}🚀 Deployment script completed successfully!${NC}"
    echo -e "${YELLOW}💡 This demonstrates Infrastructure as Code principles:${NC}"
    echo "   - Declarative configuration (render.yaml)"
    echo "   - Automated provisioning"
    echo "   - Service dependencies"
    echo "   - Environment management"
    echo "   - Scalability configuration"
}

# Run the deployment
main "$@" 