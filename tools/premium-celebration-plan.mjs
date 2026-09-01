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
  assert.equal(value.bursts.length, 5);
  value.bursts.forEach((burst, index) => {
    assert.ok(burst.startMs >= 0 && burst.endMs <= value.durationMs && burst.endMs > burst.startMs);
    if (index > 0) assert.ok(burst.startMs - value.bursts[index - 1].endMs >= 700, "Dejar descanso entre tandas");
  });
  assert.deepEqual([...new Set(value.events.map((event) => event.candidate))].sort((a, b) => a - b), [2, 3, 4, 5, 6, 7, 8], "Todos los audios elegidos deben aparecer");
  for (const event of value.events) {
    const source = sources.get(event.candidate);
    assert.ok(source, "Fuente no aprobada");
    assert.ok([event.startMs, event.offsetMs, event.durationMs].every(Number.isInteger));
    assert.ok(event.offsetMs >= 0 && event.durationMs > 0 && event.offsetMs + event.durationMs <= source.durationMs, "Fragmento fuera del audio fuente");
    assert.ok(event.gain > 0 && event.gain <= 1, "Ganancia fuera de rango");
    assert.ok(value.bursts.some((burst) => event.startMs >= burst.startMs && event.startMs + event.durationMs <= burst.endMs), "No llenar los descansos ni extender la fiesta");
    const active = value.events.filter((other) => other.startMs <= event.startMs && other.startMs + other.durationMs > event.startMs);
    assert.ok(active.filter((other) => sources.get(other.candidate)?.kind === "casino").length <= 1, "Nunca dos premios de casino a la vez");
    assert.ok(active.filter((other) => sources.get(other.candidate)?.kind === "people").length <= 2, "Como maximo dos festejos humanos simultaneos");
  }
  return value;
}

validateCelebrationPlan(plan);
