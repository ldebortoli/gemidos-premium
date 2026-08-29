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
