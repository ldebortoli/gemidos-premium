import assert from "node:assert/strict";
import { spawnSync } from "node:child_process";
import { fileURLToPath } from "node:url";
import { plan, validateCelebrationPlan } from "./premium-celebration-plan.mjs";

validateCelebrationPlan(plan);
const audio = fileURLToPath(new URL("../apps/mobile/android/app/src/main/res/raw/premium_slot_celebration.ogg", import.meta.url));
const decoded = spawnSync("ffmpeg", [
  "-v", "error", "-i", audio, "-ac", "1", "-ar", String(plan.sampleRate), "-f", "f32le", "pipe:1",
], { maxBuffer: 8 * 1024 * 1024, windowsHide: true });
if (decoded.error) throw decoded.error;
assert.equal(decoded.status, 0, decoded.stderr.toString());
const samples = decoded.stdout.length / 4;
assert.ok(Math.abs(samples / plan.sampleRate - 15) < 0.04, "El OGG decodificado debe durar 15 segundos (tolerancia del codec: 40 ms)");
let peak = 0;
for (let index = 0; index < samples; index++) {
  const sample = decoded.stdout.readFloatLE(index * 4);
  assert.ok(Number.isFinite(sample));
  peak = Math.max(peak, Math.abs(sample));
}
assert.ok(peak < 0.95, "La mezcla no debe saturar incluso tras la compresion Vorbis");
function rms(startMs, endMs) {
  const start = Math.ceil(startMs * plan.sampleRate / 1000);
  const end = Math.min(samples, Math.floor(endMs * plan.sampleRate / 1000));
  let sum = 0;
  for (let index = start; index < end; index++) sum += decoded.stdout.readFloatLE(index * 4) ** 2;
  return Math.sqrt(sum / (end - start));
}
for (const [index, burst] of plan.bursts.entries()) {
  assert.ok(rms(burst.startMs, burst.endMs) > 0.02, `La tanda ${index + 1} debe ser audible`);
  const next = plan.bursts[index + 1];
  if (next) assert.ok(rms(burst.endMs + 100, next.startMs - 100) < 0.0018, `El descanso ${index + 1} debe ser silencio real`);
}
console.log(`Audio verificado: ${(samples / plan.sampleRate).toFixed(3)} s decodificados, pico ${(20 * Math.log10(peak)).toFixed(1)} dBFS, cinco tandas audibles y cuatro descansos sin fondo.`);
