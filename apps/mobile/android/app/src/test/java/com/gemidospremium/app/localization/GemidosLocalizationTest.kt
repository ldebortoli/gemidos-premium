package com.gemidospremium.app.localization

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GemidosLocalizationTest {
    @Test
    fun `catalog exposes all twenty one requested options with complete prank copy`() {
        assertEquals(
            listOf(
                "es-AR", "es-ES", "en", "ru", "la", "ja", "it", "fr", "de", "nl",
                "zh-Hans", "zh-Hant", "pt-BR", "pt-PT", "ca", "eu", "gn", "quz",
                "cmn-Hans", "yue-Hant", "ko",
            ),
            GemidosTextCatalog.supportedLanguages.take(21).map(AppLanguage::code),
        )
        assertEquals(
            GemidosTextCatalog.supportedLanguages.size,
            GemidosTextCatalog.supportedLanguages.map(AppLanguage::nativeLabel).toSet().size,
        )
        GemidosTextCatalog.supportedLanguages.forEach { language ->
            assertTrue("Missing ${language.code}", GemidosTextCatalog.missingKeys(language).isEmpty())
            val copy = GemidosTextCatalog.forLanguage(language)
            assertTrue(copy[TextKey.READY_TO_LIGHT].isNotBlank())
            assertTrue(copy[TextKey.TORCH_ON_MAX].isNotBlank())
            assertTrue(copy[TextKey.NORMAL_PLEBEIAN_OFF].isNotBlank())
            assertFalse(copy[TextKey.TURN_ON].contains("linterna", ignoreCase = true))
            assertFalse(copy[TextKey.FLASHLIGHT_ICON].contains("flashlight", ignoreCase = true))
        }
        assertEquals(
            "Se viene...",
            GemidosTextCatalog.forLanguage(AppLanguage.SPANISH_ARGENTINA)[TextKey.READY_TO_LIGHT],
        )
        assertEquals(
            "Felicitaciones por tu apagado Premium.",
            GemidosTextCatalog.forLanguage(AppLanguage.SPANISH_ARGENTINA)[TextKey.PREMIUM_OFF_NOTICE],
        )
    }

    @Test
    fun `language codes accept regions and deterministic fallbacks`() {
        assertEquals(AppLanguage.SPANISH_SPAIN, AppLanguage.fromCode("es_ES"))
        assertEquals(AppLanguage.SPANISH_ARGENTINA, AppLanguage.fromCode("es-MX"))
        assertEquals(AppLanguage.PORTUGUESE_BRAZIL, AppLanguage.fromCode("pt"))
        assertEquals(AppLanguage.PORTUGUESE_PORTUGAL, AppLanguage.fromCode("pt-PT"))
        assertEquals(AppLanguage.CHINESE_SIMPLIFIED, AppLanguage.fromCode("zh-CN"))
        assertEquals(AppLanguage.CHINESE_TRADITIONAL, AppLanguage.fromCode("zh-TW"))
        assertEquals(AppLanguage.CHINESE_TRADITIONAL, AppLanguage.fromCode("zh-Hant-TW"))
        assertEquals(AppLanguage.MANDARIN, AppLanguage.fromCode("cmn"))
        assertEquals(AppLanguage.CANTONESE, AppLanguage.fromCode("yue-HK"))
        assertEquals(AppLanguage.QUECHUA, AppLanguage.fromCode("qu-PE"))
        assertEquals(AppLanguage.SPANISH_ARGENTINA, AppLanguage.fromCode("unsupported"))
        assertEquals(AppLanguage.SPANISH_ARGENTINA, AppLanguage.fromCode(null))
    }

    @Test
    fun `regional and indigenous options use their own audio vocabulary`() {
        assertTrue(
            GemidosTextCatalog.forLanguage(AppLanguage.PORTUGUESE_PORTUGAL)[TextKey.TORCH_ON_MAX]
                .contains("Gemidos"),
        )
        assertTrue(
            GemidosTextCatalog.forLanguage(AppLanguage.CANTONESE)[TextKey.MAX_INTENSITY_HELP]
                .contains("音訊"),
        )
        assertTrue(GemidosTextCatalog.forLanguage(AppLanguage.KOREAN)[TextKey.TURN_ON].contains("카운트다운"))
        assertTrue(GemidosTextCatalog.forLanguage(AppLanguage.GUARANI)[TextKey.TURN_ON].contains("papapy"))
        assertTrue(GemidosTextCatalog.forLanguage(AppLanguage.QUECHUA)[TextKey.TURN_ON].contains("Yupay"))
        assertTrue(GemidosTextCatalog.forLanguage(AppLanguage.LATIN)[TextKey.TURN_ON].contains("Numerationem"))
    }
    @Test
    fun `global country and community languages resolve to complete independent catalogs`() {
        listOf("ar", "ay", "af", "fi", "hi", "uk").forEach { code ->
            val language = AppLanguage.fromCode(code)
            assertEquals(code, language.code)
            val text = GemidosTextCatalog.forLanguage(language)
            assertEquals(language, text.language)
            TextKey.entries.forEach { key -> assertTrue("$code/$key", text[key].isNotBlank()) }
        }
    }
    @Test
    fun `all frozen catalog entries are nonblank and language selection preserves identity`() {
        assertEquals(250, AppLanguage.entries.size)
        AppLanguage.entries.forEach { language ->
            assertEquals(language, AppLanguage.fromCode(language.code))
            val text = GemidosTextCatalog.forLanguage(language)
            assertEquals(language, text.language)
            TextKey.entries.forEach { key -> assertTrue("${language.code}/$key", text[key].isNotBlank()) }
        }
        assertTrue(AppLanguage.fromCode("ar").isRightToLeft)
        assertTrue(AppLanguage.fromCode("he").isRightToLeft)
        assertTrue(!AppLanguage.ENGLISH.isRightToLeft)
    }

    @Test
    fun `region script and unknown language boundaries preserve deterministic resolution`() {
        assertEquals(AppLanguage.SPANISH_ARGENTINA, AppLanguage.fromCode("es"))
        assertEquals(AppLanguage.CHINESE_TRADITIONAL, AppLanguage.fromCode("zh-Hant-CN"))
        assertEquals(AppLanguage.CHINESE_TRADITIONAL, AppLanguage.fromCode("zh-TW-extra"))
        assertEquals(AppLanguage.CHINESE_TRADITIONAL, AppLanguage.fromCode("zh-HK"))
        assertEquals(AppLanguage.CHINESE_TRADITIONAL, AppLanguage.fromCode("zh-MO"))
        assertEquals(AppLanguage.CHINESE_SIMPLIFIED, AppLanguage.fromCode("zh-CN"))
        assertEquals(AppLanguage.MANDARIN, AppLanguage.fromCode("cmn-Hans-CN"))
        assertEquals(AppLanguage.CANTONESE, AppLanguage.fromCode("yue-HK"))
        assertEquals(AppLanguage.FRENCH, AppLanguage.fromCode("fr-CH"))
        assertEquals(AppLanguage.SPANISH_ARGENTINA, AppLanguage.fromCode(""))
        assertEquals(AppLanguage.SPANISH_ARGENTINA, AppLanguage.fromCode(null))
    }

    @Test
    fun `incomplete or blank catalogs fail before being exposed`() {
        assertEquals("Volver", translationCatalog(listOf(TextKey.BACK), "Volver").getValue(TextKey.BACK))
        assertTrue(translationCatalog(emptyList()).isEmpty())
        listOf(emptyArray<String>(), arrayOf("one", "two"), arrayOf(" ")).forEach { values ->
            try {
                translationCatalog(listOf(TextKey.BACK), *values)
                throw AssertionError("Invalid catalog accepted")
            } catch (_: IllegalArgumentException) {
                // Missing, surplus and blank translations are rejected.
            }
        }
    }
}
