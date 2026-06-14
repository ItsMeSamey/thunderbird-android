package com.fsck.k9.mail.store.imap

import androidx.annotation.VisibleForTesting
import com.fsck.k9.mail.ServerSettings

/**
 * Extract IMAP-specific server settings from [ServerSettings]
 */
object ImapStoreSettings {
    @VisibleForTesting
    const val AUTODETECT_NAMESPACE_KEY = "autoDetectNamespace"

    @VisibleForTesting
    const val PATH_PREFIX_KEY = "pathPrefix"

    @VisibleForTesting
    const val SEND_CLIENT_INFO = "sendClientInfo"

    @VisibleForTesting
    const val CLIENT_ID_PRESET_KEY = "clientIdPresetKey"

    @VisibleForTesting
    const val CLIENT_ID_CUSTOM_NAME = "clientIdCustomName"

    @VisibleForTesting
    const val CLIENT_ID_CUSTOM_VERSION = "clientIdCustomVersion"

    @VisibleForTesting
    const val CLIENT_ID_OAUTH_CLIENT_ID = "clientIdOauthClientId"

    @VisibleForTesting
    const val CLIENT_ID_OAUTH_REDIRECT_URI = "clientIdOauthRedirectUri"

    @VisibleForTesting
    const val USE_COMPRESSION = "useCompression"

    @JvmStatic
    val ServerSettings.autoDetectNamespace: Boolean
        get() = extra[AUTODETECT_NAMESPACE_KEY]?.toBoolean() ?: true

    @JvmStatic
    val ServerSettings.pathPrefix: String?
        get() = extra[PATH_PREFIX_KEY]

    @JvmStatic
    val ServerSettings.isUseCompression: Boolean
        get() = extra[USE_COMPRESSION]?.toBoolean() ?: true

    @JvmStatic
    val ServerSettings.isSendClientInfo: Boolean
        get() = extra[SEND_CLIENT_INFO]?.toBoolean() ?: true

    @JvmStatic
    val ServerSettings.clientIdPresetKey: String?
        get() = extra[CLIENT_ID_PRESET_KEY]?.takeIf { it.isNotEmpty() }

    @JvmStatic
    val ServerSettings.clientIdCustomName: String?
        get() = extra[CLIENT_ID_CUSTOM_NAME]?.takeIf { it.isNotEmpty() }

    @JvmStatic
    val ServerSettings.clientIdCustomVersion: String?
        get() = extra[CLIENT_ID_CUSTOM_VERSION]?.takeIf { it.isNotEmpty() }

    @JvmStatic
    val ServerSettings.clientIdOauthClientId: String?
        get() = extra[CLIENT_ID_OAUTH_CLIENT_ID]?.takeIf { it.isNotEmpty() }

    @JvmStatic
    val ServerSettings.clientIdOauthRedirectUri: String?
        get() = extra[CLIENT_ID_OAUTH_REDIRECT_URI]?.takeIf { it.isNotEmpty() }

    // Note: These extras are currently held in the instance referenced by Account.incomingServerSettings
    @JvmStatic
    fun createExtra(autoDetectNamespace: Boolean, pathPrefix: String?): Map<String, String?> {
        return mapOf(
            AUTODETECT_NAMESPACE_KEY to autoDetectNamespace.toString(),
            PATH_PREFIX_KEY to pathPrefix,
        )
    }

    // Note: These extras are required when creating an ImapStore instance.
    fun createExtra(
        autoDetectNamespace: Boolean,
        pathPrefix: String?,
        useCompression: Boolean,
        sendClientInfo: Boolean,
        clientIdPresetKey: String? = null,
        clientIdCustomName: String? = null,
        clientIdCustomVersion: String? = null,
        clientIdOauthClientId: String? = null,
        clientIdOauthRedirectUri: String? = null,
    ): Map<String, String?> {
        val extras = mutableMapOf(
            AUTODETECT_NAMESPACE_KEY to autoDetectNamespace.toString(),
            PATH_PREFIX_KEY to pathPrefix,
            USE_COMPRESSION to useCompression.toString(),
            SEND_CLIENT_INFO to sendClientInfo.toString(),
        )
        if (clientIdPresetKey != null) {
            extras[CLIENT_ID_PRESET_KEY] = clientIdPresetKey
        }
        if (clientIdCustomName != null) {
            extras[CLIENT_ID_CUSTOM_NAME] = clientIdCustomName
        }
        if (clientIdCustomVersion != null) {
            extras[CLIENT_ID_CUSTOM_VERSION] = clientIdCustomVersion
        }
        if (clientIdOauthClientId != null) {
            extras[CLIENT_ID_OAUTH_CLIENT_ID] = clientIdOauthClientId
        }
        if (clientIdOauthRedirectUri != null) {
            extras[CLIENT_ID_OAUTH_REDIRECT_URI] = clientIdOauthRedirectUri
        }
        return extras
    }
}
