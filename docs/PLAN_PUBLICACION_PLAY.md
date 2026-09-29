# Plan de publicación de Gemidos PREMIUM

Revisado el 2026-09-29. Estado: el código es público, pero la app no está en Google Play ni tiene una APK/AAB publicable. `demoRelease` es solo QA. La variante comercial usa `com.gemidospremium.app`, API 36, Google Play Billing 9.1.0 y AdMob/UMP. `playRelease` todavía no tiene una clave de subida de producción configurada y exige dos audios privados locales.

## Bloqueos previos

1. El audio principal `prank_moans.mp3` procede de un archivo entregado sin prueba de autoría o licencia. Editarlo o mantenerlo fuera del repositorio no concede derecho a distribuirlo en una app comercial. El propietario debe aportar permiso/licencia explícita para distribución comercial en una app, o sustituirlo por una grabación propia u otra fuente con derechos documentados.
2. Google Play prohíbe apps que contienen o promueven contenido sexual; una clasificación etaria alta no sustituye esa política. Por el nombre y el audio vocal sugestivo, la versión actual tiene un riesgo material de rechazo. Revisar el sonido y la presentación y, si es necesario, convertir el chiste en audio inequívocamente no sexual antes de subirlo. Google decide la admisión final.
3. La mezcla Premium contiene efectos Mixkit. La licencia de Sound Effects permite integrarlos en un producto creativo, pero el proyecto ya registra una revisión pendiente de las condiciones de uso aceptable de Envato para contenido adulto/sugestivo. Resolverla antes de distribución pública, o regenerar la mezcla con efectos propios o de licencia inequívoca.
4. La app sube automáticamente el volumen multimedia al máximo tras la cuenta regresiva. Validar en teléfono real y mostrar una advertencia/consentimiento claro antes de esta acción si la revisión de experiencia y seguridad lo exige; la función de apagado gratuito y restauración de volumen debe seguir funcionando.

## Acciones del propietario

1. Usar la misma cuenta propia de [Play Console](https://play.google.com/console/signup) que para Linterna PREMIUM. El alta cuesta USD 25 una sola vez por cuenta. Completar identidad, perfil de pagos, datos fiscales, banco y verificación de dispositivo según el tipo de cuenta. Una cuenta personal monetizada muestra nombre legal, país, email y dirección completa; una organización requiere D-U-N-S.
2. Decidir y documentar los derechos del audio principal y aprobar el contenido que efectivamente se enviará a Google. No subir el audio actual a una pista de Play mientras esto esté sin resolver.
3. Crear la app **gratuita** Gemidos PREMIUM con el paquete definitivo `com.gemidospremium.app` y una ficha que describa honestamente el contenido final. La monetización se realiza con un producto dentro de la app.
4. En **Monetizar con Play > Productos > Productos únicos**, crear `premium_silence_pack` como compra única permanente con una sola opción **Comprar**, precio y regiones elegidos; activarlo. No configurar suscripción ni alquiler.
5. En la misma cuenta de AdMob, registrar Gemidos como otra app Android y crear su propia unidad banner. Los IDs de app y banner son distintos de los de Linterna. Configurar pagos/consentimiento, un sitio HTTPS con política y `app-ads.txt`, y un email de soporte.
6. Si Play Console es una cuenta personal creada después del 13-11-2023, conseguir 12 testers adheridos a la prueba cerrada de **Gemidos** durante 14 días continuos. Pueden ser las mismas personas de Linterna si aceptan ambas invitaciones. Agregar aparte testers de licencia para compras de prueba.

## Trabajo técnico antes de enviar a revisión

1. Sustituir o acreditar los audios; actualizar procedencia, hashes y pruebas. Completar la política `docs/PRIVACIDAD.md` y la ficha Play, incluyendo clasificación IARC, público objetivo, anuncios, seguridad de datos, contacto, países y acceso para revisión.
2. Configurar los IDs AdMob de `docs/CONFIGURACION_GOOGLE.md` fuera de Git y una **clave de subida de producción** propia para Gemidos. La firma QA de `demoRelease` no se reutiliza. Activar Play App Signing y construir un AAB `playRelease` firmado con los dos audios aprobados.
3. Ejecutar pruebas, cobertura, Lint y validación acústica local; probar audio, volumen, salida/foco y restauración en teléfono real. Subir a pista interna, probar compra, cancelación, pendiente, restauración y retirada de anuncios con tester licenciado.
4. Completar prueba cerrada, pedir acceso a producción, atender la revisión de políticas y lanzar solo tras aprobación. Vincular la ficha Play en AdMob y verificar `app-ads.txt` para que los anuncios puedan servir plenamente.

## Costos y límites

- No hay una segunda tasa de alta de Play por esta app si ambas pertenecen a la misma cuenta. Las compras integradas tienen comisiones según mercado/programa; AdMob no añade tasa de alta. Un sitio HTTPS puede alojarse gratuitamente, con dominio propio opcional.
- Los derechos de audio pueden implicar un costo o exigir reemplazar material. No hay precio verificable sin conocer al titular y sus condiciones.
- La API 36 del proyecto cumple el requisito de Google Play vigente desde el 31-08-2026. Una revisión de políticas puede rechazar la versión actual por contenido, aunque pase tests y clasificación etaria.

## Fuentes oficiales

- [Alta de Play Console](https://support.google.com/googleplay/android-developer/answer/6112435)
- [Datos públicos de desarrollador](https://support.google.com/googleplay/android-developer/answer/13628312)
- [Contenido inapropiado y sexual](https://support.google.com/googleplay/android-developer/answer/9878810)
- [Clasificación por edades](https://support.google.com/googleplay/android-developer/answer/9898843)
- [Prueba cerrada](https://support.google.com/googleplay/android-developer/answer/14151465)
- [Productos únicos](https://support.google.com/googleplay/android-developer/answer/16430488)
- [Firma de apps](https://support.google.com/googleplay/android-developer/answer/9842756)
- [Verificación AdMob con app-ads.txt](https://support.google.com/admob/answer/14538460)
- [Licencia de Mixkit](https://mixkit.co/license/#sfxFree)
