# Gemidos Premium - Contexto del proyecto

## Descripcion general

Aplicacion Android humoristica de reproduccion de audio. Al abrir, muestra una cuenta regresiva visible de 10 a 0; al finalizar solicita foco de audio, lleva temporalmente el canal multimedia al maximo y reproduce un efecto de gemidos integrado. Siempre ofrece un apagado plebeyo gratuito e inmediato y un apagado Premium de presentacion exagerada, sin impedir que el usuario silencie la app.

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
- El audio integrado dura 38,28 segundos y su SHA-256 es `F12F8367AD8D20A9240E55720A02B9AD2C9216D83609C7322E28792305E4A176`.
- GitHub Secret Scanning y Push Protection no estan disponibles para este repositorio privado personal; se conserva el escaneo local y el bloqueo exacto en BACKLOG.

## Convenciones

- Preservar cambios ajenos y secretos locales.
- Actualizar este archivo solo cuando cambie informacion estable.
- La memoria persistente vive en `.codex/` y se carga siguiendo `AGENTS.md`.
- Si el proyecto tiene una UI para controlar un bot, servidor o proceso en segundo plano, cerrar esa UI debe detener el proceso administrado cuando sea tecnicamente posible.
- El apagado plebeyo debe permanecer visible, gratuito e inmediato mientras suena el audio.
- La app restaura el volumen multimedia previo y libera el foco de audio al apagar, terminar, salir o cerrarse.
