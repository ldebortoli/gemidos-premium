# Gemidos Premium - Contexto del proyecto

## Descripcion general

Aplicacion Android humoristica de reproduccion de audio. Al abrir, muestra una cuenta regresiva visible de 10 a 0; al finalizar solicita foco de audio, lleva temporalmente el canal multimedia al maximo y reproduce en loop un efecto de gemidos integrado. Siempre ofrece un apagado plebeyo gratuito e inmediato y un apagado Premium de presentacion exagerada, sin impedir que el usuario silencie la app.

## Estado detectado

- Ruta: `<project-root>`
- Version: `0.1.0` (`versionCode` 1).
- Stack: Android nativo sobre Android 16 / API 36, Kotlin, Jetpack Compose y Material 3, siguiendo las convenciones verificadas de Linterna Premium sin compartir identidad tecnica.
- Git: repositorio privado `ldebortoli/gemidos-premium`.
- Rama primaria: `main`.
- Remoto origin: `https://github.com/ldebortoli/gemidos-premium.git`.
- Apps Dashboard: cuarto perfil bundled `gemidos-premium` desde la version 0.2.11; build QA `demoRelease`, destino inicial Codex Logs y menciones solo a `@galerazo34`.

## Estructura

- `.codex/`: memoria persistente.
- `apps/mobile/android/`: aplicacion Android nativa y recurso MP3 local.
- `tools/`: controles de identidad, version, audio y Gradle.
- `docs/`: licencia de audio, privacidad, producto y configuracion comercial.
- El perfil de Apps Dashboard vive en su repositorio independiente, bajo `config/projects/gemidos-premium.json`.

## Ejecucion y tests

- Validacion verificada: `npm test`, `npm run test:android`, `npm run coverage` y `npm run lint`.
- Baseline: 18 pruebas Android, cero fallos, y 100% en instrucciones, ramas, lineas, complejidad, metodos y clases del dominio medido.
- CI fija API 36 y conserva Android Lint estricto; solo `OldTargetApi` esta deshabilitado porque el runner conoce el SDK 37 Preview y elevaba ese aviso ambiental a error.
- El APK se generara desde Apps Dashboard o solo por pedido explicito; la implementacion ordinaria no inicia Gradle para ensamblar un artefacto.
- El audio integrado es una edicion de 9,863 segundos del MP3 suministrado por el usuario, reproducida en loop; su SHA-256 es `D4937B796316EC8C391F0BB5ECD19A205BCA0584D04F8EA010FC779EB61CFB1E`.
- El contador muestra solo `Se viene...` sobre el numero. Apagado plebeyo es texto pequeno y reinicia directamente en 10; Premium silencia y restaura el audio tras su fade de 1,5 segundos, mientras una celebracion independiente de 15 segundos muestra felicitacion localizada, cinco tragamonedas, luces, particulas y cinco fuegos artificiales.
- GitHub Secret Scanning y Push Protection no estan disponibles para este repositorio privado personal; se conserva el escaneo local y el bloqueo exacto en BACKLOG.

## Convenciones

- Preservar cambios ajenos y secretos locales.
- Actualizar este archivo solo cuando cambie informacion estable.
- La memoria persistente vive en `.codex/` y se carga siguiendo `AGENTS.md`.
- Si el proyecto tiene una UI para controlar un bot, servidor o proceso en segundo plano, cerrar esa UI debe detener el proceso administrado cuando sea tecnicamente posible.
- El apagado plebeyo debe permanecer visible, gratuito e inmediato mientras suena el audio.
- La app restaura el volumen multimedia previo y libera el foco de audio al apagar, perder foco, salir o cerrarse.
- Los archivos de texto versionados se normalizan a LF mediante `.gitattributes`; scripts batch conservan CRLF y los recursos binarios se marcan explicitamente.
