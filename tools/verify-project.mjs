import crypto from "node:crypto";
import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";
import { plan, validateCelebrationPlan } from "./premium-celebration-plan.mjs";

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "..");
const read = (relativePath) => fs.readFileSync(path.join(root, relativePath), "utf8");
const requiredFiles = [
  "version.properties",
  "apps/mobile/app.json",
  "apps/mobile/android/app/src/main/AndroidManifest.xml",
  "apps/mobile/android/app/src/main/res/values/strings.xml",
  "apps/mobile/android/app/src/main/res/drawable/ic_launcher_foreground.xml",
  "apps/mobile/android/app/src/main/res/mipmap-anydpi/ic_launcher.xml",
  "apps/mobile/android/app/src/main/res/mipmap-anydpi/ic_launcher_round.xml",
  "apps/mobile/android/app/src/main/res/raw/prank_moans.mp3",
  "apps/mobile/android/app/src/main/res/raw/premium_slot_celebration.ogg",
  "apps/mobile/android/app/src/main/java/com/gemidospremium/app/domain/GemidosEngine.kt",
  "apps/mobile/android/app/src/main/java/com/gemidospremium/app/domain/PremiumSilenceRunner.kt",
  "apps/mobile/android/app/src/main/java/com/gemidospremium/app/platform/AndroidPrankAudioPort.kt",
  "apps/mobile/android/app/src/main/java/com/gemidospremium/app/platform/AndroidPremiumCelebrationAudio.kt",
  "apps/mobile/android/app/src/main/java/com/gemidospremium/app/ui/GemidosPremiumScreen.kt",
  "apps/mobile/android/app/src/main/java/com/gemidospremium/app/localization/GemidosLocalization.kt",
  "apps/mobile/android/app/src/demo/AndroidManifest.xml",
  "apps/mobile/android/app/src/play/AndroidManifest.xml",
  ".github/workflows/ci.yml",
  "tools/generate-premium-celebration.ps1",
  "tools/premium-celebration-plan.json",
  "tools/premium-celebration-plan.test.mjs",
  "tools/verify-premium-audio.mjs",
  "docs/AUDIO_LICENSE.md",
  "docs/CONFIGURACION_GOOGLE.md",
  "docs/PRIVACIDAD.md",
];
const errors = [];
validateCelebrationPlan(plan);

for (const relativePath of requiredFiles) {
  if (!fs.existsSync(path.join(root, relativePath))) errors.push(`Falta ${relativePath}`);
}

