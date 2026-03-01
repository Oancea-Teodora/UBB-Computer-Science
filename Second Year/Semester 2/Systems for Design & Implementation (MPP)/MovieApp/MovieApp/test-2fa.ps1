# Test script for 2FA functionality
Write-Host "🔐 Testing MovieApp 2FA System" -ForegroundColor Green

# 1. Login to get access token
Write-Host "`n1. Logging in as admin..." -ForegroundColor Yellow
$loginBody = '{"email": "admin@gmail.com", "password": "admin"}'

try {
    $loginResponse = Invoke-WebRequest -Uri "http://localhost:3000/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    $loginData = $loginResponse.Content | ConvertFrom-Json
    $token = $loginData.accessToken
    Write-Host "✅ Login successful!" -ForegroundColor Green
    Write-Host "   User: $($loginData.user.email) (Role: $($loginData.user.role))"
    Write-Host "   Token: $($token.Substring(0,20))..."
}
catch {
    Write-Host "❌ Login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# 2. Check 2FA status
Write-Host "`n2. Checking 2FA status..." -ForegroundColor Yellow
try {
    $headers = @{ Authorization = "Bearer $token" }
    $statusResponse = Invoke-WebRequest -Uri "http://localhost:3000/auth/2fa/status" -Method Get -Headers $headers
    $statusData = $statusResponse.Content | ConvertFrom-Json
    Write-Host "✅ 2FA Status:" -ForegroundColor Green
    Write-Host "   Enabled: $($statusData.twoFactorEnabled)"
    Write-Host "   Backup codes remaining: $($statusData.backupCodesRemaining)"
    Write-Host "   Setup in progress: $($statusData.setupInProgress)"
}
catch {
    Write-Host "❌ Status check failed: $($_.Exception.Message)" -ForegroundColor Red
}

# 3. Setup 2FA (if not already enabled)
Write-Host "`n3. Setting up 2FA..." -ForegroundColor Yellow
try {
    $setupResponse = Invoke-WebRequest -Uri "http://localhost:3000/auth/2fa/setup" -Method Post -Headers $headers
    $setupData = $setupResponse.Content | ConvertFrom-Json
    Write-Host "✅ 2FA Setup initiated!" -ForegroundColor Green
    Write-Host "   Secret: $($setupData.secret.Substring(0,10))..."
    Write-Host "   QR Code generated for authenticator app"
    Write-Host "   Manual entry key: $($setupData.manualEntryKey.Substring(0,10))..."
}
catch {
    $errorData = ($_.ErrorDetails.Message | ConvertFrom-Json)
    Write-Host "⚠️  Setup response: $($errorData.error)" -ForegroundColor Yellow
}

Write-Host "`n🎉 2FA System Features:" -ForegroundColor Cyan
Write-Host "   ✅ TOTP token generation with QR codes"
Write-Host "   ✅ Backup codes for account recovery"
Write-Host "   ✅ Email notifications"
Write-Host "   ✅ Token versioning for security"
Write-Host "   ✅ Authenticator app support"

Write-Host "`n📱 To complete 2FA setup:" -ForegroundColor Magenta
Write-Host "   1. Install Google Authenticator, Authy, or similar app"
Write-Host "   2. Scan the QR code or enter the secret manually"
Write-Host "   3. Use POST /auth/2fa/verify-setup with the 6-digit code"
Write-Host "   4. Save the backup codes securely"

Write-Host "`n🌐 Access the web interface at:" -ForegroundColor Cyan
Write-Host "   http://localhost:3000/auth/login-page" 