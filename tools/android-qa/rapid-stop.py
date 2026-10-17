"""Regression: free stop must win over a Premium task already in progress."""
import json
import time
import qa
import matrix

assert qa.SLUG == "gemidos"
qa.verify_emulator()
qa.profile()
qa.shell("wm", "size", "reset")
qa.language("gemidos", "es-AR")
qa.shell("cmd", "media_session", "volume", "--stream", "3", "--set", "5")
fixture = qa.BASE / "premium.xml"
fixture.write_text('<map><boolean name="premium_owned" value="true" /></map>', encoding="utf-8")
qa.adb("push", fixture, "/data/local/tmp/premium-owned.xml")
qa.shell("run-as", qa.pkg("gemidos"), "cp", "/data/local/tmp/premium-owned.xml", "shared_prefs/premium_entitlement.xml")
output = qa.ROOT / "reports" / qa.RUN
try:
    matrix.start("gemidos")
    qa.ensure_rotation("gemidos", 0)
    time.sleep(11)
    for _ in range(12):
        capture = qa.probe()
        premium = [node for node in qa.own("gemidos", capture) if node["text"] == "APAGADO PREMIUM" and node["visible"]]
        if premium:
            break
        qa.scroll("gemidos", capture)
    else:
        raise RuntimeError("Premium action not reachable")
    stop = next(node for node in qa.own("gemidos", capture) if node["text"] == "Apagado plebeyo" and node["visible"])
    started = time.monotonic()
    qa.tap(premium[0])
    time.sleep(.1)
    qa.tap(stop)
    elapsed = round((time.monotonic() - started) * 1000)
    assert elapsed < 1500, "Harness was too slow to exercise the task race"
    time.sleep(2)
    capture = qa.save("gemidos", "rapid-stop")
    text = [node["text"] for node in qa.own("gemidos", capture)]
    assert "Se viene..." in text and "El audio está apagado" not in text, "Late Premium completion overwrote the free stop countdown"
    assert not any(node.get("liveRegion") == 1 for node in qa.own("gemidos", capture)), "Celebration returned after stopping"
    assert "volume is 5 " in qa.shell("cmd", "media_session", "volume", "--stream", "3", "--get")
    (output / "rapid-stop-receipt.json").write_text(json.dumps({"status": "passed", "twoTapsElapsedMs": elapsed, "countdownPreservedAfterTaskDeadline": True}, indent=2), encoding="utf-8")
    print("gemidos RAPID STOP PASS", elapsed, "ms", flush=True)
finally:
    qa.shell("input", "keyevent", 3)
    time.sleep(.5)
    qa.shell("am", "force-stop", qa.pkg("gemidos"))
