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
