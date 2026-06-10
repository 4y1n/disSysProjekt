# Run the GUI module using Maven (PowerShell-friendly)
# Usage: .\run-gui.ps1
# This will call: mvn -pl gui -am javafx:run
# It avoids manual quoting of --module-path and other pitfalls.

Param()

Write-Host "Starting gui module with Maven: mvn -pl gui -am javafx:run"
try {
    & mvn -pl gui -am javafx:run
    $exit = $LASTEXITCODE
    if ($exit -ne 0) {
        Write-Host "Maven exited with code $exit"
        exit $exit
    }
} catch {
    Write-Error "Failed to start Maven: $_"
    exit 1
}

