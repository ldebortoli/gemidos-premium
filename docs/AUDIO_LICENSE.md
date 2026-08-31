# Audio integrado y procedencia

## Recurso actual

- Archivo fuente local suministrado por el usuario: `Voicy_gemi 2 audio troll.mp3`.
- Fecha de recepción y transformación: 2026-08-31.
- Archivo integrado: `apps/mobile/android/app/src/main/res/raw/prank_moans.mp3`.
- Formato: MP3, 44,1 kHz, mono, 112 kb/s.
- Duración verificada: 9,863 segundos.
- Tamaño verificado: 139.141 bytes.
- SHA-256: `D4937B796316EC8C391F0BB5ECD19A205BCA0584D04F8EA010FC779EB61CFB1E`.

## Transformación aplicada

La aplicación no incorpora el archivo recibido sin cambios. La edición integrada:

- recorta el silencio inicial y final sobrante;
- eleva el tono aproximadamente 3,5 %;
- modifica levemente el tempo neto;
- aplica filtros pasa-altos y pasa-bajos, ecualización y atenuación;
- limita picos para evitar clipping adicional;
- elimina metadatos heredados y agrega metadatos internos de trazabilidad.

El resultado se reproduce en loop mientras la pantalla permanece activa y hasta que el usuario emplea un apagado o Android interrumpe el foco/ciclo de vida.

## Derechos y distribución

No se recibió documentación de autoría o licencia junto con el archivo fuente. Las transformaciones técnicas anteriores no eliminan automáticamente derechos de autor ni constituyen una autorización de distribución. Antes de publicar la app fuera de pruebas privadas, el propietario del proyecto debe confirmar que posee una licencia o permiso suficiente para usar y redistribuir este audio.

El MP3 fuente de Descargas no se copia al repositorio; solo se versiona la edición integrada. El recurso anterior de Pixabay fue reemplazado y ya no forma parte del binario actual.

## Efectos originales de tragamonedas y premios

- Archivo integrado: `apps/mobile/android/app/src/main/res/raw/premium_slot_celebration.ogg`.
- Formato: Ogg Vorbis, 44,1 kHz, mono.
- Duración verificada: 15,000 segundos.
- Tamaño verificado: 51.546 bytes.
- SHA-256: `7118D61B5A8B259B188FF898BC6B09658525BA2C27B36A2BFA800DA36E7930D1`.
- Fecha de creación y verificación: 2026-08-31.

Esta pista fue generada específicamente para el proyecto, sin grabaciones ni muestras de terceros. Conserva cinco fanfarrias armónicas de premio y agrega cinco ráfagas breves de aplausos sintetizados y voces generadas localmente (`¡Hurra!`, `¡Vamos!`, `¡Premio!`, `¡Bravo!` y `Woohoo!`). No contiene una base mecánica ni otro sonido constante: cada bloque está separado por aproximadamente un segundo de silencio real. No se reproduce en loop y se interrumpe si la app sale de primer plano o el usuario reinicia la experiencia.

El generador reproducible vive en `tools/generate-premium-celebration.ps1` y se ejecuta con `pwsh -NoProfile -File tools/generate-premium-celebration.ps1`. Usa FFmpeg más las voces locales de Windows, fija las semillas de ruido y el número de serie OGG, y no descarga recursos de red.
