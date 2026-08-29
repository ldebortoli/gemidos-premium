import crypto from "node:crypto";
import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

const root = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "..");
const read = (relativePath) => fs.readFileSync(path.join(root, relativePath), "utf8");
const requiredFiles = [
  "version.properties",
  "apps/mobile/app.json",
  "apps/mobile/android/app/src/main/AndroidManifest.xml",
  "apps/mobile/android/app/src/main/res/values/strings.xml",
  "apps/mobile/android/app/src/main/res/raw/prank_moans.mp3",
  "apps/mobile/android/app/src/main/java/com/gemidospremium/app/domain/GemidosEngine.kt",
  "apps/mobile/android/app/src/main/java/com/gemidospremium/app/domain/PremiumSilenceRunner.kt",
  "apps/mobile/android/app/src/main/java/com/gemidospremium/app/platform/AndroidPrankAudioPort.kt",
  "apps/mobile/android/app/src/main/java/com/gemidospremium/app/ui/GemidosPremiumScreen.kt",
  "apps/mobile/android/app/src/main/java/com/gemidospremium/app/localization/GemidosLocalization.kt",
  "apps/mobile/android/app/src/demo/AndroidManifest.xml",
  "apps/mobile/android/app/src/play/AndroidManifest.xml",
  "docs/AUDIO_LICENSE.md",
  "docs/CONFIGURACION_GOOGLE.md",
  "docs/PRIVACIDAD.md",
];
const errors = [];

for (const relativePath of requiredFiles) {
  if (!fs.existsSync(path.join(root, relativePath))) errors.push(`Falta ${relativePath}`);
}

if (errors.length === 0) {
  const engine = read("apps/mobile/android/app/src/main/java/com/gemidospremium/app/domain/GemidosEngine.kt");
  const mainActivity = read("apps/mobile/android/app/src/main/java/com/gemidospremium/app/MainActivity.kt");
  const audioPort = read("apps/mobile/android/app/src/main/java/com/gemidospremium/app/platform/AndroidPrankAudioPort.kt");
  const screen = read("apps/mobile/android/app/src/main/java/com/gemidospremium/app/ui/GemidosPremiumScreen.kt");
  const localization = read("apps/mobile/android/app/src/main/java/com/gemidospremium/app/localization/GemidosLocalization.kt");
  const manifest = read("apps/mobile/android/app/src/main/AndroidManifest.xml");
  const gradle = read("apps/mobile/android/app/build.gradle.kts");
  const mobilePackage = JSON.parse(read("apps/mobile/package.json"));
  const appConfig = JSON.parse(read("apps/mobile/app.json"));
  const strings = read("apps/mobile/android/app/src/main/res/values/strings.xml");

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
  if (!mainActivity.includes("override fun onPause") || !mainActivity.includes("engine.pause()")) {
    errors.push("Salir de la actividad debe cortar el audio y restaurar el telefono");
  }
  if (
    !audioPort.includes("getStreamVolume") ||
    !audioPort.includes("getStreamMaxVolume") ||
    !audioPort.includes("setStreamVolume") ||
    !audioPort.includes("abandonAudioFocus")
  ) {
    errors.push("El puerto Android debe maximizar temporalmente y restaurar el volumen y foco multimedia");
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
  if (audio.length !== 765600) errors.push("El recurso de audio no coincide con el archivo verificado");
  if (audioHash !== "F12F8367AD8D20A9240E55720A02B9AD2C9216D83609C7322E28792305E4A176") {
    errors.push("El hash del audio integrado no coincide con la fuente documentada");
  }
}

if (errors.length > 0) {
  console.error(errors.map((error) => `- ${error}`).join("\n"));
  process.exit(1);
}

console.log("Identidad, cuenta regresiva, audio restaurable, idiomas y separacion demo/Play verificados.");
