# Decisiones tecnicas

No borrar decisiones anteriores. Si una decision cambia, agregar una nueva entrada que indique cual reemplaza.

## D-001 - Memoria persistente del proyecto

- Estado: vigente.
- Fecha: 2026-08-29.
- Decision: usar `.codex/` como fuente de verdad entre sesiones, modelos y agentes.
- Motivo: continuidad independiente del historial del chat.

## D-002 - Android nativo y Compose

- Estado: vigente.
- Fecha: 2026-08-29.
- Decision: implementar Gemidos PREMIUM como aplicacion Android nativa en Kotlin con Jetpack Compose y Material 3, en un repositorio independiente de Linterna Premium.
- Motivo: reutilizar convenciones locales verificadas y controlar de forma directa el audio, el foco multimedia, el volumen y el ciclo de vida.

## D-003 - Apagado gratuito, restauracion de volumen y salida segura

- Estado: vigente.
- Fecha: 2026-08-29.
- Decision: mantener un boton plebeyo gratuito e inmediato; guardar el volumen multimedia anterior y restaurarlo al detenerse el audio, terminar el clip, perder foco, salir o destruirse la actividad. El flujo Premium nunca sera la unica via de apagado.
- Motivo: conservar el chiste sin bloquear el silencio, dejar el telefono a maximo volumen de forma persistente ni reproducir audio fuera de control.

## D-004 - Audio integrado con licencia documentada

- Estado: vigente.
- Fecha: 2026-08-29.
- Decision: integrar `LollipopMoans`, de jshine7/Freesound mediante Pixabay, como efecto incluido bajo la Pixabay Content License; versionar el archivo dentro de la aplicacion con origen, licencia y hash documentados.
- Motivo: aproximarse al audio clasico de bromas sin depender de un enlace remoto ni incorporar un clip viral de autoria incierta.

## D-005 - Cuenta regresiva automatica de diez segundos

- Estado: vigente.
- Fecha: 2026-08-29.
- Decision: iniciar una cuenta regresiva visible de 10 a 0 en cada entrada activa a la app; al llegar a cero, reproducir una sola vez el clip al maximo volumen permitido. Salir cancela la cuenta y volver inicia una nueva.
- Motivo: cumplir el remate pedido y mantener el comportamiento ligado a una pantalla activa y controlable.

## D-006 - Localizacion equivalente a Linterna Premium

- Estado: vigente.
- Fecha: 2026-08-29.
- Decision: conservar las mismas 21 opciones de idioma de Linterna Premium y exigir por prueba que cada catalogo tenga todos los textos visibles de la experiencia de Gemidos PREMIUM.
- Motivo: cumplir la paridad solicitada sin dejar acciones criticas ni mensajes de audio sin traducir.

## D-007 - Separacion demo/Play e integracion operativa

- Estado: vigente.
- Fecha: 2026-08-29.
- Decision: usar `demo` para Billing simulado y anuncios oficiales de prueba, reservar `play` para Google Play Billing, AdMob/UMP y firma comercial, y registrar la app como perfil Android nativo en Apps Dashboard 0.2.11.
- Motivo: permitir QA local seguro sin credenciales ni cobros, manteniendo preparado el camino comercial y el flujo operativo compartido.

## D-008 - API 36 estable y `OldTargetApi` aislado

- Estado: vigente.
- Fecha: 2026-08-29.
- Decision: conservar Android 16 / API 36 como `compileSdk` y `targetSdk`, fijar ese SDK en CI y deshabilitar unicamente `OldTargetApi`; el resto de Android Lint continua con `warningsAsErrors` y `abortOnError`.
- Motivo: API 36 cumple el requisito vigente de Google Play y Android 17 / API 37 sigue documentado como opt-in Preview; el runner conoce API 37 y elevaba esa unica advertencia ambiental a error, aunque el SDK 37 no esta disponible como plataforma estable en los repositorios locales.

## D-009 - Audio suministrado, transformado y en loop

- Estado: vigente; reemplaza la seleccion de audio de D-004 y la reproduccion unica de D-005.
- Fecha: 2026-08-31.
- Decision: integrar solo una version transformada del MP3 suministrado por el usuario: recorte de silencios, tono +3,5 %, tempo neto levemente mas rapido, filtros de paso/EQ, atenuacion y limitador; eliminar metadatos heredados y reproducirla en loop hasta un apagado o evento de ciclo de vida.
- Motivo: usar el audio exacto pedido con una identidad sonora perceptiblemente editada y un loop controlable, sin presentar la transformacion como prueba de licencia ni renunciar a la restauracion segura de volumen/foco.

