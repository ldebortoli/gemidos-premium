import assert from "node:assert/strict";
import test from "node:test";
import { plan, validateCelebrationPlan } from "./premium-celebration-plan.mjs";

test("Los siete sonidos aprobados cubren los 15 segundos completos y sin pausas", () => {
  assert.equal(validateCelebrationPlan(plan), plan);
});

const rejects = (name, mutate, message) => test(name, () => {
  const changed = structuredClone(plan);
  mutate(changed);
  assert.throws(() => validateCelebrationPlan(changed), message);
});

rejects("Rechaza el candidato 1", (value) => { value.sources[0].candidate = 1; }, /aprobados/);
rejects("Rechaza la ovacion grande aunque se renumere", (value) => { value.sources[0].id = 462; }, /rechazada/);
rejects("No permite omitir un festejo aprobado", (value) => { value.events = value.events.filter((event) => event.candidate !== 5); }, /deben aparecer/);
rejects("No permite huecos ni siquiera al comienzo", (value) => { value.events[0].startMs = 1; }, /silencios/);
rejects("No recorta los sonidos de festejo", (value) => { value.events[0].durationMs = 5039; }, /recortar/);
rejects("No salta el comienzo de ningun festejo", (value) => { value.events[0].offsetMs = 1; }, /inicio/);
rejects("No extiende el audio mas alla de los 15 segundos", (value) => { value.events[5].startMs = 8601; }, /extender/);
rejects("No amontona mas de cuatro sonidos", (value) => { value.events.push({ ...value.events[7], startMs: 8800 }); }, /cuatro/);
rejects("No amontona mas de tres festejos humanos", (value) => { value.events.push({ ...value.events[4], startMs: 10560 }); }, /tres festejos/);
rejects("Solo cruza brevemente las transiciones de casino", (value) => { value.events[2].startMs = 4000; }, /brevemente/);
rejects("No permite fuentes fuera de la carpeta de cache", (value) => { value.sources[0].file = "../audio.mp3"; }, /rutas externas/);
rejects("Exige ganancia audible y acotada", (value) => { value.events[0].gain = 0; }, /Ganancia/);
rejects("Exige tiempos enteros", (value) => { value.events[0].startMs = 0.5; }, /enteros/);
