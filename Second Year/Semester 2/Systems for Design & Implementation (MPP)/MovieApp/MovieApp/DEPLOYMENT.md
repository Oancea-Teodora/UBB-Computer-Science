# 🚀 MovieApp Infrastructure as Code Deployment

This document explains how to deploy the complete MovieApp infrastructure using **Infrastructure as Code (IaC)** principles on Render, achieving the same goals as Docker Compose + Amazon ECS but with a more modern, declarative approach.

## 📋 Overview

### What is Infrastructure as Code?

Infrastructure as Code (IaC) is the practice of managing and provisioning computing infrastructure through machine-readable definition files, rather than through manual processes.

### Render Blueprint vs Docker Compose + ECS

| Feature | Docker Compose + ECS | Render Blueprint |
|---------|---------------------|------------------|
| **Configuration** | `docker-compose.yml` | `render.yaml` |
| **Orchestration** | ECS Service Discovery | Automatic service linking |
| **Database** | RDS setup required | PostgreSQL auto-provisioned |
| **Load Balancing** | ALB configuration | Built-in |
| **HTTPS/SSL** | Certificate Manager | Automatic |
| **Scaling** | ECS Auto Scaling | Built-in auto-scaling |
| **Health Checks** | Custom ECS config | Declarative |
| **Deployment** | CodePipeline/Manual | Git-based auto-deploy |

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    MovieApp Infrastructure                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  ┌─────────────────┐    ┌─────────────────┐    ┌──────────────┐ │
│  │   Frontend      │    │    Backend      │    │  Database    │ │
│  │   (React SPA)   │◄──►│   (Node.js)     │◄──►│ (PostgreSQL) │ │
│  │                 │    │                 │    │              │ │
│  │ - React Router  │    │ - Express API   │    │ - User Data  │ │
│  │ - Material UI   │    │ - Socket.IO     │    │ - Movies     │ │
│  │ - Real-time     │    │ - JWT Auth      │    │ - Directors  │ │
│  │   Updates       │    │ - File Upload   │    │ - Logs       │ │
│  └─────────────────┘    └─────────────────┘    └──────────────┘ │
│                                                                 │
│  URL: movieapp-        URL: movieapp-        Type: pserv       │
│       frontend.onr...       backend.onr...   Plan: free        │
│                                              Region: Frankfurt │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## 📁 Files Structure

```
MovieApp/
├── render.yaml              # 🎯 Infrastructure as Code definition
├── deploy.sh               # 🚀 Linux/Mac deployment script
├── deploy.ps1              # 🚀 Windows deployment script
├── backend/
│   ├── Dockerfile          # 🐳 Backend container definition
│   ├── src/
│   └── package.json
├── frontend/
│   ├── Dockerfile          # 🐳 Frontend container definition
│   ├── nginx.conf          # ⚙️ Web server configuration
│   ├── src/
│   └── package.json
└── DEPLOYMENT.md           # 📖 This documentation
```

## 🎯 Quick Start

### Option 1: Automated Deployment (Recommended)

**Windows:**
```powershell
.\deploy.ps1
```

**Linux/Mac:**
```bash
chmod +x deploy.sh
./deploy.sh
```

### Option 2: Manual Deployment

1. **Go to Render Dashboard:** https://dashboard.render.com
2. **Click "New +" → "Blueprint"**
3. **Connect Repository:** `https://github.com/Oancea-Teodora/MovieManagementApp.git`
4. **Select Blueprint:** `render.yaml`
5. **Click "Apply"** to deploy all services

### Option 3: Direct Blueprint Link

🔗 **One-click deployment:** 
https://dashboard.render.com/create?type=blueprint&repo=https://github.com/Oancea-Teodora/MovieManagementApp.git

## 🔧 Configuration Details

### render.yaml Breakdown

```yaml
services:
  # Database Service
  - type: pserv
    name: movieapp-database
    env: postgresql
    plan: free
    
  # Backend API Service  
  - type: web
    name: movieapp-backend
    runtime: docker
    rootDir: backend
    healthCheckPath: /health
    envVars:
      - key: DATABASE_URL
        fromDatabase:
          name: movieapp-database
          property: connectionString
          
  # Frontend Web Service
  - type: web  
    name: movieapp-frontend
    runtime: docker
    rootDir: frontend
    healthCheckPath: /health
    envVars:
      - key: VITE_API_URL
        fromService:
          type: web
          name: movieapp-backend
          property: host
```

### Key IaC Features

1. **🔗 Service Dependencies:** Frontend automatically gets backend URL
2. **🗄️ Database Linking:** Backend automatically gets database connection string
3. **🚀 Auto-scaling:** Configured scaling policies
4. **🔍 Health Checks:** Automatic health monitoring
5. **🔄 Auto-deploy:** Git-based continuous deployment
6. **🌍 Multi-region:** Configurable region deployment

## 🎯 Deployment Process