## D-010 - Contraste plebeyo/Premium y reinicio automatico

- Estado: vigente.
- Fecha: 2026-08-31.
- Decision: mostrar `Se viene...` como unico texto del contador; representar Apagado plebeyo como una accion textual pequena que corta y reinicia inmediatamente en 10 sin mensajes; reservar el estado silenciado visible para Premium y ampliar su celebracion a 3,2 segundos con tragamonedas, luces alternadas, lluvia de particulas y multiples fuegos artificiales.
- Motivo: reforzar el contraste comico pedido, quitar pasos y mensajes innecesarios del flujo gratuito y concentrar el exceso visual en la experiencia Premium.

## D-011 - Finales de linea deterministas

- Estado: vigente.
- Fecha: 2026-08-31.
- Decision: normalizar a LF todo texto versionado mediante `.gitattributes`, conservar CRLF para archivos batch y declarar los recursos binarios de forma explicita.
- Motivo: reproducir de manera estable los controles de formato entre Windows y los runners Linux sin reescrituras masivas ni aceptar finales de linea mixtos.

## D-012 - Celebracion Premium posterior al silencio

- Estado: vigente; reemplaza la duracion visual definida en D-010.
- Fecha: 2026-08-31.
- Decision: conservar el fade y restaurado del audio en 1,5 segundos, pero mantener la celebracion visual superpuesta durante 15 segundos; mostrar una felicitacion localizada y cinco maquinas tragamonedas en total, con cuatro minis distribuidas por la pantalla.
- Motivo: prolongar el remate visual sin prolongar el ruido ni retrasar la recuperacion del volumen y foco multimedia del dispositivo.

## D-013 - Pista original para la celebracion Premium

- Estado: vigente.
- Fecha: 2026-08-31.
- Decision: acompanar la celebracion con una pista Ogg Vorbis mono de 15 segundos sintetizada para el proyecto, compuesta por clics de carrete, monedas/campanillas y cinco fanfarrias; reproducirla una vez, a volumen relativo moderado, sin tomar muestras externas y con detencion explicita al pausar, destruir, reiniciar o restablecer Premium.
- Motivo: agregar sonido de tragamonedas y premios sin depender de material de terceros, sin repetir decodificacion de multiples efectos cortos y sin comprometer el restaurado seguro del audio principal.

## D-014 - Festejos puntuales sin sonido constante

- Estado: vigente; reemplaza la composicion sonora definida en D-013.
- Fecha: 2026-08-31.
- Decision: quitar toda base mecanica/monedas repetitivas; conservar cinco fanfarrias de premio y acompanarlas con cinco rafagas puntuales de aplausos sintetizados y vitores generados mediante voces locales de Windows (`Hurra`, `Vamos`, `Premio`, `Bravo` y `Woohoo`), dejando aproximadamente un segundo de silencio real entre bloques.
- Motivo: preservar el sonido de ganar aprobado por el usuario, eliminar la textura constante rechazada y hacer que cada evento de festejo tenga contraste y descanso audible.

## D-015 - Siete efectos elegidos en tandas con descansos

- Estado: vigente; amplia D-014 sin sustituir sus fanfarrias ni restaurar la base constante.
- Fecha: 2026-09-01.
- Decision: sumar a la composicion previa los candidatos Mixkit 2 a 8 (531, 459, 437, 2012, 2011, 1934, 1928), excluyendo expresamente 1/462. Distribuirlos en cinco tandas durante 15 segundos: como maximo un efecto de casino y dos humanos nuevos simultaneos, recortes de unos dos segundos para clips largos, gritos cortos completos, fades y descansos. Mantener reproductor, apagado/restaurado y animaciones intactos.
- Motivo: incorporar la seleccion exacta del usuario sin amontonar todos los sonidos. El mismo pedido se envio a la tarea de Linterna Premium para implementacion independiente.
- Trazabilidad: guardar fuentes/hashes/tiempos en un manifiesto, verificar sus limites con pruebas y conservar los MP3 fuente fuera de Git. Mixkit autoriza productos creativos bajo condiciones; documentar sus restricciones y revisar compatibilidad del contenido sugestivo antes de distribuir fuera de pruebas privadas.
- Validacion: usar Node 24, ya fijado por CI, para exigir 100 % de lineas/ramas/funciones del validador; comprobar localmente con FFmpeg duracion, picos y descansos del OGG final. No agregar descargas ni FFmpeg al CI rapido.

