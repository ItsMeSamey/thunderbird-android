package app.k9mail.feature.account.oauth.domain.usecase

import android.content.Intent
import app.k9mail.feature.account.oauth.domain.FakeAuthorizationRepository
import app.k9mail.feature.account.oauth.domain.entity.AuthorizationIntentResult
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fsck.k9.mail.store.imap.ClientIdPresets
import kotlinx.coroutines.test.runTest
import net.thunderbird.core.common.oauth.OAuthConfiguration
import net.thunderbird.core.preference.clientid.ClientIdPreference
import net.thunderbird.core.preference.clientid.ClientIdPreferenceManager
import org.junit.Test

class GetOAuthRequestIntentTest {

    @Test
    fun `should return NotSupported when hostname has no oauth configuration`() = runTest {
        val testSubject = GetOAuthRequestIntent(
            repository = FakeAuthorizationRepository(),
            configurationProvider = { null },
            clientIdPreferenceManager = FakeClientIdPreferenceManager(),
        )
        val hostname = "hostname"
        val emailAddress = "emailAddress"

        val result = testSubject.execute(hostname, emailAddress)

        assertThat(result).isEqualTo(AuthorizationIntentResult.NotSupported)
    }

    @Test
    fun `should return Success when repository has intent`() = runTest {
        val intent = Intent()
        val repository = FakeAuthorizationRepository(
            answerGetAuthorizationRequestIntent = AuthorizationIntentResult.Success(intent),
        )
        val testSubject = GetOAuthRequestIntent(
            repository = repository,
            configurationProvider = { oAuthConfiguration },
            clientIdPreferenceManager = FakeClientIdPreferenceManager(),
        )
        val hostname = "hostname"
        val emailAddress = "emailAddress"

        val result = testSubject.execute(hostname, emailAddress)

        assertThat(result).isEqualTo(
            AuthorizationIntentResult.Success(
                intent = intent,
            ),
        )
        assertThat(repository.recordedGetAuthorizationRequestIntentConfiguration).isEqualTo(oAuthConfiguration)
        assertThat(repository.recordedGetAuthorizationRequestIntentEmailAddress).isEqualTo(emailAddress)
    }

    @Test
    fun `should keep OAuth configuration unchanged when default mobile preset is configured`() = runTest {
        val intent = Intent()
        val repository = FakeAuthorizationRepository(
            answerGetAuthorizationRequestIntent = AuthorizationIntentResult.Success(intent),
        )
        val testSubject = GetOAuthRequestIntent(
            repository = repository,
            configurationProvider = { googleOAuthConfiguration },
            clientIdPreferenceManager = FakeClientIdPreferenceManager(
                ClientIdPreference(presetKey = ClientIdPresets.THUNDERBIRD_MOBILE.key),
            ),
        )

        testSubject.execute(hostname = "hostname", emailAddress = "emailAddress")

        assertThat(repository.recordedGetAuthorizationRequestIntentConfiguration).isEqualTo(googleOAuthConfiguration)
    }

    @Test
    fun `should override OAuth client identity when desktop preset has issuer override`() = runTest {
        val intent = Intent()
        val repository = FakeAuthorizationRepository(
            answerGetAuthorizationRequestIntent = AuthorizationIntentResult.Success(intent),
        )
        val testSubject = GetOAuthRequestIntent(
            repository = repository,
            configurationProvider = { googleOAuthConfiguration },
            clientIdPreferenceManager = FakeClientIdPreferenceManager(
                ClientIdPreference(presetKey = ClientIdPresets.THUNDERBIRD_DESKTOP.key),
            ),
        )

        testSubject.execute(hostname = "hostname", emailAddress = "emailAddress")

        assertThat(repository.recordedGetAuthorizationRequestIntentConfiguration).isEqualTo(
            googleOAuthConfiguration.copy(
                clientId = "406964657835-aq8lmia8j95dhl1a2bvharmfk3t1hgqj.apps.googleusercontent.com",
                redirectUri = "http://localhost",
            ),
        )
    }

    @Test
    fun `should keep OAuth configuration unchanged when desktop preset has no issuer override`() = runTest {
        val intent = Intent()
        val repository = FakeAuthorizationRepository(
            answerGetAuthorizationRequestIntent = AuthorizationIntentResult.Success(intent),
        )
        val testSubject = GetOAuthRequestIntent(
            repository = repository,
            configurationProvider = { oAuthConfiguration },
            clientIdPreferenceManager = FakeClientIdPreferenceManager(
                ClientIdPreference(presetKey = ClientIdPresets.THUNDERBIRD_DESKTOP.key),
            ),
        )

        testSubject.execute(hostname = "hostname", emailAddress = "emailAddress")

        assertThat(repository.recordedGetAuthorizationRequestIntentConfiguration).isEqualTo(oAuthConfiguration)
    }

    private class FakeClientIdPreferenceManager(
        private var config: ClientIdPreference = ClientIdPreference(),
    ) : ClientIdPreferenceManager {
        override fun save(config: ClientIdPreference) {
            this.config = config
        }

        override fun getConfig(): ClientIdPreference = config

        override fun getConfigFlow() = kotlinx.coroutines.flow.flowOf(config)
    }

    private companion object {
        val oAuthConfiguration = OAuthConfiguration(
            issuer = "test.issuer",
            clientId = "clientId",
            scopes = listOf("scope", "scope2"),
            authorizationEndpoint = "auth.example.com",
            tokenEndpoint = "token.example.com",
            redirectUri = "redirect.example.com",
        )

        val googleOAuthConfiguration = oAuthConfiguration.copy(
            issuer = "accounts.google.com",
        )
    }
}
