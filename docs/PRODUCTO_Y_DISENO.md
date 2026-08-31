# Producto y diseño implementado

## Propósito

Gemidos PREMIUM es una broma privada y controlable: crea diez segundos de expectativa y reproduce un clip vocal sugerente a volumen alto. El remate visual contrapone un botón Premium deliberadamente enorme con un apagado plebeyo gratuito.

## Recorrido principal

1. La app abre en `COUNTDOWN` mostrando 10 y reduce un número por segundo.
2. Al llegar a 0 pasa a `PLAYING`, solicita foco multimedia, guarda el volumen anterior, pide el máximo permitido y reproduce el MP3 integrado en loop.
3. `Apagado plebeyo` detiene el reproductor, restaura volumen/foco y vuelve directamente a `COUNTDOWN` en 10, sin aviso, pantalla de apagado ni botón de reinicio.
4. `APAGADO PREMIUM` abre confirmación si no hay licencia; en demo la simula sin cobro. Con Premium activo ejecuta un fade de 1,5 segundos, corta y restaura el audio, y mantiene durante 15 segundos una celebración superpuesta con una felicitación localizada, cinco tragamonedas distribuidas, luces perimetrales, lluvia de partículas y cinco ráfagas de fuegos artificiales.
5. El clip se repite hasta apagarlo; al perder foco, cambiar de app o cerrar la actividad, la reproducción termina y el volumen se restaura.
6. Desde la pantalla silenciada se puede reiniciar el contador.

## Estados y fallos

- `COUNTDOWN`: 10 a 0, sin audio.
- `PLAYING`: audio activo y ambos apagados visibles.
- `PREMIUM_SILENCING`: fade de 1,5 segundos; el restaurado final vive en un `finally`. La celebración visual es independiente y continúa hasta completar sus 15 segundos aunque el estado ya sea `SILENCED`.
- `SILENCED`: reservado para el desenlace Premium, audio detenido y reinicio disponible.
- `ERROR`: mensaje recuperable inmediatamente encima de la acción de reinicio.
- Compra: confirmación, cancelación, pendiente, error y restauración mediante Google Play.
- Anuncios: identificadores oficiales de prueba en demo; consentimiento UMP e IDs externos en Play.

## Dirección visual

Tema oscuro con superficies carbón, rosa apagado y dorado mate. El encabezado y la insignia de edición conservan el parentesco conceptual con Linterna PREMIUM sin reutilizar su icono. El símbolo propio combina labios estilizados y ondas de sonido.

La pantalla se desplaza en alturas compactas y texto grande; el menú de idiomas queda anclado a su control, con alto máximo de 360 dp. Los botones usan alturas mínimas superiores a 48 dp y el principal Premium mide 112 dp.

## No objetivos de 0.1.0

- No hay cuentas, backend, telemetría propia ni contenido descargado en runtime.
- No se intenta ignorar los límites de volumen seguro de Android.
- No se reproduce en segundo plano ni se mantiene un servicio persistente.
- No se publica automáticamente en Google Play ni se crea una ficha comercial sin autorización.
