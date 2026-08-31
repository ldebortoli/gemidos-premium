# Gemidos PREMIUM

Aplicación Android humorística inspirada en la experiencia de Linterna PREMIUM. Al abrirla muestra una cuenta regresiva de 10 a 0 y, al llegar a cero, reproduce un clip de gemidos integrado al máximo volumen multimedia permitido por el dispositivo.

La salida gratuita nunca queda bloqueada: **Apagado plebeyo** corta el audio de inmediato. **APAGADO PREMIUM** ofrece una compra única y, cuando está activo, aplica un fade ceremonial con confeti. Al detenerse, perder foco, cambiar de app o cerrar la actividad, se libera el foco de audio y se restaura el volumen multimedia anterior.

## Alcance de la versión 0.1.0

- Android nativo con Kotlin, Jetpack Compose y Material 3.
- Cuenta regresiva automática de diez segundos.
- Reproducción local en loop, sin red, de un MP3 editado de 9,86 segundos.
- Volumen multimedia temporal al máximo, foco transitorio y restauración idempotente.
- Apagado plebeyo como texto pequeño: corta gratis y reinicia la cuenta automáticamente, sin pantalla intermedia.
- Apagado Premium extragrande con fade, tragamonedas, luces, confeti y múltiples fuegos artificiales.
- Compra simulada en `demo`; Google Play Billing real preparado en `play`.
- Banner de prueba en `demo`; AdMob/UMP preparado en `play`.
- Selector persistente con las 21 opciones de idioma de Linterna PREMIUM.
- Tema oscuro sobrio, contenido desplazable, objetivos táctiles amplios y etiquetas accesibles.

## Estructura

- `apps/mobile/android/`: aplicación Android nativa.
- `apps/mobile/app.json`: identidad portable y versión usada por los controles locales.
- `tools/`: validaciones de proyecto, versión y ejecución portable de Gradle.
- `docs/`: audio/licencia, privacidad, configuración comercial y contrato de diseño.
- `.codex/`: memoria persistente del proyecto.

## Requisitos

- Node.js 20 o posterior.
- Java 17.
- Android SDK con API 36 para compilar.
- Para `demoRelease`, `APPS_DASHBOARD_ANDROID_TEST_KEYSTORE_PATH` debe apuntar al keystore QA local estable.

## Validación local

```powershell
npm test
npm run test:android
npm run coverage
npm run lint
```

`npm run coverage` exige 100% de instrucciones, ramas, líneas y métodos en el dominio cubierto. Las pruebas Android y lint usan `demoDebug`; no necesitan Play Console ni AdMob reales.

## APK y variantes

- `demoDebug`: desarrollo local, compra simulada e IDs oficiales de anuncios de prueba.
- `demoRelease`: APK autónoma de QA, no depurable y firmada con la identidad estable del Apps Dashboard.
- `playRelease`: distribución futura mediante Google Play, con Billing, AdMob, consentimiento y firma de producción.

La generación nativa se inicia desde Apps Dashboard. No se construye una APK como efecto secundario de cambios ordinarios; `npm run build:apk` se reserva para un pedido explícito o para validar el pipeline.

## Seguridad, privacidad y límites

- No solicita cámara, micrófono, contactos, ubicación ni almacenamiento.
- No graba audio: solo reproduce el recurso integrado.
- El contenido vocal es sugerente y debe declararse correctamente en la clasificación de contenido antes de una publicación comercial.
- El archivo actual fue suministrado por el propietario del proyecto y transformado, pero esa edición no constituye por sí sola una autorización de derechos; hay que confirmar su licencia antes de distribuir públicamente.
- La app modifica temporalmente el volumen multimedia global; guarda el valor anterior y lo restaura al finalizar o salir.
- Android, controles parentales, límites de volumen seguro o políticas del fabricante pueden impedir alcanzar el máximo físico.
- Compras y anuncios quedan detrás de los SDK oficiales de Google. No se versionan credenciales, keystores ni IDs publicables.

## Audio

El recurso integrado y su hash se documentan en [`docs/AUDIO_LICENSE.md`](docs/AUDIO_LICENSE.md). No se redistribuye como descarga independiente.

## Publicación pendiente

Antes de publicar: configurar Play Console y AdMob, completar Data safety y clasificación de contenido, publicar una política de privacidad final, inyectar IDs/firma de producción y validar audio, restauración de volumen y ciclo de vida en un teléfono Android real.
