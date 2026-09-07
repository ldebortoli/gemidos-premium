import assert from "node:assert/strict";
import fs from "node:fs";
import { spawnSync } from "node:child_process";
import test from "node:test";

test("Private audio and personal profile paths are absent from tracked sources", () => {
  const listed = spawnSync("git", ["ls-files", "-z"], { encoding: "utf8", windowsHide: true });
  assert.equal(listed.status, 0);
  const files = listed.stdout.split("\0").filter(Boolean);
  assert.ok(files.length > 0);
  assert.equal(files.filter((file) => /\.(mp3|ogg|wav|pcm|flac|m4a)$/i.test(file)).length, 0);
  for (const file of files.filter((file) => /\.(md|json|mjs|ps1|kts|properties|yml)$/.test(file))) {
    assert.doesNotMatch(fs.readFileSync(file, "utf8"), /[A-Za-z]:[\\/]+Users[\\/]+/i, file);
  }
});

test("Only named quality tasks can compile without private audio; installable builds cannot use placeholders", () => {
  const gradle = fs.readFileSync("apps/mobile/android/app/build.gradle.kts", "utf8");
  const allowed = gradle.match(/val sourceOnlyQualityTasks = setOf\(([\s\S]*?)\)/)?.[1];
  assert.ok(allowed);
  assert.deepEqual([...allowed.matchAll(/"([^"]+)"/g)].map((match) => match[1]), [
    "testDemoDebugUnitTest", "jacocoTestReport", "jacocoTestCoverageVerification", "lintDemoDebug",
  ]);
  assert.match(gradle, /requestedTasks\.isNotEmpty\(\) && requestedTasks\.all \{ it in sourceOnlyQualityTasks \}/);
  assert.match(gradle, /privateAudioCount == 1 \|\| \(!privateAudioPresent && !sourceOnlyQualityRun\)/);
  assert.match(gradle, /throw GradleException\("Faltan los audios privados/);
  assert.match(gradle, /if \(!privateAudioPresent\) \{\s*sourceSets\.getByName\("main"\)\.res\.srcDir\("src\/testFixtures\/res"\)/);
  for (const name of ["prank_moans", "premium_slot_celebration"]) {
    assert.match(fs.readFileSync(`apps/mobile/android/app/src/testFixtures/res/raw/${name}.txt`, "utf8"), /NON-PLAYABLE TEST RESOURCE/);
  }
});
