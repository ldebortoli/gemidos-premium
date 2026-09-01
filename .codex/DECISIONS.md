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
