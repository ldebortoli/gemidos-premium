import assert from "node:assert/strict";
import fs from "node:fs";

export const plan = JSON.parse(fs.readFileSync(new URL("./premium-celebration-plan.json", import.meta.url), "utf8"));

export function validateCelebrationPlan(value) {
  assert.equal(value.durationMs, 15000, "La fiesta debe durar 15 segundos");
  assert.equal(value.sampleRate, 44100);
  assert.ok(value.foundationGain > 0 && value.foundationGain <= 1, "Conservar los festejos existentes sin amplificarlos");
  assert.deepEqual(value.sources.map((source) => source.candidate).sort((a, b) => a - b), [2, 3, 4, 5, 6, 7, 8], "Solo candidatos aprobados 2 a 8");
  assert.equal(new Set(value.sources.map((source) => source.id)).size, 7);
  assert.ok(value.sources.every((source) => source.id !== 462), "No integrar la ovacion rechazada");
  const sources = new Map(value.sources.map((source) => [source.candidate, source]));
  for (const source of value.sources) {
    assert.ok(Number.isInteger(source.id) && source.id > 0);
    assert.ok(["people", "casino"].includes(source.kind));
    assert.equal(source.kind, source.candidate >= 7 ? "casino" : "people");
    assert.match(source.file, /^[a-z0-9-]+\.mp3$/, "Usar solo nombres de archivo, sin rutas externas");
    assert.match(source.sha256, /^[0-9A-F]{64}$/);
    assert.ok(source.durationMs > 0);
  }
  for (const event of value.events) {
    const source = sources.get(event.candidate);
    assert.ok(source, "Fuente no aprobada");
    assert.ok([event.startMs, event.offsetMs, event.durationMs].every(Number.isInteger), "Los tiempos deben expresarse como milisegundos enteros");
    assert.equal(event.offsetMs, 0, "Cada sonido debe comenzar desde el inicio");
    assert.equal(event.durationMs, source.durationMs, "Los festejos no se pueden recortar ni interrumpir");
    assert.ok(event.startMs >= 0 && event.startMs + event.durationMs <= value.durationMs, "No extender la fiesta mas alla de 15 segundos");
    assert.ok(event.gain > 0 && event.gain <= 1, "Ganancia fuera de rango");
  }
  assert.deepEqual([...new Set(value.events.map((event) => event.candidate))].sort((a, b) => a - b), [2, 3, 4, 5, 6, 7, 8], "Todos los audios elegidos deben aparecer");
  const boundaries = [...new Set([0, value.durationMs, ...value.events.flatMap((event) => [event.startMs, event.startMs + event.durationMs])])].sort((a, b) => a - b);
  for (let index = 0; index < boundaries.length - 1; index++) {
    const start = boundaries[index];
    const end = boundaries[index + 1];
    const active = value.events.filter((event) => event.startMs <= start && event.startMs + event.durationMs >= end);
    assert.ok(active.length > 0, "No puede haber silencios durante los 15 segundos");
    assert.ok(active.filter((event) => sources.get(event.candidate).kind === "casino").length <= 2, "Como maximo dos premios de casino durante un solapamiento");
    assert.ok(active.filter((event) => sources.get(event.candidate).kind === "people").length <= 3, "Como maximo tres festejos humanos simultaneos");
    assert.ok(active.length <= 4, "Como maximo cuatro festejos simultaneos");
  }
  const casinoEvents = value.events.filter((event) => sources.get(event.candidate).kind === "casino");
  for (let left = 0; left < casinoEvents.length; left++) {
    for (let right = left + 1; right < casinoEvents.length; right++) {
      const overlap = Math.min(casinoEvents[left].startMs + casinoEvents[left].durationMs, casinoEvents[right].startMs + casinoEvents[right].durationMs) - Math.max(casinoEvents[left].startMs, casinoEvents[right].startMs);
      assert.ok(overlap <= 250, "Los premios de casino solo pueden solaparse brevemente para evitar silencios");
    }
  }
  return value;
}

validateCelebrationPlan(plan);