## D-016 - Icono completo dentro de las mascaras Android

- Estado: vigente.
- Fecha: 2026-09-01.
- Decision: conservar el dibujo de boca y dos ondas, escalado uniformemente al 70 % sobre el centro 54/54 y trasladado -3/-1 dp. Reutilizar el mismo foreground en iconos adaptativos, variantes legacy y splash; quitar los dibujos de linterna que permanecian en los recursos pre-API 26.
- Motivo: la onda exterior alcanzaba 43,57 dp desde el centro y se recortaba con la mascara circular de radio 36 dp. El dibujo corregido, incluyendo trazos y antialiasing, alcanza 27,49 dp: queda dentro del circulo seguro de radio 33 dp recomendado por Android. La correccion no requiere rasterizar ni regenerar el diseño.
- Verificacion: render local del vector antes/despues con mascaras circular y rectangular redondeada, comprobacion de todos los pixeles del simbolo contra el circulo seguro y contrato de escala/ondas/referencias compartidas en `tools/verify-project.mjs`.
- Referencia: https://developer.android.com/develop/ui/compose/system/icon_design_adaptive

## D-017 - Festejos completos y continuos durante 15 segundos

- Estado: vigente; reemplaza los descansos, recortes y fades definidos en D-014 y D-015, pero conserva su seleccion de sonidos y la exclusion de 1/462.
- Fecha: 2026-09-01.
- Decision: reproducir cada aparicion de los siete efectos Mixkit desde el inicio hasta su final natural, repetir completos los candidatos 8, 3 y 5, y cruzar las transiciones para cubrir de 0 a 15 segundos sin pausas audibles. Conservar las fanfarrias, vitores y aplausos generados; iniciar la primera fanfarria en cero y agregar un aplauso final. Limitar el solapamiento a cuatro efectos aprobados, tres humanos y dos de casino; los cruces de casino duran como maximo 250 ms.
- Motivo: el usuario aprobo los sonidos pero rechazo los silencios entre ellos y pidio que la fiesta no se interrumpa durante toda la animacion. El mismo requerimiento se envio a la tarea `Diseña Linterna Premium` para su implementacion independiente.
- Validacion: el plan impide recortes, offsets, huecos y exceso de simultaneidad; la prueba acustica exige senal audible en 300 ventanas consecutivas de 50 ms y en cada segundo de la pista.

## D-018 - Publicacion source-only autorizada (2026-09-07)

- El propietario pide hacer publico Gemidos Premium y autoriza expresamente limpiar/re-escribir el historial, retirar audios y rutas personales y mantener los recursos locales.
- Respaldo privado completo verificado antes de filtrar los nueve commits; retirar ambos recursos de audio de todas las revisiones y sustituir la ruta local de perfil por project-root. No publicar el respaldo ni hashes de commits anteriores que faciliten acceder a copias antiguas.
- Version 0.1.1 / build 2 para el contrato de fuentes publicas. Tests JVM/cobertura/Lint admiten identificadores de recurso no reproducibles, solo sin ambos audios y para cuatro tareas explicitas de calidad. Cualquier build instalable o par de audios incompleto falla antes de ejecutar tareas. Los audios locales conservan hashes y comportamiento; no generar APK.
- Los permisos de audios siguen pendientes para distribucion comercial. La publicacion del codigo no incluye grabaciones ni concede derechos sobre ellas. README, hashes y procedimiento en docs/PUBLIC_SOURCE.md.
- GitHub puede mantener commits antiguos fuera de las referencias reescritas (documentacion oficial sobre removing-sensitive-data-from-a-repository). El propietario autoriza ademas conservar el remoto original como gemidos-premium-private-archive (privado) y publicar el historial limpio en un repositorio nuevo con el nombre/URL originales. Esto reemplaza el push forzado al remoto original; se preservan sus audios, notas y logs fuera del repositorio publico. El nuevo repositorio se crea primero privado para verificarlo antes de cambiar visibilidad.

## 2026-10-03 · Novedades en respuesta al APK