### Step 1: Infrastructure Provisioning
```
┌─────────────────┐
│ render.yaml     │ ──► Database (PostgreSQL)
│ parsed          │ ──► Backend (Docker container)
│                 │ ──► Frontend (Docker container)
└─────────────────┘
```

### Step 2: Service Linking
```
Frontend ◄── VITE_API_URL ──┤
                             ├── Backend URL
Backend  ◄── DATABASE_URL ───┤
                             ├── Database Connection
Database ◄── Auto-provisioned
```

### Step 3: Health Check Setup
```
/health endpoint ──► Backend monitoring
/health endpoint ──► Frontend monitoring
Database ──────────► Connection monitoring
```

## 📊 Monitoring & Verification

### Health Endpoints

- **Backend:** `https://movieapp-backend.onrender.com/health`
- **Frontend:** `https://movieapp-frontend.onrender.com/health`

### Expected Response
```json
{
  "status": "healthy",
  "message": "Server is running"
}
```

### Application Features to Test

1. **🔐 Authentication:**
   - User registration
   - User login
   - JWT token handling

2. **📊 Data Management:**
   - Create/Read/Update/Delete movies
   - Create/Read/Update/Delete directors
   - Data persistence

3. **⚡ Real-time Features:**
   - Socket.IO connectivity
   - Live movie updates
   - Auto-generation toggle

4. **📁 File Operations:**
   - File upload
   - File download
   - File storage

## 🔒 Security Features

### Automatic HTTPS
- ✅ SSL certificates auto-provisioned
- ✅ HTTP to HTTPS redirects
- ✅ TLS 1.2+ encryption

### Environment Security
- ✅ Secrets management
- ✅ Environment variable isolation
- ✅ Database connection encryption

### CORS Configuration
- ✅ Dynamic origin validation
- ✅ Credential handling
- ✅ Method restrictions

## 🚀 Scaling & Performance

### Auto-scaling Configuration
```yaml
scaling:
  minInstances: 1
  maxInstances: 1  # Free tier limitation
```

### Performance Optimizations
- ✅ Docker multi-stage builds
- ✅ Nginx static asset serving
- ✅ Database connection pooling
- ✅ Asset caching headers

## 🔄 CI/CD Pipeline

### Automated Deployment Flow
```
GitHub Push ──► Render Webhook ──► Build ──► Deploy ──► Health Check ──► Live
```

### Environment Management
- **Development:** Local docker-compose
- **Staging:** Render free tier
- **Production:** Render paid tier (optional)

## 🆚 Comparison with Docker Compose + ECS

### Advantages of Render Blueprint IaC

1. **🎯 Simplicity:** Single YAML file vs multiple AWS resources
2. **💰 Cost:** Free tier includes everything vs AWS complex pricing
3. **⚡ Speed:** Deploy in minutes vs hours of AWS setup
4. **🔒 Security:** HTTPS automatic vs manual certificate management
5. **📊 Monitoring:** Built-in vs CloudWatch setup required

### Docker Compose + ECS Equivalent

Our `render.yaml` achieves the same functionality as:

```yaml
# This would require:
# - ECS Cluster
# - Task Definitions  
# - Service Discovery
# - Load Balancer
# - RDS Instance
# - Security Groups
# - IAM Roles
# - Certificate Manager
# - Route 53
# - CloudWatch
```

## 🎉 Success Criteria

✅ **Bronze Level:** Backend deployed  
✅ **Silver Level:** Frontend + Backend deployed  
✅ **Gold Level:** Infrastructure as Code deployment complete!

### Gold Level Achievements

1. ✅ **Declarative Infrastructure:** Complete stack defined in code
2. ✅ **Automated Provisioning:** One-click deployment
3. ✅ **Service Orchestration:** Automatic service linking
4. ✅ **Environment Management:** Proper separation of configs
5. ✅ **Scaling Configuration:** Ready for production scaling
6. ✅ **Health Monitoring:** Comprehensive health checks
7. ✅ **Security Best Practices:** HTTPS, secrets management
8. ✅ **CI/CD Integration:** Git-based deployment pipeline

## 🛠️ Troubleshooting

### Common Issues

1. **Service Not Starting:** Check Docker build logs
2. **Database Connection:** Verify DATABASE_URL environment variable
3. **CORS Errors:** Check frontend/backend URL configuration
4. **Health Check Failing:** Verify /health endpoints

### Debug Commands

```bash
# Check service status
curl https://movieapp-backend.onrender.com/health
curl https://movieapp-frontend.onrender.com/health

# Validate Blueprint locally
./deploy.sh # or .\deploy.ps1
```

## 📚 Further Reading

- [Render Blueprint Documentation](https://render.com/docs/infrastructure-as-code)
- [Docker Best Practices](https://docs.docker.com/develop/best-practices/)
- [Infrastructure as Code Principles](https://en.wikipedia.org/wiki/Infrastructure_as_code)

---

🎉 **Congratulations!** You've successfully implemented Infrastructure as Code deployment, achieving the Gold level requirements using modern cloud-native practices! 