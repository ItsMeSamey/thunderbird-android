package com.fsck.k9.mail.store.imap

data class ClientIdPreset(
    val key: String,
    val displayName: String,
    val appName: String,
    val appVersion: String,
    val oauthIssuerOverrides: Map<String, OAuthIssuerOverride> = emptyMap(),
)

data class OAuthIssuerOverride(
    val clientId: String,
    val redirectUri: String = "",
)

object ClientIdPresets {
    private val DESKTOP_OAUTH_ISSUER_OVERRIDES = mapOf(
        "accounts.google.com" to OAuthIssuerOverride(
            clientId = "406964657835-aq8lmia8j95dhl1a2bvharmfk3t1hgqj.apps.googleusercontent.com",
            redirectUri = "http://localhost",
        ),
        "login.microsoftonline.com" to OAuthIssuerOverride(
            clientId = "9e5f94bc-e8a4-4e73-b8be-63364c29d753",
            redirectUri = "http://localhost",
        ),
        "login.yahoo.com" to OAuthIssuerOverride(
            clientId = "dj0yJmk9WVZUaWRNUUZSQTBNJmQ9WVdrOVNqbHJUMGhtTkU4bWNHbzlNQT09JnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PTgz",
            redirectUri = "http://localhost",
        ),
        "login.aol.com" to OAuthIssuerOverride(
            clientId = "dj0yJmk9MGNoTTQ0SjhIN1dSJmQ9WVdrOVdIVnFkVVp2UkZNbWNHbzlNQT09JnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PTNm",
            redirectUri = "http://localhost",
        ),
        "www.fastmail.com" to OAuthIssuerOverride(
            clientId = "35f141ae",
            redirectUri = "http://localhost",
        ),
        "auth.tb.pro" to OAuthIssuerOverride(
            clientId = "desktop",
            redirectUri = "http://localhost",
        ),
        "auth-stage.tb.pro" to OAuthIssuerOverride(
            clientId = "desktop",
            redirectUri = "http://localhost",
        ),
    )

    val THUNDERBIRD_MOBILE = ClientIdPreset(
        key = "thunderbird_mobile",
        displayName = "Mobile",
        appName = "Thunderbird for Android",
        appVersion = "133.0",
    )

    val THUNDERBIRD_DESKTOP = ClientIdPreset(
        key = "thunderbird_desktop",
        displayName = "Desktop",
        appName = "Thunderbird",
        appVersion = "128.0",
        oauthIssuerOverrides = DESKTOP_OAUTH_ISSUER_OVERRIDES,
    )

    val entries: List<ClientIdPreset> = listOf(THUNDERBIRD_MOBILE, THUNDERBIRD_DESKTOP)
    private val entriesByKey: Map<String, ClientIdPreset> = entries.associateBy { it.key }

    fun fromKey(key: String): ClientIdPreset? = entriesByKey[key]

    const val DEFAULT_KEY = "default"
    const val CUSTOM_KEY = "custom"
}
