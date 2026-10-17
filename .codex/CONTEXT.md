# Gemidos Premium - Contexto del proyecto

## Descripcion general

Aplicacion Android humoristica de reproduccion de audio. Al abrir, muestra una cuenta regresiva visible de 10 a 0; al finalizar solicita foco de audio, lleva temporalmente el canal multimedia al maximo y reproduce en loop un efecto de gemidos integrado. Siempre ofrece un apagado plebeyo gratuito e inmediato y un apagado Premium de presentacion exagerada, sin impedir que el usuario silencie la app.

## Estado detectado

- Ruta: `<project-root>`
- Version de fuente: `0.1.3` (`versionCode` 4); QA AOSP del16/10 completo en su alcance, sin nueva distribución; validación del AVD Google Play original pendiente.
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
- Baseline: 24 pruebas JVM Android, cero fallos/omitidas y 100% instrucciones/ramas/líneas/métodos/complejidad/clases del núcleo puro (dominio, modelos, contratos y localización).
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

## Novedades de APK · regla 2026-10-03

- RELEASE_NOTES.json en la raíz conserva cambios revisados por versión/build y prefijo de artefactos. Apps Dashboard central publica esas notas después de confirmar cada archivo, como respuesta al APK y continuaciones en cadena cuando superan el límite de Telegram; incluye versiones no anunciadas para esa app/chat/plataforma.
- Codex Bot verifica identidad, destino, permisos, archivo y cada respuesta. Credenciales cifradas; recibos y progreso locales fuera de Git; reintentos continúan partes pendientes sin repetir el APK. Sólo avanzar la versión anunciada al confirmar la cadena completa. El transporte no garantiza exactamente una vez si se pierde la respuesta HTTP después de la aceptación remota.
- Preparar una entrada por cada nueva iteración distribuible y mantener el historial. Esta documentación no cambia el comportamiento ni versión del producto móvil; el cambio de publicador se entrega en Apps Dashboard 0.2.16.

- Idiomas desde 0.1.2: 250 opciones persistentes, 21 previas conservadas y 229 catálogos automáticos nuevos, con dirección RTL por selección. Inventario portable en docs/LANGUAGE_INVENTORY.json; no hay revisión nativa afirmada ni fallback de cadenas. jam bloqueado por fragmentos fuente.

- Validación desde ampliación 2026-10-16: 24 pruebas JVM Android sin omitidas; 100% de seis métricas JaCoCo en dominio/modelos/contratos/localización pura, con exclusiones explícitas del adaptador Android SharedPreferences y puentes Kotlin generados. Lint correcto. Catálogos y procedencia/hashes se controlan también desde npm test.

- Última distribución confirmada en Codex Logs: 0.1.2/3 APK767→notas768. Fuente y audios separados, firma QA estable; la validación Android actual está documentada en docs/VALIDACION_ANDROID.md; sin revisión nativa afirmada.

- QA reproducible: tools/android-qa en AVD aislado Premium_QA_API36 (AOSP API36 rev2,3072MB,2vCPU,SwiftShader), root y ro.hw_timeout_multiplier=3 finito, arranque sin snapshots. Compilar primero y emular después.11 perfiles/33 capturas,15 RTL y TalkBack/teclado por app; cobertura pura100% no equivale a cobertura UI. El AVD Google Play original tiene reparación solicitada, con respaldo completo y Emulator37.2.12 preparado17/10, datos/imagen preservados y Quick Boot desactivado. Arranque/retest bloqueados por restricción actual del usuario y memoria libre; no queda apartado como resolución final. Ver docs/AVD_GOOGLE_PLAY_REPAIR.md.
