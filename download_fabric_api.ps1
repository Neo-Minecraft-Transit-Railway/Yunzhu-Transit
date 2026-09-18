$targetDir = "D:/Minecraft/test/.minecraft/versions"
$versions = @{
    "1.19.2" = "1.19.2-Fabric"
    "1.19.4" = "1.19.4-Fabric"
}

foreach ($mc in $versions.Keys) {
    $verDir = $versions[$mc]
    $modsDir = "$targetDir/$verDir/mods"
    $url = "https://api.modrinth.com/v2/project/fabric-api/version?loaders=[`"fabric`"]&game_versions=[`"$mc`"]"
    
    Write-Host "Fetching Fabric API for $mc..."
    try {
        $resp = Invoke-RestMethod -Uri $url -TimeoutSec 15
        if ($resp.Count -gt 0) {
            $file = $resp[0].files[0]
            $outPath = "$modsDir/$($file.filename)"
            Write-Host "  Downloading $($file.filename)..."
            Invoke-WebRequest -Uri $file.url -OutFile $outPath -TimeoutSec 60
            Write-Host "  Done: $outPath"
        } else {
            Write-Host "  NOT FOUND"
        }
    } catch {
        Write-Host "  FAILED: $_"
    }
}
