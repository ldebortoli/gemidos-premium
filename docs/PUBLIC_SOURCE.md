# Código público, audios privados

El repositorio publica el código de Gemidos PREMIUM, no sus grabaciones. Los dos recursos de audio se retiraron también de las revisiones anteriores antes de abrir el repositorio. El propietario conserva una copia local y un respaldo privado del historial anterior. No se redistribuyen esos respaldos.

## Revisar el código sin audios

`npm test`, `npm run coverage` y `npm run lint` funcionan desde un clon público. Los tests JVM y Android Lint compilan con dos identificadores de recurso de texto, no reproducibles, únicamente cuando faltan los audios privados. No se prueba reproducción física con esos recursos ni se genera una APK. La cobertura de dominio y del plan sonoro mantiene sus umbrales del 100 %.

Gradle rechaza las tareas de ensamblado, instalación y cualquier tarea que no sea la lista explícita de calidad si falta alguno de los audios. Nunca usar estos recursos de texto como audio de la aplicación.

## Compilar una aplicación reproducible

Es necesario contar con autorización suficiente para usar los audios y disponer de los dos recursos privados documentados en `AUDIO_LICENSE.md`:

- `apps/mobile/android/app/src/main/res/raw/prank_moans.mp3`
- `apps/mobile/android/app/src/main/res/raw/premium_slot_celebration.ogg`

Copiarlos localmente, sin agregarlos a Git. Ejecutar `npm test` para comprobar sus hashes y `npm run test:audio` para la prueba acústica opcional con FFmpeg. Después usar el flujo de build documentado en README. Esta publicación no incluye una APK ni habilita distribución comercial de los audios.

Si se usan grabaciones propias o con otra licencia, hay que actualizar las comprobaciones de hash y procedencia correspondientes antes de construir. La publicación de este código no concede derechos sobre grabaciones de terceros.
