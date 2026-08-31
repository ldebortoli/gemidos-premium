# Session handoff

## Objetivo general

Entregar Gemidos PREMIUM como aplicacion Android nativa mantenible: cuenta regresiva de 10 a 0, reproduccion automatica de un efecto de gemidos a volumen multimedia maximo, apagado gratuito inmediato, experiencia Premium y las 21 opciones de idioma de Linterna Premium.

## Tarea actual

Entrega actual completada; no hay trabajo en curso.

## Estado actual

- Memoria persistente inicializada y reconciliada con el proyecto real.
- Rama primaria `main`; remoto privado `https://github.com/ldebortoli/gemidos-premium.git`.
- Android nativo Kotlin/Compose con paquete `com.gemidospremium.app`, version 0.1.0 y variantes `demo`/`play`.
- La cuenta regresiva 10 a 0 inicia al entrar; al llegar a cero el reproductor local solicita foco transitorio y eleva temporalmente el canal multimedia al maximo permitido.
- El contador muestra solo `Se viene...` sobre el numero, sin explicacion inferior.
- `Apagado plebeyo` es texto pequeno sin superficie de boton: detiene gratis e inmediatamente y reinicia la cuenta en 10, sin aviso, pantalla silenciada ni control intermedio. `APAGADO PREMIUM` ofrece compra simulada en demo y, cuando hay licencia, una celebracion de 3,2 segundos con tragamonedas, luces perimetrales, 120 particulas y cinco fuegos artificiales.
- La restauracion de volumen/foco se ejecuta al apagar, terminar el clip, perder foco, pausar o destruir la actividad.
- El selector conserva las 21 opciones de idioma de Linterna Premium y todos los textos visibles especificos tienen catalogo completo.
- Audio: edicion transformada del MP3 suministrado por el usuario, 9,863 s, 139.141 bytes y SHA-256 `D4937B796316EC8C391F0BB5ECD19A205BCA0584D04F8EA010FC779EB61CFB1E`; se reproduce en loop y su procedencia/limitacion de derechos queda documentada en `docs/AUDIO_LICENSE.md`.
- Validacion: `npm test`, `npm run test:android`, `npm run coverage` y `npm run lint` correctos; 18 pruebas, cero fallos y cobertura 100% en instrucciones, ramas, lineas, complejidad, metodos y clases del dominio medido.
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
