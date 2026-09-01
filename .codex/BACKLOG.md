# TODO

- [P1] Configurar Play Console, producto Premium, AdMob, politica publica y pruebas en telefono real antes de una publicacion comercial.
- [P2] Revisar el contenido y la clasificacion etaria exigida por Google Play antes de distribuir fuera de un grupo privado de pruebas.
- [P2] Antes de distribuir fuera de pruebas privadas, confirmar compatibilidad del contenido sugestivo con las condiciones de uso aceptable de Mixkit/Envato para los nuevos efectos; no asumir que una licencia gratuita carece de restricciones de uso.
- [P2] Activar GitHub Secret Scanning y Push Protection [BLOCKED: el repositorio es privado y pertenece a la cuenta personal `ldebortoli`; la API de GitHub devolvio HTTP 422 `Secret scanning is not available for this repository` y `security_and_analysis: null`, por lo que el plan/tipo de repositorio actual no ofrece ambas protecciones].

# IN PROGRESS

No hay tareas en curso.

# DONE

- [2026-09-01] Convertir la celebracion Premium en una secuencia continua de 15 segundos: reproducir completos los siete efectos aprobados, repetir 8/3/5 y solaparlos sin pausas ni cortes, conservar los sonidos anteriores con fanfarria inicial y aplauso final, y enviar la misma orden a `Diseña Linterna Premium`. Mezcla de 157.605 bytes validada en 300 ventanas consecutivas de 50 ms sin silencios y regenerada dos veces byte a byte; 14 pruebas del plan, 18 Android, cobertura 100 % de ambos alcances y lint correctos. Sin APK ni seguimiento de CI.
- [2026-09-01] Corregir el icono: reducir el simbolo al 70 %, centrarlo y conservar ambas ondas dentro de la zona segura; unificar variantes legacy/adaptativas y splash con el mismo foreground. Comparacion visual circular/redondeada y prueba de pixeles: radio maximo 27,49 dp frente al limite seguro 33 dp. Contratos, 12 pruebas del plan, 18 Android, cobertura 100 % en los alcances existentes y lint correctos. Sin APK ni seguimiento de CI.
- [2026-09-01] Integrar los siete efectos Mixkit aprobados (2 a 8, excluir 1/462) sobre los sonidos Premium anteriores, por cinco tandas con descansos y un solo premio/casino simultaneo; documentar fuentes/licencia/hashes y conservar MP3 fuera de Git. Validar 12 pruebas del plan, 18 Android, cobertura 100 % de ambos alcances, lint, picos/silencios, regeneracion byte a byte y checkout del indice con core.autocrlf=true. Enviar el mismo pedido a `Diseña Linterna Premium`, ya activa. Sin APK ni seguimiento de CI.
- [2026-08-31] Rehacer la pista Premium sin sonido constante: conservar cinco fanfarrias de premio, agregar `Hurra`, `Vamos`, `Premio`, `Bravo`, `Woohoo` y cinco rafagas de aplausos, dejar silencios reales entre bloques y versionar un generador determinista; verificar contratos, 18 pruebas, cobertura 100% y lint local sin generar APK.
- [2026-08-31] Crear e integrar una pista Ogg Vorbis original de 15 segundos con clics de tragamonedas, monedas/campanillas y cinco fanfarrias de premio; sincronizarla con Premium, detenerla al salir/reiniciar/restablecer, documentar hash/procedencia y verificar contratos, 18 pruebas, cobertura 100% y lint local sin generar APK.
- [2026-08-31] Extender Apagado Premium: conservar el silencio/restaurado a los 1,5 segundos, mantener 15 segundos de animacion, mostrar felicitacion en las 21 opciones de idioma y sumar cuatro tragamonedas distribuidas ademas de la central; verificar contratos, 18 pruebas, cobertura 100% y lint local sin generar APK.
- [2026-08-31] Integrar una edicion transformada del MP3 suministrado y reproducirla en loop; dejar `Se viene...` como unico texto del contador; convertir Apagado plebeyo en texto pequeno con reinicio automatico; ampliar Premium con tragamonedas, luces, 120 particulas y cinco fuegos artificiales; normalizar finales de linea; verificar contratos, 18 pruebas, cobertura 100% y lint local sin generar APK.
- [2026-08-29] Corregir el CI inicial: conservar API 36 estable, aislar solo `OldTargetApi` frente al SDK 37 Preview conocido por el runner, mantener todos los demas avisos como errores y revalidar contratos, 18 pruebas, cobertura 100% y lint local.
- [2026-08-29] Entregar Gemidos PREMIUM 0.1.0: proyecto Android independiente, contador 10 a 0, audio licenciado al maximo con restauracion, apagados Premium/plebeyo, 21 idiomas, documentacion, repositorio privado e integracion Apps Dashboard 0.2.11; verificar 18 pruebas, cobertura 100%, lint y controles de identidad/version sin generar APK.
- [2026-08-29] Inicializar la memoria persistente del proyecto.
