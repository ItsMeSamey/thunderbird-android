package app.k9mail.feature.account.server.settings.ui.incoming.content

import android.content.res.Resources
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.res.stringResource
import app.k9mail.core.ui.compose.designsystem.molecule.input.CheckboxInput
import app.k9mail.core.ui.compose.designsystem.molecule.input.SelectInput
import app.k9mail.core.ui.compose.designsystem.molecule.input.TextInput
import app.k9mail.feature.account.common.domain.entity.AuthenticationType
import app.k9mail.feature.account.common.ui.item.defaultItemPadding
import app.k9mail.feature.account.server.settings.R
import app.k9mail.feature.account.server.settings.ui.common.mapper.toResourceString
import app.k9mail.feature.account.server.settings.ui.incoming.IncomingServerSettingsContract.Event
import app.k9mail.feature.account.server.settings.ui.incoming.IncomingServerSettingsContract.State
import com.fsck.k9.mail.store.imap.ClientIdPresets
import kotlinx.collections.immutable.toImmutableList

internal fun LazyListScope.imapFormItems(
    state: State,
    onEvent: (Event) -> Unit,
    resources: Resources,
) {
    item {
        CheckboxInput(
            text = stringResource(id = R.string.account_server_settings_incoming_imap_namespace_label),
            checked = state.imapAutodetectNamespaceEnabled,
            onCheckedChange = { onEvent(Event.ImapAutoDetectNamespaceChanged(it)) },
            contentPadding = defaultItemPadding(),
        )
    }

    item {
        if (state.imapAutodetectNamespaceEnabled) {
            TextInput(
                onTextChange = {},
                label = stringResource(id = R.string.account_server_settings_incoming_imap_prefix_label),
                contentPadding = defaultItemPadding(),
                isEnabled = false,
            )
        } else {
            TextInput(
                text = state.imapPrefix.value,
                errorMessage = state.imapPrefix.error?.toResourceString(resources),
                onTextChange = { onEvent(Event.ImapPrefixChanged(it)) },
                label = stringResource(id = R.string.account_server_settings_incoming_imap_prefix_label),
                contentPadding = defaultItemPadding(),
            )
        }
    }

    item {
        CheckboxInput(
            text = stringResource(id = R.string.account_server_settings_incoming_imap_compression_label),
            checked = state.imapUseCompression,
            onCheckedChange = { onEvent(Event.ImapUseCompressionChanged(it)) },
            contentPadding = defaultItemPadding(),
        )
    }

    item {
        CheckboxInput(
            text = stringResource(R.string.account_server_settings_incoming_imap_send_client_info_label),
            checked = state.imapSendClientInfo,
            onCheckedChange = { onEvent(Event.ImapSendClientInfoChanged(it)) },
            contentPadding = defaultItemPadding(),
        )
    }

    if (state.imapSendClientInfo) {
        val clientIdOptions = (listOf(ClientIdPresets.DEFAULT_KEY) +
            ClientIdPresets.entries.map { it.key } + ClientIdPresets.CUSTOM_KEY).toImmutableList()
        val clientIdOptionLabels = mapOf(
            ClientIdPresets.DEFAULT_KEY to resources.getString(
                R.string.account_server_settings_incoming_imap_client_id_default,
            ),
        ) + ClientIdPresets.entries.associate { it.key to it.displayName } +
            (
                ClientIdPresets.CUSTOM_KEY to resources.getString(
                    R.string.account_server_settings_incoming_imap_client_id_custom,
                )
                )

        item {
            SelectInput(
                options = clientIdOptions,
                optionToStringTransformation = { clientIdOptionLabels[it] ?: it },
                selectedOption = state.imapClientIdPresetKey,
                onOptionChange = { onEvent(Event.ImapClientIdPresetChanged(it)) },
                label = stringResource(id = R.string.account_server_settings_incoming_imap_client_id_label),
                contentPadding = defaultItemPadding(),
            )
        }

        if (state.imapClientIdPresetKey == ClientIdPresets.CUSTOM_KEY) {
            item {
                TextInput(
                    text = state.imapClientIdCustomName.value,
                    onTextChange = { onEvent(Event.ImapClientIdCustomNameChanged(it)) },
                    label = stringResource(id = R.string.account_server_settings_incoming_imap_client_id_custom_name),
                    contentPadding = defaultItemPadding(),
                )
            }

            item {
                TextInput(
                    text = state.imapClientIdCustomVersion.value,
                    onTextChange = { onEvent(Event.ImapClientIdCustomVersionChanged(it)) },
                    label = stringResource(
                        id = R.string.account_server_settings_incoming_imap_client_id_custom_version,
                    ),
                    contentPadding = defaultItemPadding(),
                )
            }

            if (state.authenticationType == AuthenticationType.OAuth2) {
                item {
                    TextInput(
                        text = state.imapClientIdOauthClientId.value,
                        onTextChange = { onEvent(Event.ImapClientIdOauthClientIdChanged(it)) },
                        label = stringResource(id = R.string.account_server_settings_incoming_imap_client_id_oauth_client_id),
                        contentPadding = defaultItemPadding(),
                    )
                }

                item {
                    TextInput(
                        text = state.imapClientIdOauthRedirectUri.value,
                        onTextChange = { onEvent(Event.ImapClientIdOauthRedirectUriChanged(it)) },
                        label = stringResource(id = R.string.account_server_settings_incoming_imap_client_id_oauth_redirect_uri),
                        contentPadding = defaultItemPadding(),
                    )
                }
            }
        }
    }
}
