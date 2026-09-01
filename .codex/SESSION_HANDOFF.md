# Session handoff

## Objetivo general

Entregar Gemidos PREMIUM como aplicacion Android nativa mantenible: cuenta regresiva de 10 a 0, reproduccion automatica de un efecto de gemidos a volumen multimedia maximo, apagado gratuito inmediato, experiencia Premium y las 21 opciones de idioma de Linterna Premium.

## Tarea actual

Icono corregido y validado; no quedan tareas de implementacion activas. La correccion aparecera al generar/instalar la proxima APK desde Apps Dashboard. El pedido anterior de sonidos esta terminado en este proyecto y fue enviado a `Diseña Linterna Premium` para implementacion independiente.

## Estado actual

- Memoria persistente inicializada y reconciliada con el proyecto real.
- Rama primaria `main`; remoto privado `https://github.com/ldebortoli/gemidos-premium.git`.
- Android nativo Kotlin/Compose con paquete `com.gemidospremium.app`, version 0.1.0 y variantes `demo`/`play`.
- Icono: boca y ambas ondas reducidas uniformemente al 70 %, traslacion -3/-1 sobre pivote 54/54. Radio visible medido 27,49 dp, dentro del circulo seguro de 33 dp; antes llegaba a 43,57 dp y la onda exterior se recortaba. Legacy/adaptativo y splash comparten el mismo vector. Comparacion local revisada en `artifacts/launcher-icon-comparison.png` y vista corregida en `artifacts/launcher-icon-fixed.png` (ignoradas por Git).
- La cuenta regresiva 10 a 0 inicia al entrar; al llegar a cero el reproductor local solicita foco transitorio y eleva temporalmente el canal multimedia al maximo permitido.
- El contador muestra solo `Se viene...` sobre el numero, sin explicacion inferior.
- `Apagado plebeyo` es texto pequeno sin superficie de boton: detiene gratis e inmediatamente y reinicia la cuenta en 10, sin aviso, pantalla silenciada ni control intermedio. `APAGADO PREMIUM` ofrece compra simulada en demo y, cuando hay licencia, silencia/restaura tras el fade de 1,5 segundos mientras una capa visual continua durante 15 segundos con felicitacion localizada, una tragamonedas central, cuatro minis distribuidas, luces perimetrales, 120 particulas y cinco fuegos artificiales.
- La celebracion Premium reproduce una vez `premium_slot_celebration.ogg`: mezcla de 15 segundos (cabecera 14,998 s), 111.244 bytes, SHA-256 `C69B2D6F3023E118D955D88E2FC68F6F9B933F25DC7EB993BE15F12E0950ABDE`. Conserva fanfarrias/aplausos/vitores anteriores al 65 % y agrega los siete Mixkit aprobados en cinco tandas: un premio/casino y hasta dos personas simultaneos, sin base constante. Los descansos medidos duran 0,85-1,05 s y el pico es -1,8 dBFS. Dos regeneraciones `bitexact` coinciden byte a byte. Se detiene al salir, reiniciar o restablecer Premium; no cambia el apagado/restaurado de 1,5 s.
- La restauracion de volumen/foco se ejecuta al apagar, terminar el clip, perder foco, pausar o destruir la actividad.
- El selector conserva las 21 opciones de idioma de Linterna Premium y todos los textos visibles especificos tienen catalogo completo.
- Audio: edicion transformada del MP3 suministrado por el usuario, 9,863 s, 139.141 bytes y SHA-256 `D4937B796316EC8C391F0BB5ECD19A205BCA0584D04F8EA010FC779EB61CFB1E`; se reproduce en loop y su procedencia/limitacion de derechos queda documentada en `docs/AUDIO_LICENSE.md`.
- Validacion: 12 pruebas nuevas del plan y 18 pruebas Android, cero fallos. Cobertura 100 % del validador (lineas/ramas/funciones) y dominio Android (instrucciones/ramas/lineas/complejidad/metodos/clases); umbrales en scripts y Gradle. `npm run test:audio` verifica duracion, picos y silencios con FFmpeg. Gradle `jacocoTestReport jacocoTestCoverageVerification lintDemoDebug` correcto. Sin APK y sin monitorizar CI.
- Validacion del icono: `npm test`, compilacion de recursos mediante pruebas Android, JaCoCo y Android Lint correctos; render local del vector con dos mascaras y comprobacion pixel a pixel de su contencion. `tools/verify-project.mjs` protege escala, dos ondas y reutilizacion del dibujo en las variantes/splash.
- `npm test` y `npm run test:audio` tambien pasan sobre un checkout nuevo del indice con `core.autocrlf=true`. Escaneo de secretos/rutas personales agregadas sin hallazgos; GitHub sigue reportando `security_and_analysis: null` para el repositorio privado personal (bloqueo de plan documentado).
- El run CI inicial `33278253748` fallo solo en Android Lint: el runner conoce API 37 Preview y, con `warningsAsErrors`, elevo `OldTargetApi` sobre `targetSdk = 36`. Se mantiene API 36 estable y se deshabilita unicamente ese diagnostico; el resto de lint sigue estricto. `tools/verify-project.mjs` protege este contrato.
- Apps Dashboard 0.2.11 incorpora `gemidos-premium` como cuarto perfil bundled Android nativo. La carga real valido la raiz Git; commit del Dashboard `056e26c` publicado en `main`.
- No se genero APK. El usuario puede iniciar `demoRelease` desde Apps Dashboard cuando quiera un artefacto instalable.
- Escaneo local de secretos sin hallazgos. GitHub rechazo Secret Scanning/Push Protection con HTTP 422 porque no esta disponible para este repositorio privado personal; el bloqueo figura inline en BACKLOG.
- `.gitattributes` normaliza texto a LF, conserva CRLF en batch y marca recursos binarios; la revision de renormalizacion no produjo cambios masivos.

## Proximos pasos

1. Cuando el usuario lo pida, generar `demoRelease` desde Apps Dashboard y probar volumen, restauracion, ciclo de vida y botones en un telefono Android real.
2. Antes de publicar, configurar producto `premium_silence_pack`, AdMob/UMP, firma de produccion, politica de privacidad, Data safety y clasificacion de contenido.
3. Activar Secret Scanning/Push Protection si el plan o tipo del repositorio pasa a soportarlo.

## Riesgos

- El volumen maximo y el contenido vocal pueden resultar molestos; el apagado plebeyo y la restauracion del volumen deben permanecer garantizados.
- La publicacion comercial puede requerir clasificacion etaria y declaraciones de contenido sexual/sugestivo en Google Play.
- El audio fuente no debe redistribuirse como archivo independiente; se incorpora solamente dentro del producto creativo.
- Los siete MP3 de Mixkit no se versionan: se descargan bajo demanda a `artifacts/audio-sources/` y se validan contra sus hashes. La licencia y restricciones de uso de Envato quedan documentadas; antes de distribuir fuera de pruebas privadas se debe confirmar su compatibilidad con el contenido sugestivo de Gemidos Premium.
