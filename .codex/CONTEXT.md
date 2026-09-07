# Gemidos Premium - Contexto del proyecto

## Descripcion general

Aplicacion Android humoristica de reproduccion de audio. Al abrir, muestra una cuenta regresiva visible de 10 a 0; al finalizar solicita foco de audio, lleva temporalmente el canal multimedia al maximo y reproduce en loop un efecto de gemidos integrado. Siempre ofrece un apagado plebeyo gratuito e inmediato y un apagado Premium de presentacion exagerada, sin impedir que el usuario silencie la app.

## Estado detectado

- Ruta: `<project-root>`
- Version: `0.1.1` (`versionCode` 2).
- Stack: Android nativo sobre Android 16 / API 36, Kotlin, Jetpack Compose y Material 3, siguiendo las convenciones verificadas de Linterna Premium sin compartir identidad tecnica.
- Git: repositorio publico source-only `ldebortoli/gemidos-premium`, independiente del original privado preservado como gemidos-premium-private-archive.
- Rama primaria: `main`.
- Remoto origin: `https://github.com/ldebortoli/gemidos-premium.git`.
- Apps Dashboard: cuarto perfil bundled `gemidos-premium` desde la version 0.2.11; build QA `demoRelease`, destino inicial Codex Logs y menciones solo a `@galerazo34`.

## Estructura

- `.codex/`: memoria persistente.
- `apps/mobile/android/`: aplicacion Android nativa, audios locales ignorados por Git y recursos de texto exclusivos de las pruebas source-only.
- `tools/`: controles de identidad, version, audio y Gradle.
- `docs/`: licencia de audio, privacidad, producto y configuracion comercial.
- El perfil de Apps Dashboard vive en su repositorio independiente, bajo `config/projects/gemidos-premium.json`.

## Ejecucion y tests

- Validacion verificada: `npm test`, `npm run test:android`, `npm run coverage` y `npm run lint`.
- Baseline: 18 pruebas Android, cero fallos, y 100% en instrucciones, ramas, lineas, complejidad, metodos y clases del dominio medido.
- CI fija API 36 y conserva Android Lint estricto; solo `OldTargetApi` esta deshabilitado porque el runner conoce el SDK 37 Preview y elevaba ese aviso ambiental a error.
- El APK se generara desde Apps Dashboard o solo por pedido explicito; la implementacion ordinaria no inicia Gradle para ensamblar un artefacto.
- Codigo publicado sin audios: historial filtrado con autorizacion, original remoto conservado privado con otro nombre. Ver D-018 y docs/PUBLIC_SOURCE.md. Tests JVM/cobertura/lint pueden compilar sin grabaciones; builds instalables exigen ambos audios privados. Restaurados y comprobados los hashes originales en la copia local; sin APK nueva.
- El audio integrado es una edicion de 9,863 segundos del MP3 suministrado por el usuario, reproducida en loop; su SHA-256 es `D4937B796316EC8C391F0BB5ECD19A205BCA0584D04F8EA010FC779EB61CFB1E`.
- El contador muestra solo `Se viene...` sobre el numero. Apagado plebeyo es texto pequeno y reinicia directamente en 10; Premium silencia y restaura el audio tras su fade de 1,5 segundos, mientras una celebracion independiente de 15 segundos muestra felicitacion localizada, cinco tragamonedas, luces, particulas y cinco fuegos artificiales.
- La celebracion Premium reproduce una mezcla Ogg Vorbis continua de 15 segundos: conserva cinco fanfarrias, vitores y aplausos generados, y suma completos los siete efectos Mixkit aprobados (candidatos 2 a 8, nunca 1/462). Algunos clips se repiten completos y sus transiciones se solapan brevemente para cubrir toda la animacion sin pausas audibles; no se restaura el sonido mecanico constante descartado. Se detiene al salir, reiniciar o restablecer Premium. Fuentes, hashes y tiempos viven en `tools/premium-celebration-plan.json`; los MP3 fuente no se versionan.
- Las herramientas raiz usan Node 24, alineado con CI. `npm test` incluye 14 pruebas del plan sonoro con umbral 100 % de lineas/ramas/funciones; `npm run test:audio` verifica localmente duracion, pico y 300 ventanas consecutivas de 50 ms sin silencios mediante FFmpeg, sin agregar descargas ni codecs al CI rapido.
- El icono vectorial de boca/dos ondas esta al 70 % y centrado dentro de la zona segura de Android; iconos adaptativos, legacy y splash comparten `ic_launcher_foreground`, sin el icono heredado de Linterna.
- GitHub Secret Scanning y Push Protection habilitados y verificados en el nuevo repositorio publico. El bloqueo historico del repo privado queda resuelto para este proyecto mantenido.

## Convenciones

- Preservar cambios ajenos y secretos locales.
- Actualizar este archivo solo cuando cambie informacion estable.
- La memoria persistente vive en `.codex/` y se carga siguiendo `AGENTS.md`.
- Si el proyecto tiene una UI para controlar un bot, servidor o proceso en segundo plano, cerrar esa UI debe detener el proceso administrado cuando sea tecnicamente posible.
- El apagado plebeyo debe permanecer visible, gratuito e inmediato mientras suena el audio.
- La app restaura el volumen multimedia previo y libera el foco de audio al apagar, perder foco, salir o cerrarse.
- Los sonidos suplementarios de la celebracion nunca deben impedir ni retrasar el restaurado del audio principal.
- Los archivos de texto versionados se normalizan a LF mediante `.gitattributes`; scripts batch conservan CRLF y los recursos binarios se marcan explicitamente.