- Decisión del usuario: texto con todos los cambios de cada app después de confirmar el APK; si excede Telegram, el primero responde al archivo y cada continuación al texto anterior. Regla global vigente para todos los proyectos actuales/futuros que distribuyen APKs.
- Un único publicador central cubre los cinco perfiles de Apps Dashboard. Notas revisadas en RELEASE_NOTES.json, identidad app/versión/build validada antes de envío y registro por app/chat/plataforma; congelar partes iniciadas y persistir confirmaciones para continuar fallos sin subir el mismo archivo. No usar mensajes genéricos, logs Git ni inventar entrega auténtica/producción.
- Cambio de comportamiento del dashboard 0.2.16; archivos de notas de los cinco productos son documentación de builds existentes, sin cambios de app ni incremento móvil. No reconstruir ni redistribuir APKs para anunciar los tres recibos confirmados de Nico; completar sus respuestas sobre los originales. Cerrar sólo tickets totalmente implementados y verificados; los retests físicos y proveedores reales pendientes conservan bloqueos.

## 2026-10-16 · Ampliación verificable de idiomas y RTL

- Inventario congelado: 251 candidatos de los listados públicos de Google. Se conservan las 21 opciones/códigos/textos previos, incluidos gn/quz. Se incorporan 229 catálogos automáticos completos: 250 opciones en cada app. No representa todas las lenguas ni recursos de Internet.
- Catálogos locales sin red ni fallback de cadenas; fuentes, entrada efectiva/reformulaciones y hashes en docs/LANGUAGE_INVENTORY.json. La integridad técnica no es revisión nativa. Se bloquea jam: los recursos consultados devolvieron fragmentos fuente que no pueden validarse como criollo sin revisión especializada.
- Separar enum, catálogo puro y SharedPreferences para probar todas las claves y ampliar el umbral JVM del 100% al dominio y localización pura. El adaptador Android, Compose y SDK externos conservan verificación Android fuera de la instrumentación JVM. Lotes de 20 idiomas evitan límites de métodos JVM/DEX.
- Aplicar LocalLayoutDirection al idioma seleccionado, sin depender del idioma del teléfono; preservar UI, audio, variantes y preferencias. Una sola subida de patch/build por entrega. Dashboard original, firma demoRelease y Codex Logs autorizados; no Play, no webs ni CI remoto monitoreado.

- Medición ampliada final: dominio, modelos, contratos y localización pura, seis métricas JaCoCo al 100% (instrucciones, ramas, líneas, métodos, complejidad y clases). Sólo se excluyen SharedPreferences dependiente de Android y `$DefaultImpls` generado por Kotlin. Pruebas de límites del sintetizador verifican recorte defensivo antes/después del búfer; sin modificar audio ni quitar guardas.

## 2026-10-16 · Notas de APK y límite del harness Android

- RELEASE_NOTES debe ordenar builds únicos de mayor a menor, conservar historial y comenzar con la versión/build de app.json. El primer intento rechazado no publicó archivos; una prueba local reproduce el orden incorrecto y ahora forma parte de npm test. Corregir sólo notas/tests no exige nueva versión ni recompilar; compartir los APK existentes y verificar recibos.
- El lote de idioma consta de250 opciones, no certifica traducción nativa ni exhaustividad de Internet. jam sigue bloqueado. Las pruebas puras100% no sustituyen UI/lector/insets ni hardware.
- API36 y API33 locales fallan en System UI antes de poder ejecutar la matriz. Mantener ese harness deshabilitado hasta evidencia de reparación o pedido explícito de retest; no atribuir el ANR a la app ni contar casos como aprobados. Fallback compatible usado: JVM/catálogos, lint, metadatos/dex/firma y recibos del pipeline. Windows SetIsBorderRequired/0x80004002 sigue sin reintentarse.

## 2026-10-16 · Reparación y retest solicitados

- Pedido explícito autoriza retomar el harness Android bloqueado. Diagnosticar System UI y validar ambas apps con ADB/instrumentación, preservando perfiles y datos con respaldos. No reintentar captura nativa Windows incompatible. Sin nuevos envíos Telegram autorizados por este pedido.


## 2026-10-16 · Hallazgos de UI reproducidos

- La captura con texto 200% y paisaje demuestra confirmación Premium sin precio/ayuda visibles y acciones superpuestas en ambas apps. Sustituir el layout fijo de AlertDialog por Dialog con toda la confirmación y acciones en un único flujo desplazable; conservar textos y cancelación.
- El selector marca visualmente el idioma pero AccessibilityNodeInfo.selected es false. Exponer selected por opción; conservar nombre, idioma y preferencia. Barras del sistema siempre claras sobre el tema oscuro. Gemidos identifica el apagado gratuito como botón accesible.
- Snapshot restore confirmado incompatible en este entorno: ADB permanece offline tras restaurar premium-systemui-ready; no repetir sin evidencia de reparación. -prop rechaza propiedades ro.* (sólo qemu.*), por lo que el multiplicador no se aplicó. Datos y respaldos de AVD preservados. Recuperación puntual de System UI comprobada tras arranque; no equivale a corregir el fallo de arranque en frío.