if (errors.length === 0) {
  const engine = read("apps/mobile/android/app/src/main/java/com/gemidospremium/app/domain/GemidosEngine.kt");
  const mainActivity = read("apps/mobile/android/app/src/main/java/com/gemidospremium/app/MainActivity.kt");
  const audioPort = read("apps/mobile/android/app/src/main/java/com/gemidospremium/app/platform/AndroidPrankAudioPort.kt");
  const premiumAudio = read("apps/mobile/android/app/src/main/java/com/gemidospremium/app/platform/AndroidPremiumCelebrationAudio.kt");
  const premiumAudioGenerator = read("tools/generate-premium-celebration.ps1");
  const screen = read("apps/mobile/android/app/src/main/java/com/gemidospremium/app/ui/GemidosPremiumScreen.kt");
  const localization = read("apps/mobile/android/app/src/main/java/com/gemidospremium/app/localization/GemidosLocalization.kt");
  const manifest = read("apps/mobile/android/app/src/main/AndroidManifest.xml");
  const gradle = read("apps/mobile/android/app/build.gradle.kts");
  const ciWorkflow = read(".github/workflows/ci.yml");
  const mobilePackage = JSON.parse(read("apps/mobile/package.json"));
  const appConfig = JSON.parse(read("apps/mobile/app.json"));
  const strings = read("apps/mobile/android/app/src/main/res/values/strings.xml");
  const launcherForeground = read("apps/mobile/android/app/src/main/res/drawable/ic_launcher_foreground.xml");
  if (
    !launcherForeground.includes('android:scaleX="0.7"') ||
    !launcherForeground.includes('android:scaleY="0.7"') ||
    !launcherForeground.includes('android:translateX="-3"') ||
    !launcherForeground.includes('android:translateY="-1"') ||
    (launcherForeground.match(/android:strokeColor=/g) || []).length !== 2
  ) {
    errors.push("El icono debe conservar sus dos ondas reducidas y centradas dentro de la zona segura");
  }
  for (const directory of ["mipmap-anydpi", "mipmap-anydpi-v26"]) {
    for (const name of ["ic_launcher", "ic_launcher_round"]) {
      const launcher = read(`apps/mobile/android/app/src/main/res/${directory}/${name}.xml`);
      if (!launcher.includes('@drawable/ic_launcher_foreground') || !launcher.includes('@color/launcher_background')) {
        errors.push(`${directory}/${name} debe reutilizar el simbolo y fondo de Gemidos`);
      }
    }
  }
  if (!read("apps/mobile/android/app/src/main/res/values/themes.xml").includes('@drawable/ic_launcher_foreground')) {
    errors.push("El splash debe conservar el mismo simbolo del launcher");
  }

  if (!engine.includes("countdown: Int = 10") && !read("apps/mobile/android/app/src/main/java/com/gemidospremium/app/model/GemidosModels.kt").includes("countdown: Int = 10")) {
    errors.push("La cuenta regresiva debe comenzar en 10");
  }
  if (!engine.includes("GemidosEffect.StartAudio") || !screen.includes("delay(1_000)")) {
    errors.push("La cuenta regresiva debe iniciar el audio automaticamente al llegar a cero");
  }
  if (!engine.includes("fun turnOffNormally") || !screen.includes("TextKey.NORMAL_PLEBEIAN_OFF")) {
    errors.push("Falta el apagado plebeyo gratuito e inmediato");
  }
  if (!screen.includes("height(112.dp)") || !screen.includes("TextKey.PREMIUM_OFF")) {
    errors.push("Apagado Premium debe ser el boton principal extragrande");
  }
  if (screen.includes("TextKey.MAX_INTENSITY_HELP")) {
    errors.push("El contador no debe mostrar una explicacion debajo del numero");
  }
  if (
    screen.includes("OutlinedButton(") ||
    !screen.includes("text = text[TextKey.NORMAL_PLEBEIAN_OFF]") ||
    !screen.includes("fontSize = 12.sp") ||
    !screen.includes(".clickable(onClick = onNormalOff)")
  ) {
    errors.push("Apagado plebeyo debe ser texto pequeno tocable, sin superficie de boton");
  }
  if (
    !screen.includes("PremiumDopamineOverlay(") ||
    !screen.includes("repeat(120)") ||
    !screen.includes("burstCenters") ||
    !screen.includes("★ PREMIUM ×777 ★") ||
    !screen.includes("tween(15_000") ||
    !screen.includes("celebrationText = text[TextKey.PREMIUM_OFF_NOTICE]") ||
    (screen.match(/MiniSlotMachine\(/g) || []).length < 5
  ) {
    errors.push("El apagado Premium debe celebrar 15 segundos con felicitacion, luces y cinco tragamonedas");
  }
  if (!mainActivity.includes("override fun onPause") || !mainActivity.includes("engine.pause()")) {
    errors.push("Salir de la actividad debe cortar el audio y restaurar el telefono");
  }
  if (
    !audioPort.includes("getStreamVolume") ||
    !audioPort.includes("getStreamMaxVolume") ||
    !audioPort.includes("setStreamVolume") ||
    !audioPort.includes("abandonAudioFocus") ||
    !audioPort.includes("isLooping = true")
  ) {
    errors.push("El puerto Android debe reproducir en loop, maximizar temporalmente y restaurar volumen y foco");
  }
  if (
    !premiumAudio.includes("R.raw.premium_slot_celebration") ||
    !premiumAudio.includes("isLooping = false") ||
    !mainActivity.includes("premiumCelebrationAudio.play()") ||
    (mainActivity.match(/premiumCelebrationAudio\.stop\(\)/g) || []).length < 4
  ) {
    errors.push("La celebracion Premium debe reproducir una vez sus sonidos y detenerlos con el ciclo de vida");
  }
  if (
    !premiumAudioGenerator.includes('Text = "¡Hurra!"') ||
    !premiumAudioGenerator.includes('Text = "¡Bravo!"') ||
    !premiumAudioGenerator.includes("$jingleDelays") ||
    !premiumAudioGenerator.includes("$clapDelays") ||
    !premiumAudioGenerator.includes("$plan.events") ||
    !premiumAudioGenerator.includes("$source.sha256") ||
    premiumAudioGenerator.includes("mod(t\\,0.12)") ||
    premiumAudioGenerator.includes("mod(t\\,0.31)")
  ) {
    errors.push("La pista Premium debe conservar sus fanfarrias y solapar completos los audios aprobados durante 15 segundos");
  }
  if (appConfig.app.name !== "Gemidos PREMIUM" || appConfig.app.android.package !== "com.gemidospremium.app") {
    errors.push("La identidad visible y el paquete deben pertenecer a Gemidos PREMIUM");
  }
  if (!strings.includes(">Gemidos PREMIUM<") || !screen.includes('text = "GEMIDOS"')) {
    errors.push("El nombre visible debe ser Gemidos PREMIUM en launcher e interfaz");
  }
  for (const code of [
    "es-AR", "es-ES", "en", "ru", "la", "ja", "it", "fr", "de", "nl",
    "zh-Hans", "zh-Hant", "pt-BR", "pt-PT", "ca", "eu", "gn", "quz",
    "cmn-Hans", "yue-Hant", "ko",
  ]) {
    if (!localization.includes(`("${code}",`)) errors.push(`Falta el idioma ${code}`);
  }
  if (
    !screen.includes("LanguageSelector(") ||
    !mainActivity.includes("PreferencesLanguageStore") ||
    !screen.includes("heightIn(max = 360.dp)")
  ) {
    errors.push("Falta el selector persistente y desplazable de 21 idiomas");
  }
  if (
    !gradle.includes("APPS_DASHBOARD_ANDROID_TEST_KEYSTORE_PATH") ||
    mobilePackage.scripts["build:apk"] !== "node ../../tools/run-gradle.mjs assembleDemoRelease"
  ) {
    errors.push("La APK interna debe ser demoRelease con la identidad QA estable");
  }
  if (
    !gradle.includes("compileSdk = 36") ||
    !gradle.includes("targetSdk = 36") ||
    !gradle.includes('"OldTargetApi"') ||
    !ciWorkflow.includes("platforms;android-36 build-tools;36.0.0")
  ) {
    errors.push("CI debe fijar API 36 y aislar unicamente el aviso ambiental OldTargetApi");
  }
  if (manifest.includes("CAMERA") || manifest.includes("RECORD_AUDIO") || manifest.includes("READ_CONTACTS")) {
    errors.push("La app no debe solicitar camara, microfono ni contactos");
  }
  const playManifest = read("apps/mobile/android/app/src/play/AndroidManifest.xml");
  if (playManifest.includes("ca-app-pub-3940256099942544")) {
    errors.push("La variante Play no puede contener identificadores publicitarios de prueba");
  }

  const audioPath = path.join(root, "apps/mobile/android/app/src/main/res/raw/prank_moans.mp3");
  const audio = fs.readFileSync(audioPath);
  const audioHash = crypto.createHash("sha256").update(audio).digest("hex").toUpperCase();
  if (audio.length !== 139141) errors.push("El recurso de audio no coincide con la edicion verificada");
  if (audioHash !== "D4937B796316EC8C391F0BB5ECD19A205BCA0584D04F8EA010FC779EB61CFB1E") {
    errors.push("El hash del audio integrado no coincide con la edicion documentada");
  }
  const premiumAudioPath = path.join(root, "apps/mobile/android/app/src/main/res/raw/premium_slot_celebration.ogg");
  const premiumAudioAsset = fs.readFileSync(premiumAudioPath);
  const premiumAudioHash = crypto.createHash("sha256").update(premiumAudioAsset).digest("hex").toUpperCase();
  if (premiumAudioAsset.length !== 157605) errors.push("La pista Premium no coincide con el recurso verificado");
  if (premiumAudioHash !== "191CB456CA2CBEF6FE08F1E94857DC1D07463F4BE5143416216C0D2E39C985D6") {
    errors.push("El hash de los sonidos Premium no coincide con la mezcla documentada");
  }
}

if (errors.length > 0) {
  console.error(errors.map((error) => `- ${error}`).join("\n"));
  process.exit(1);
}

console.log("Identidad, cuenta regresiva, audio restaurable, idiomas y separacion demo/Play verificados.");
