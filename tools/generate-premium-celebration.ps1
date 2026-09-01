[CmdletBinding()]
param(
    [string]$SourceDirectory
)

$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Speech

$projectRoot = Split-Path -Parent $PSScriptRoot
$outputPath = Join-Path $projectRoot "apps\mobile\android\app\src\main\res\raw\premium_slot_celebration.ogg"
$planPath = Join-Path $PSScriptRoot "premium-celebration-plan.json"
$plan = Get-Content -Raw -LiteralPath $planPath | ConvertFrom-Json
& node (Join-Path $PSScriptRoot "premium-celebration-plan.mjs")
if ($LASTEXITCODE -ne 0) { throw "El plan de mezcla no cumple los limites de la celebracion." }
if (-not $SourceDirectory) {
    $SourceDirectory = Join-Path $projectRoot "artifacts\audio-sources"
}
[IO.Directory]::CreateDirectory($SourceDirectory) | Out-Null
foreach ($source in $plan.sources) {
    $sourcePath = Join-Path $SourceDirectory $source.file
    if (-not (Test-Path -LiteralPath $sourcePath)) {
        $sourceUrl = "https://assets.mixkit.co/active_storage/sfx/$($source.id)/$($source.id)-preview.mp3"
        Invoke-WebRequest -Uri $sourceUrl -OutFile $sourcePath
    }
    if ((Get-FileHash -LiteralPath $sourcePath -Algorithm SHA256).Hash -ne $source.sha256) {
        throw "El audio $($source.candidate) cambio: revisar antes de integrarlo."
    }
}
$tempRoot = [IO.Path]::GetFullPath($env:TEMP)
$workPath = [IO.Path]::GetFullPath(
    (Join-Path $tempRoot ("gemidos-premium-cheers-" + [guid]::NewGuid().ToString("N")))
)
$tempPrefix = $tempRoot.TrimEnd('\') + '\'
if (-not $workPath.StartsWith($tempPrefix, [StringComparison]::OrdinalIgnoreCase)) {
    throw "La ruta de trabajo temporal quedo fuera del directorio permitido."
}
[IO.Directory]::CreateDirectory($workPath) | Out-Null
$foundationPath = Join-Path $workPath "original-celebration.ogg"

function New-CheerWave {
    param(
        [Parameter(Mandatory = $true)][string]$Name,
        [Parameter(Mandatory = $true)][string]$Voice,
        [Parameter(Mandatory = $true)][string]$Text,
        [Parameter(Mandatory = $true)][int]$Rate
    )

    $wavePath = Join-Path $workPath ($Name + ".wav")
    $speaker = New-Object System.Speech.Synthesis.SpeechSynthesizer
    try {
        $speaker.SelectVoice($Voice)
        $speaker.Rate = $Rate
        $speaker.Volume = 100
        $speaker.SetOutputToWaveFile($wavePath)
        $speaker.Speak($Text)
    }
    finally {
        $speaker.Dispose()
    }
    return $wavePath
}

try {
    $voiceRequests = @(
        @{ Name = "hurra"; Voice = "Microsoft Raul"; Text = "¡Hurra!"; Rate = 3 },
        @{ Name = "vamos"; Voice = "Microsoft Sabina Desktop"; Text = "¡Vamos!"; Rate = 3 },
        @{ Name = "premio"; Voice = "Microsoft Raul"; Text = "¡Premio!"; Rate = 2 },
        @{ Name = "bravo"; Voice = "Microsoft Sabina Desktop"; Text = "¡Bravo!"; Rate = 3 },
        @{ Name = "woohoo"; Voice = "Microsoft Zira Desktop"; Text = "Woohoo!"; Rate = 3 }
    )
    $voiceProbe = New-Object System.Speech.Synthesis.SpeechSynthesizer
    try {
        $installedVoices = $voiceProbe.GetInstalledVoices() |
            ForEach-Object { $_.VoiceInfo.Name }
    }
    finally {
        $voiceProbe.Dispose()
    }
    foreach ($request in $voiceRequests) {
        if ($installedVoices -notcontains $request.Voice) {
            throw "Falta la voz local requerida: $($request.Voice)."
        }
    }
    $voices = foreach ($request in $voiceRequests) {
        New-CheerWave @request
    }

    $silence = "anullsrc=r=44100:cl=mono:d=15"
    $jingle = "aevalsrc=exprs='0.13*(sin(2*PI*659.25*t)*between(t\,0\,0.16)*exp(-7*t)+sin(2*PI*783.99*t)*between(t\,0.16\,0.32)*exp(-7*(t-0.16))+sin(2*PI*987.77*t)*between(t\,0.32\,0.48)*exp(-7*(t-0.32))+0.62*(sin(2*PI*523.25*t)+sin(2*PI*659.25*t)+sin(2*PI*783.99*t))*between(t\,0.48\,0.95)*exp(-4*(t-0.48)))':s=44100:d=0.95"
    $applause = "anoisesrc=color=white:amplitude=0.20:r=44100:d=1.05:seed=424242"

    $filterParts = New-Object 'System.Collections.Generic.List[string]'
    $filterParts.Add("[1:a]asplit=5[j0][j1][j2][j3][j4]")
    $jingleDelays = @(0, 3200, 6200, 9200, 12200)
    for ($index = 0; $index -lt 5; $index++) {
        $filterParts.Add("[j$index]adelay=$($jingleDelays[$index])[dj$index]")
    }

    $filterParts.Add("[2:a]aeval='val(0)*exp(-55*mod(t\,0.16))*lt(mod(t\,0.16)\,0.050)',highpass=f=650,lowpass=f=6500,asplit=6[c0][c1][c2][c3][c4][c5]")
    $clapDelays = @(950, 3950, 6950, 9950, 12950, 13950)
    for ($index = 0; $index -lt 6; $index++) {
        $filterParts.Add("[c$index]adelay=$($clapDelays[$index])[dc$index]")
    }

    $voiceDelays = @(650, 3650, 6650, 9650, 12650)
    for ($index = 0; $index -lt 5; $index++) {
        $inputIndex = $index + 3
        $primaryDelay = $voiceDelays[$index]
        $secondDelay = $primaryDelay + 85
        $filterParts.Add("[$($inputIndex):a]aresample=44100,highpass=f=120,lowpass=f=6500,asplit=2[v$($index)a][v$($index)b]")
        $filterParts.Add("[v$($index)a]volume=0.60,aecho=0.8:0.5:55:0.16,adelay=$primaryDelay[dv$($index)a]")
        $filterParts.Add("[v$($index)b]asetrate=45600,aresample=44100,volume=0.28,aecho=0.8:0.45:80:0.12,adelay=$secondDelay[dv$($index)b]")
    }

    $jingleInputs = (0..4 | ForEach-Object { "[dj$_]" }) -join ""
    $clapInputs = (0..5 | ForEach-Object { "[dc$_]" }) -join ""
    $voiceInputs = (0..4 | ForEach-Object { "[dv$($_)a][dv$($_)b]" }) -join ""
    $mixInputs = "[0:a]$jingleInputs$clapInputs$voiceInputs"
    $filterParts.Add(
        $mixInputs + "amix=inputs=22:duration=longest:normalize=0,volume=1.65," +
        "alimiter=limit=0.88[out]"
    )
    $filter = $filterParts -join ";"

    $ffmpegArguments = @(
        "-hide_banner", "-loglevel", "error", "-y",
        "-f", "lavfi", "-i", $silence,
        "-f", "lavfi", "-i", $jingle,
        "-f", "lavfi", "-i", $applause
    )
    foreach ($voicePath in $voices) {
        $ffmpegArguments += @("-i", $voicePath)
    }
    $ffmpegArguments += @(
        "-filter_complex", $filter,
        "-map", "[out]", "-t", "15", "-ac", "1", "-ar", "44100",
        "-c:a", "libvorbis", "-q:a", "4", "-fflags", "+bitexact", "-flags:a", "+bitexact",
        "-serial_offset", "4242", "-map_metadata", "-1",
        "-metadata", "title=Gemidos Premium Prize Celebration",
        "-metadata", "comment=Original prize fanfares, synthesized applause and generated cheers",
        $foundationPath
    )
    & ffmpeg @ffmpegArguments
    if ($LASTEXITCODE -ne 0) {
        throw "FFmpeg no pudo generar la pista de festejo."
    }

    # Preserve the existing fanfares/cheers, then overlap complete approved clips.
    # Every source starts at offset zero and plays to its natural end; no clip is looped.
    $culture = [Globalization.CultureInfo]::InvariantCulture
    $mixArguments = @("-hide_banner", "-loglevel", "error", "-y", "-i", $foundationPath)
    $mixFilters = New-Object 'System.Collections.Generic.List[string]'
    $mixFilters.Add("[0:a]volume=$($plan.foundationGain.ToString($culture))[foundation]")
    $mixLabels = "[foundation]"
    for ($index = 0; $index -lt $plan.events.Count; $index++) {
        $event = $plan.events[$index]
        $source = $plan.sources | Where-Object { $_.candidate -eq $event.candidate }
        $mixArguments += @("-i", (Join-Path $SourceDirectory $source.file))
        $gain = $event.gain.ToString($culture)
        $inputIndex = $index + 1
        $mixFilters.Add(
            "[$($inputIndex):a]aresample=44100,aformat=channel_layouts=mono," +
            "asetpts=PTS-STARTPTS,volume=$gain," +
            "adelay=$($event.startMs)[event$index]"
        )
        $mixLabels += "[event$index]"
    }
    $mixFilters.Add(
        $mixLabels + "amix=inputs=$($plan.events.Count + 1):duration=longest:normalize=0," +
        "alimiter=limit=0.85:level=false:latency=true[out]"
    )
    $mixArguments += @(
        "-filter_complex", ($mixFilters -join ";"), "-map", "[out]",
        "-t", "15", "-ac", "1", "-ar", "44100", "-c:a", "libvorbis", "-q:a", "4",
        "-fflags", "+bitexact", "-flags:a", "+bitexact", "-serial_offset", "4242", "-map_metadata", "-1",
        "-metadata", "title=Gemidos Premium layered prize celebration",
        "-metadata", "comment=Original fanfares and cheers with selected Mixkit SFX; see docs/AUDIO_LICENSE.md",
        $outputPath
    )
    & ffmpeg @mixArguments
    if ($LASTEXITCODE -ne 0) { throw "FFmpeg no pudo mezclar los siete efectos aprobados." }

    & ffprobe -v error -show_entries "format=duration,size,bit_rate:stream=codec_name,sample_rate,channels" -of "default=noprint_wrappers=1" $outputPath
    Get-FileHash $outputPath -Algorithm SHA256
}
finally {
    $resolvedWork = [IO.Path]::GetFullPath($workPath)
    if (
        $resolvedWork.StartsWith($tempPrefix, [StringComparison]::OrdinalIgnoreCase) -and
        (Test-Path -LiteralPath $resolvedWork)
    ) {
        Remove-Item -LiteralPath $resolvedWork -Recurse -Force
    }
}