## 2026-10-16 · QA final en ejecución

- Corregidas alturas rígidas: textos de acciones completos con alemán/español al 200%. Gemidos mueve apagado gratuito a barra explícita dentro de safeDrawing; visible durante sonido y celebración, con rol Button y error inmediatamente encima. Cancelar también la coroutine Premium al apagar para evitar que termine sobre el nuevo estado.
- Celebraciones recortadas al contenido seguro del Scaffold; una región live polite anuncia el mensaje completo y oculta símbolos decorativos al lector. Priorizar felicitación visual sobre rodillos en vistas compactas.
- APK candidatos compilados, lint y gates puros100% aprobados. Matriz final:11 perfiles/33 capturas por app y auditoría de geometría sin errores; incluye controles fijos. Escenas compactas200% Linterna pasan ambas orientaciones. Gemidos/RTL/teclado/TalkBack siguen en ejecución, sin contarlos completos. Test de volumen ahora fija y comprueba baseline5 en el AVD aislado, no presupone estado de un proceso de test forzado.

## 2026-10-16 · Cierre del QA Android

- Usar AOSP API36 default/x86_64 rev2 en Premium_QA_API36, sólo emulador y paquetes demo.debug. Launcher con preflight de memoria/compiladores; stop/framework/ro.hw_timeout_multiplier=3/start verificado, tiempos finitos y animator1. Dos arranques fríos funcionan; no equivale a certificar rendimiento de producción. No se cierra ninguna app del usuario.
- Perfil Google Play original sigue ANR KeyguardService20002ms: ro.debuggable0 y sin servicio console impiden aplicar la configuración root probada. La ruta documentada -shell tampoco está disponible en esa imagen; deshabilitada sin nuevos reintentos hasta evidencia concreta de reparación. Restaurar config respaldado, conservar datos/default_boot y eliminar sólo snapshot diagnóstico/helper propios. Windows nativo y restauración de snapshots siguen deshabilitados.
- Once perfiles por app/33 capturas pasan geometría runtime, objetivos44dp y revisión visual de acciones finales; quince RTL por app, en/zu por callbacks reales, último menú, persistencia y teclado aprobados. Escenas compactas200% en ambas orientaciones pasan; una región polite y figuras decorativas ocultas. TalkBack16 real conserva servicios (DONT_SUPPRESS), foco, acciones y rol Button virtual; no afirmar voz/pronunciación ni cobertura lingüística nativa.
- Ajustar tests al contrato real: SharedPreferences.apply persiste de forma asíncrona, checked representa selected del menú y Compose puede exponer Button en un hijo virtual no clicable. Verificar rol alrededor de la etiqueta y actuación en su padre, sin cambiar código sano para satisfacer una suposición del harness.
- Gemidos: reproducir carrera de tarea Premium al apagar en menos de1,5s; cancelar premiumSilenceJob antes de apagar. Regresión falla en APK anterior y pasa con nuevo APK (581ms), cuenta regresiva conservada y volumen5 restaurado. Sin cambiar audios, catálogos,15s de fiesta ni dependencias.
- QA manual optativo fuera de CI; suites puras100% y lint/build locales correctos. Recibos portables públicos y evidencias crudas locales ignoradas. Una sola subida patch/build por producto y entrega; sin APK Telegram nuevo ni monitoreo CI. Ambos AVD detenidos, accesibilidad/volumen/insets de QA restaurados.

## 2026-10-17 · Preparación del original sin arranque

