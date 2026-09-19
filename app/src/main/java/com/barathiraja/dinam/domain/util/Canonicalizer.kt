package com.barathiraja.dinam.domain.util

import java.security.MessageDigest
import java.util.Locale

object Canonicalizer {

    fun canonicalId(text: String): String {
        val normalized = normalize(text)

        val digest = MessageDigest
            .getInstance("SHA-256")
            .digest(normalized.toByteArray())

        return digest.joinToString("") {
            "%02x".format(it)
        }
    }

    private fun normalize(text: String): String {
        return text
            .lowercase(Locale.US)
            .replace(
                Regex("^\\s*\\d+(?:\\.\\d+)?\\s*"),
                ""
            )
            .replace(
                Regex(
                    "^\\s*(kg|kgs|g|gram|grams|mg|ml|l|litre|litres|liter|liters|"
                            + "pack|packs|packet|packets|box|boxes|bottle|bottles|"
                            + "can|cans|piece|pieces|pcs|pair|pairs)\\b\\s*"
                ),
                ""
            )
            .replace(
                Regex("[^a-z0-9\\s]"),
                " "
            )
            .replace(
                Regex("\\s+"),
                " "
            )
            .trim()
    }
}