# Audio integrado y procedencia

## Recurso actual

Desde 0.1.1 estos recursos son exclusivamente locales y están excluidos del repositorio público y de su historial. Las menciones siguientes a integración describen la copia privada de la aplicación, no archivos descargables del repositorio. Ver `PUBLIC_SOURCE.md`.

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

Ni el MP3 fuente ni su edición integrada se versionan en el repositorio público. El recurso anterior de Pixabay fue reemplazado y tampoco se conserva en el historial publicado.

## Celebracion Premium: composicion original y efectos Mixkit elegidos

- Archivo integrado: `apps/mobile/android/app/src/main/res/raw/premium_slot_celebration.ogg`.
- Formato: Ogg Vorbis, 44,1 kHz, mono.
- Duración: 15 segundos; decodificación OGG 14,998 s (tolerancia del codec menor a 40 ms).
- Tamaño verificado: 157.605 bytes.
- SHA-256: `191CB456CA2CBEF6FE08F1E94857DC1D07463F4BE5143416216C0D2E39C985D6`.
- Fecha de mezcla y verificación: 2026-09-01.

Conserva las cinco fanfarrias armónicas, aplausos sintetizados y voces locales (`¡Hurra!`, `¡Vamos!`, `¡Premio!`, `¡Bravo!` y `Woohoo!`) de la composición previa, atenuadas al 65 % para dar espacio a los efectos reales aprobados. La mezcla actual sí incluye muestras de terceros y no se presenta como enteramente original.

### Selección aprobada y procedencia

El usuario aprobó los candidatos 2 a 8 y rechazó expresamente el 1 (`Huge crowd cheering victory`, Mixkit 462). Ese sonido no se incorpora.

| Candidato | Mixkit ID | Título oficial | Fuente |
|---|---|---|---|
| 2 | 531 | Birthday crowd party cheer | [Crowd](https://mixkit.co/free-sound-effects/crowd/) |
| 3 | 459 | Male crowd cheering short | [Crowd](https://mixkit.co/free-sound-effects/crowd/) |
| 4 | 437 | Small crowd ovation | [Crowd](https://mixkit.co/free-sound-effects/crowd/) |
| 5 | 2012 | Males yes victory | [Win](https://mixkit.co/free-sound-effects/win/) |
| 6 | 2011 | Male voice cheer victory | [Win](https://mixkit.co/free-sound-effects/win/) |
| 7 | 1934 | Payout award | [Win](https://mixkit.co/free-sound-effects/win/) |
| 8 | 1928 | Slot machine win | [Win](https://mixkit.co/free-sound-effects/win/) |

Se usan las mismas preescuchas MP3 oficiales presentadas y aprobadas, disponibles en `https://assets.mixkit.co/active_storage/sfx/{id}/{id}-preview.mp3`. El manifiesto `tools/premium-celebration-plan.json` fija sus nombres, SHA-256, duración y cada reproducción completa. No se eliminan marcas de agua.

Licencia consultada el 2026-09-01: [Mixkit Sound Effects Free License](https://mixkit.co/license/#sfxFree), cuyo [texto completo](https://mixkit.co/license/modal/sfxFree/) permite incorporar los efectos a productos creativos comerciales o no comerciales, pero no redistribuirlos aislados, como stock ni junto a archivos fuente, atribuirse su autoría o registrarlos en servicios de gestión de derechos. Los siete MP3 se mantienen fuera de Git; solo se integra la composición terminada dentro de la aplicación. El generador no entrega un paquete descargable de efectos.

Los [términos de Mixkit](https://mixkit.co/terms/) también incorporan la [política de uso aceptable de Envato](https://help.elements.envato.com/hc/en-us/articles/31035788503321-Acceptable-Use-Policy), con restricciones sobre contenido adulto, ofensivo y apuestas. Antes de distribuir Gemidos Premium fuera de pruebas privadas se debe confirmar la compatibilidad de su contenido sugestivo con esas condiciones; esta integración técnica no equivale a una autorización de Envato ni elimina el pendiente de derechos del audio principal. Las tragamonedas son únicamente decorativas: no se apuesta ni se entregan premios de valor real.

### Mezcla y mantenimiento

- La línea de tiempo es continua de 0 a 15 segundos: los clips empiezan en su muestra inicial y llegan a su final natural, sin recortes, offsets ni fades añadidos. Los candidatos 8, 3 y 5 reaparecen completos para prolongar la fiesta.
- Las transiciones se solapan: como máximo hay cuatro efectos aprobados simultáneos, hasta tres humanos y dos de casino; dos premios de casino solo se cruzan durante 250 ms o menos para evitar un hueco.
- Se conservan las cinco fanfarrias, los vítores generados y los aplausos anteriores. La primera fanfarria arranca en cero y un aplauso adicional completa el último segundo, sin restaurar el sonido mecánico constante que se había descartado.
- No hay loops de archivos ni interrupciones de clips. La verificación acústica divide toda la pista en 300 ventanas consecutivas de 50 ms y exige señal audible en cada una; además comprueba por separado los 15 segundos completos.
- La pista respeta el mismo reproductor y se interrumpe al salir, reiniciar o restablecer Premium. No modifica el fade/restaurado del audio principal de 1,5 s.

Regenerar: `pwsh -NoProfile -File tools/generate-premium-celebration.ps1`. Requiere Node 24 (el mismo major que CI), FFmpeg y las voces Windows Raul, Sabina Desktop y Zira Desktop. Descarga únicamente las siete fuentes seleccionadas si no están en `artifacts/audio-sources/` (ignorado), y aborta si no coinciden sus hashes. Para reutilizar archivos ya descargados: agregar `-SourceDirectory <carpeta-local>`. Semillas de ruido, modo `bitexact` y número de serie OGG permanecen fijados; dos regeneraciones locales coincidieron byte a byte. Cambios de versión de FFmpeg o voces pueden cambiar el hash final y requieren revisión.

Validación rápida sin red ni FFmpeg: `npm test` incluye 14 pruebas del plan, además de los contratos del proyecto. `npm run test:audio-plan` exige 100 % de líneas, ramas y funciones en el validador del plan mediante la cobertura nativa de Node. Validación acústica local: `npm run test:audio` decodifica el OGG con FFmpeg y comprueba duración, margen de pico, continuidad en las 300 ventanas de 50 ms y presencia audible en cada segundo. `npm run coverage` mantiene el umbral 100 % para instrucciones, ramas, líneas y métodos del dominio Android (el sintetizador, códecs y plataforma Android no forman parte de ese alcance).