- El usuario respondió «Seguí con las tareas que no requieren iniciar el AVD» ante el preflight con menos de2 GiB libres; la medición llegó a unos400 MiB. No arrancar ahora ningún AVD ni cerrar aplicaciones ajenas. La reparación y el retest original quedan BLOCKED por esta restricción y recursos, sin nueva automatización o monitor.
- Respaldar íntegro AppAlimentos_Pixel_API36, identidad y Emulator36.6.11 en %LOCALAPPDATA%/CodexAvdBackups/AppAlimentos_Pixel_API36-20261017. Contrastar SHA-256 del userdata y configuración previa. Actualizar componente estable a37.2.12: notas oficiales37.1.11 corrigen unidades RAM/heap (526671872), aplicable a2G/228M. Es candidato concreto; no demuestra corregir ANR sin arranque.
- Desactivar sólo Quick Boot/snapshots automáticos en config.ini, con hardware/imagen GooglePlay API36rev7/datos preservados. No reintentar -shell, -prop ro.*, snapshots ni captura Windows incompatible. No usar preparación root AOSP en la imagen user.
- Harness con selección explícita de los dos AVD autorizados, comparación de nombre real y serial, exclusión de teléfonos, paquete demo.debug propio y metadatos de origen por captura. Ocho tests offline por repo, también bajo Python -O; las suites Android completas requieren Python normal. No cambian producto/versión/APK.
- Mantener recibo AOSP16/10 como histórico; original0/11 perfiles,0/33 capturas,0/15 RTL por app y0/2 boots en esta ejecución. Informar límites exactos en docs/AVD_GOOGLE_PLAY_REPAIR.md, no aprobar por preparación offline. JVM/JaCoCo/lint/build no repetidos por RAM; baseline anterior no se presenta como nuevo.

- Reconciliación posterior17/10 13:55: se observó instancia original -read-only/puerto5582 creada13:41; no consta lanzamiento en historial de esta tarea y propietario no confirmado. Dejar intacta por exclusión de procesos ajenos; no usar para QA ni afirmar ambos AVD detenidos. Esta observación corrige el estado supuesto anterior, sin aprobar reparación.

## 2026-10-19 · Librerías Android compatibles

- Pedido explícito de Lucas desde En la misma: actualizar librerías en todas las apps donde sea compatible. Entrega 0.1.4/5 con seis familias estables, conservando minSdk24/compileSdk36, AGP8.13.2, Kotlin2.3.21 y Compose2026.06.01. Billing9.1.0 y UMP4.0.0 ya coinciden con últimos estables del registro Google.
- Inspección oficial de AAR/POM: Activity1.13/Core1.18 requieren compile36/AGP8.9.1; Splash1.2 compile35/AGP8.6; Fragment1.9.1/Lifecycle2.11 compile34/AGP8.1.1. Ads25.5 sube minSdk a24, ya existente en ambas apps. Core1.19.1 y Compose2026.08/09 requieren compile37/AGP9.1: no forzar migración de toolchain. Registro Maven no publica BOM2026.07; Junio01 es el último compatible inspeccionado.
- Changelogs oficiales: https://developer.android.com/jetpack/androidx/releases/activity ; https://developer.android.com/jetpack/androidx/releases/core ; https://developer.android.com/jetpack/androidx/releases/fragment ; https://developer.android.com/jetpack/androidx/releases/lifecycle ; https://developers.google.com/admob/android/rel-notes . Validar Node/JVM/JaCoCo100%/lint/build antes de publicar; no AVD, servicios pagos, Telegram o tiendas.

## 2026-10-19 · Límite transitivo de Lifecycle

- Sustituye sólo la selección Lifecycle2.11.0 del bloque anterior: aunque runtime-android declara compile34, su alineación de grupo fuerza lifecycle-runtime-compose-android2.11.0, que requiere compile37/AGP9.1. Gradle checkDemoDebugAarMetadata lo rechazó antes de compilar. Seleccionar Lifecycle2.10.0, cuyo AAR Compose requiere compile36/AGP8.9.1, y repetir sólo tras esta corrección concreta. No elevar SDK/AGP ni desactivar el guard de metadatos. Las otras cinco familias conservan versiones candidatas.


## Metadatos aclarados y validación · 2026-10-19

- Aclaración del bloque anterior: runtime-compose-android2.10.0 declara minCompile35/AGP8.6; se transcribió36/8.9.1 por error. Gradle verifica toda la resolución con compile36/AGP8.13.2.2.11 se rechazó por requerir SDK37. Las seis familias seleccionadas pasan tests/lint/build sin modificar toolchain, minSdk24, permisos ni lógica de audio/compra. Billing9.1/UMP4.0 ya actuales.
- Revisión de corrección, simplicidad, arquitectura, seguridad y rendimiento sin hallazgos adicionales.19Node,24JVM y seis métricas JaCoCo1.00 del alcance puro; plan sonoro Node100% líneas/ramas/funciones. APK demoDebug local con audio privado aprobado, nunca versionado. Secret Scanning/Push Protection enabled verificados19/10. No trasladar cobertura al hardware/UI/SDK.
