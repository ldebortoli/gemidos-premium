# Session handoff

## Estado actual · 2026-09-07

- Version0.1.1/build2; codigo56035d4, actualizaciones de memoria posteriores.
- Main/origin https://github.com/ldebortoli/gemidos-premium.git PUBLIC,
  repositorio independiente id1360702988. Secret Scanning y Push Protection
  habilitados y verificados; autor noreply.
- El propietario autorizo retirar audios/rutas personales de todo el historial,
  conservar recursos locales, renombrar el remoto original privado y publicar
  un repositorio nuevo con nombre/URL originales. D-018.
- Los nueve commits originales se filtraron antes del primer push al repo nuevo.
  Original remoto id1350942338 sigue PRIVATE como gemidos-premium-private-archive;
  no es un fork ni se abrio ese archivo al publico.
- Respaldo completo verificado: artifacts/publication-backup/before-public.bundle.
  Ambos audios tambien respaldados ahi y restaurados en res/raw con hashes
  originales. Todo ese material es local/ignorado: NO versionarlo ni publicarlo.
- No mezclar otros clones del historial antiguo con main: usar un clon nuevo o
  migrar solo cambios de codigo revisados. No restaurar remotos/ref de respaldo.
- Repo publico200 sin sesion; archivo privado404; audios en main y commit
  antiguo404. Gitleaks del historial y diff sin hallazgos.

## Validacion y contrato source-only

- Sin audios: npm test (14 tests del plan +2 guardas),18 pruebas Android,
  coverage100% y Lint correctos. Gradle permite solo cuatro tareas explicitas
  de calidad, con identificadores .txt no reproducibles exclusivos de tests.
- Build instalable sin audios y calidad con solo uno de los dos audios:
  rechazo temprano verificado con dry-run. No generar una APK con esos stubs.
- Con originales restaurados: contratos, plan, acustica300ventanas,
  cobertura y Lint aprobados; hashes de ambos recursos intactos.
- JaCoCo dominio:796 instrucciones,38 ramas,153 lineas,22 metodos, todos100%;
  plan sonoro100% lineas/ramas/funciones. Umbrales100% preservados.
- Sin APK nueva, sin prueba de reproduccion en dispositivo, pagos o anuncios
  reales. CI no monitoreada; pushes [skip ci] luego de verificar localmente.
- Source-only y restauracion local documentados en docs/PUBLIC_SOURCE.md.
  README/version.properties/manifest/paquetes/lockfiles sincronizados.
- .gitattributes conserva LF y CRLF para .bat; gradlew ejecutable100755.
- El codigo de reproduccion, idiomas, UI e identidad aprobados no cambio.

## Continuidad de la aplicacion

- Android Kotlin/Compose, com.gemidospremium.app, variantes demo/play. Audio
  empieza tras cuenta regresiva10; apagado gratuito inmediato y restauracion
  de volumen/foco al salir. Premium silencia en1.5s y celebra15s sin retener ruido.
- Audio local principal9.863s en loop y celebracion14.998s continua; licencia,
  hashes y generador en docs/AUDIO_LICENSE.md. No redistribuir las grabaciones.
- Apps Dashboard usa la misma carpeta local; demoRelease requiere el keystore
  QA estable via APPS_DASHBOARD_ANDROID_TEST_KEYSTORE_PATH.
- Antes de distribucion comercial: confirmar derechos/clasificacion del audio,
  condiciones Mixkit, Play Console/Billing, AdMob/UMP, firma y politica publica.
- USER_QUEUE vacia. Sin trabajo pendiente de esta publicacion. No generar APK
  hasta que el usuario la pida; no reabrir el antiguo archivo privado.
