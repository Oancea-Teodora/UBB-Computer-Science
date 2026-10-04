# Run script for parser
param(
    [string]$file = "sample.src"
)

if (-not (Test-Path "parser.exe")) {
    Write-Host "parser.exe not found. Building first..." -ForegroundColor Yellow
    & .\build.ps1
    if ($LASTEXITCODE -ne 0) {
        exit 1
    }
}

Write-Host "`nRunning parser on $file..." -ForegroundColor Green
Write-Host "================================" -ForegroundColor Gray

& .\parser.exe $file


