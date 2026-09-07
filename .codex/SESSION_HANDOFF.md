# Session handoff

## Objetivo general

Entregar Gemidos PREMIUM como aplicacion Android nativa mantenible: cuenta regresiva de 10 a 0, reproduccion automatica de un efecto de gemidos a volumen multimedia maximo, apagado gratuito inmediato, experiencia Premium y las 21 opciones de idioma de Linterna Premium.

## Tarea actual

- DONE 2026-09-07: publicacion source-only autorizada desde Galerazo web, codigo56035d4. Nueve commits limpios publicados en repo nuevo independiente con URL original (id1360702988), PUBLIC y ambas protecciones de secretos enabled. Remoto original id1350942338 permanece PRIVATE bajo gemidos-premium-private-archive; no es un fork. Backup completo en artifacts/publication-backup/before-public.bundle y audios en esa carpeta; ambos recursos restaurados en res/raw e ignorados, hashes intactos. Version0.1.1/build2 para controles de fuentes publicas, experiencia sin cambios ni APK. HTTP anonimo repo200, archivo privado404, audios actuales y hash de commit antiguo404. No hubo push forzado al archivo: se conservo todo su historial privado.
- Verificado localmente sin audios: 14 tests del plan + 2 controles de fuente publica,18 pruebas Android y JaCoCo100% (796 instrucciones,38 ramas,153 lineas,22 metodos),Lint correcto. Ensamblado dry-run sin audios y test dry-run con un solo audio rechazados con mensaje esperado. Con originales restaurados: contratos/plan/acustica300ventanas/cobertura/Lint correctos. Gitleaks en historial y staged sin hallazgos. CI se omite con [skip ci] tras validar localmente; no se monitoreo. No hay trabajo pendiente de esta publicacion.

Icono corregido y validado; no quedan tareas de implementacion activas. La correccion aparecera al generar/instalar la proxima APK desde Apps Dashboard. El pedido anterior de sonidos esta terminado en este proyecto y fue enviado a `Diseña Linterna Premium` para implementacion independiente.

## Estado actual

- Memoria persistente inicializada y reconciliada con el proyecto real.
- Rama primaria `main`; remoto publico source-only `https://github.com/ldebortoli/gemidos-premium.git`.
- Android nativo Kotlin/Compose con paquete `com.gemidospremium.app`, version 0.1.1 / build2 y variantes `demo`/`play`.
- Icono: boca y ambas ondas reducidas uniformemente al 70 %, traslacion -3/-1 sobre pivote 54/54. Radio visible medido 27,49 dp, dentro del circulo seguro de 33 dp; antes llegaba a 43,57 dp y la onda exterior se recortaba. Legacy/adaptativo y splash comparten el mismo vector. Comparacion local revisada en `artifacts/launcher-icon-comparison.png` y vista corregida en `artifacts/launcher-icon-fixed.png` (ignoradas por Git).
- La cuenta regresiva 10 a 0 inicia al entrar; al llegar a cero el reproductor local solicita foco transitorio y eleva temporalmente el canal multimedia al maximo permitido.
- El contador muestra solo `Se viene...` sobre el numero, sin explicacion inferior.
- `Apagado plebeyo` es texto pequeno sin superficie de boton: detiene gratis e inmediatamente y reinicia la cuenta en 10, sin aviso, pantalla silenciada ni control intermedio. `APAGADO PREMIUM` ofrece compra simulada en demo y, cuando hay licencia, silencia/restaura tras el fade de 1,5 segundos mientras una capa visual continua durante 15 segundos con felicitacion localizada, una tragamonedas central, cuatro minis distribuidas, luces perimetrales, 120 particulas y cinco fuegos artificiales.
- La celebracion Premium reproduce una vez `premium_slot_celebration.ogg`: mezcla continua de 15 segundos (decodifica 14,998 s), 157.605 bytes, SHA-256 `191CB456CA2CBEF6FE08F1E94857DC1D07463F4BE5143416216C0D2E39C985D6`. Conserva fanfarrias/aplausos/vitores anteriores al 65 %, inicia la primera fanfarria en cero, agrega un aplauso final y reproduce completos los siete Mixkit aprobados; 8/3/5 se repiten enteros. Las transiciones se cruzan hasta 250 ms y la pista no tiene silencios en ninguna de sus 300 ventanas de 50 ms; el pico es -1,4 dBFS. Dos regeneraciones `bitexact` coinciden byte a byte. Se detiene al salir, reiniciar o restablecer Premium; no cambia el apagado/restaurado de 1,5 s.
- La restauracion de volumen/foco se ejecuta al apagar, terminar el clip, perder foco, pausar o destruir la actividad.
- El selector conserva las 21 opciones de idioma de Linterna Premium y todos los textos visibles especificos tienen catalogo completo.
- Audio: edicion transformada del MP3 suministrado por el usuario, 9,863 s, 139.141 bytes y SHA-256 `D4937B796316EC8C391F0BB5ECD19A205BCA0584D04F8EA010FC779EB61CFB1E`; se reproduce en loop y su procedencia/limitacion de derechos queda documentada en `docs/AUDIO_LICENSE.md`.
- Validacion: 14 pruebas del plan y 18 pruebas Android, cero fallos. Cobertura 100 % del validador (lineas/ramas/funciones) y dominio Android (instrucciones/ramas/lineas/complejidad/metodos/clases); umbrales en scripts y Gradle. `npm run test:audio` verifica duracion, pico y continuidad en 300 ventanas de 50 ms con FFmpeg. Gradle `jacocoTestReport jacocoTestCoverageVerification lintDemoDebug` correcto. Sin APK y sin monitorizar CI.
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
3. Mantener Secret Scanning/Push Protection activos en el repo publico y no volver a publicar historiales o audios del archivo privado.

## Riesgos

- El volumen maximo y el contenido vocal pueden resultar molestos; el apagado plebeyo y la restauracion del volumen deben permanecer garantizados.
- La publicacion comercial puede requerir clasificacion etaria y declaraciones de contenido sexual/sugestivo en Google Play.
- El audio fuente no debe redistribuirse como archivo independiente; se incorpora solamente dentro del producto creativo.
- Los siete MP3 de Mixkit no se versionan: se descargan bajo demanda a `artifacts/audio-sources/` y se validan contra sus hashes. La licencia y restricciones de uso de Envato quedan documentadas; antes de distribuir fuera de pruebas privadas se debe confirmar su compatibilidad con el contenido sugestivo de Gemidos Premium.
- El pedido equivalente de audio continuo, clips completos y relleno de los 15 segundos fue enviado a la tarea `Diseña Linterna Premium` (`01a048db-3bc7-7cc3-aaee-b1d37d53e4c2`) el 2026-09-01; su implementacion pertenece a esa tarea independiente.
