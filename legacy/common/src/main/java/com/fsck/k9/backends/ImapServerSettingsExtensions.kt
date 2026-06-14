package com.fsck.k9.backends

import com.fsck.k9.mail.ServerSettings
import com.fsck.k9.mail.store.imap.ImapStoreSettings
import com.fsck.k9.mail.store.imap.ImapStoreSettings.autoDetectNamespace
import com.fsck.k9.mail.store.imap.ImapStoreSettings.clientIdCustomName
import com.fsck.k9.mail.store.imap.ImapStoreSettings.clientIdCustomVersion
import com.fsck.k9.mail.store.imap.ImapStoreSettings.clientIdOauthClientId
import com.fsck.k9.mail.store.imap.ImapStoreSettings.clientIdOauthRedirectUri
import com.fsck.k9.mail.store.imap.ImapStoreSettings.clientIdPresetKey
import com.fsck.k9.mail.store.imap.ImapStoreSettings.pathPrefix
import net.thunderbird.core.android.account.LegacyAccount
import net.thunderbird.core.android.account.LegacyAccountDto

fun LegacyAccountDto.toImapServerSettings(): ServerSettings {
    val serverSettings = incomingServerSettings
    return serverSettings.copy(
        extra = ImapStoreSettings.createExtra(
            autoDetectNamespace = serverSettings.autoDetectNamespace,
            pathPrefix = serverSettings.pathPrefix,
            useCompression = useCompression,
            sendClientInfo = isSendClientInfoEnabled,
            clientIdPresetKey = serverSettings.clientIdPresetKey,
            clientIdCustomName = serverSettings.clientIdCustomName,
            clientIdCustomVersion = serverSettings.clientIdCustomVersion,
            clientIdOauthClientId = serverSettings.clientIdOauthClientId,
            clientIdOauthRedirectUri = serverSettings.clientIdOauthRedirectUri,
        ),
    )
}

fun LegacyAccount.toImapServerSettings(): ServerSettings {
    val serverSettings = incomingServerSettings
    return serverSettings.copy(
        extra = ImapStoreSettings.createExtra(
            autoDetectNamespace = serverSettings.autoDetectNamespace,
            pathPrefix = serverSettings.pathPrefix,
            useCompression = useCompression,
            sendClientInfo = isSendClientInfoEnabled,
            clientIdPresetKey = serverSettings.clientIdPresetKey,
            clientIdCustomName = serverSettings.clientIdCustomName,
            clientIdCustomVersion = serverSettings.clientIdCustomVersion,
            clientIdOauthClientId = serverSettings.clientIdOauthClientId,
            clientIdOauthRedirectUri = serverSettings.clientIdOauthRedirectUri,
        ),
    )
}
