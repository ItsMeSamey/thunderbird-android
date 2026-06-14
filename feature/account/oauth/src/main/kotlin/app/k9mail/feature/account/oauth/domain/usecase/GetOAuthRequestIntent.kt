package app.k9mail.feature.account.oauth.domain.usecase

import app.k9mail.feature.account.oauth.domain.AccountOAuthDomainContract
import app.k9mail.feature.account.oauth.domain.AccountOAuthDomainContract.UseCase.GetOAuthRequestIntent
import app.k9mail.feature.account.oauth.domain.entity.AuthorizationIntentResult
import com.fsck.k9.mail.store.imap.ClientIdPresets
import net.thunderbird.core.common.oauth.OAuthConfiguration
import net.thunderbird.core.common.oauth.OAuthConfigurationProvider
import net.thunderbird.core.preference.clientid.ClientIdPreferenceManager

internal class GetOAuthRequestIntent(
    private val repository: AccountOAuthDomainContract.AuthorizationRepository,
    private val configurationProvider: OAuthConfigurationProvider,
    private val clientIdPreferenceManager: ClientIdPreferenceManager,
) : GetOAuthRequestIntent {
    override fun execute(
        hostname: String,
        emailAddress: String,
        perAccountPresetKey: String?,
        customOauthClientId: String?,
        customOauthRedirectUri: String?,
    ): AuthorizationIntentResult {
        val configuration = configurationProvider.getConfiguration(hostname)
            ?: return AuthorizationIntentResult.NotSupported

        return repository.getAuthorizationRequestIntent(
            configuration.withOAuthClientOverride(perAccountPresetKey, customOauthClientId, customOauthRedirectUri),
            emailAddress,
        )
    }

    private fun OAuthConfiguration.withOAuthClientOverride(
        perAccountPresetKey: String?,
        customOauthClientId: String?,
        customOauthRedirectUri: String?,
    ): OAuthConfiguration {
        val globalPresetKey = clientIdPreferenceManager.getConfig().presetKey
        val effectivePresetKey = perAccountPresetKey?.takeIf {
            it.isNotEmpty() && it != ClientIdPresets.DEFAULT_KEY
        } ?: globalPresetKey
        val preset = ClientIdPresets.fromKey(effectivePresetKey)

        val issuerOverride = preset?.oauthIssuerOverrides?.get(issuer)
        if (issuerOverride != null) {
            val oauthClientId = issuerOverride.clientId.takeIf { it.isNotBlank() } ?: clientId
            val oauthRedirectUri = issuerOverride.redirectUri.takeIf { it.isNotBlank() } ?: redirectUri
            return copy(clientId = oauthClientId, redirectUri = oauthRedirectUri)
        }

        if (preset?.key == ClientIdPresets.CUSTOM_KEY) {
            val oauthClientId = customOauthClientId?.takeIf { it.isNotBlank() } ?: clientId
            val oauthRedirectUri = customOauthRedirectUri?.takeIf { it.isNotBlank() } ?: redirectUri
            if (oauthClientId != clientId || oauthRedirectUri != redirectUri) {
                return copy(clientId = oauthClientId, redirectUri = oauthRedirectUri)
            }
        }

        return this
    }
}
