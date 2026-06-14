package net.thunderbird.core.preference.clientid

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.thunderbird.core.preference.clientid.ClientIdPreference.Companion.THUNDERBIRD_MOBILE_KEY
import net.thunderbird.core.preference.storage.StorageEditor
import net.thunderbird.core.preference.storage.StoragePersister

class DefaultClientIdPreferenceManager(
    private val storagePersister: StoragePersister,
    private val storageEditor: StorageEditor,
) : ClientIdPreferenceManager {

    private val configState = MutableStateFlow(readConfig())

    override fun getConfig(): ClientIdPreference = configState.value

    override fun getConfigFlow(): StateFlow<ClientIdPreference> = configState.asStateFlow()

    override fun save(config: ClientIdPreference) {
        writeConfig(config)
        configState.value = config
    }

    private fun readConfig(): ClientIdPreference {
        val storage = storagePersister.loadValues()
        return ClientIdPreference(
            presetKey = storage.getStringOrDefault(PRESET_KEY, THUNDERBIRD_MOBILE_KEY),
        )
    }

    private fun writeConfig(config: ClientIdPreference) {
        storageEditor.putString(PRESET_KEY, config.presetKey)
        storageEditor.commit()
    }

    private companion object {
        const val PRESET_KEY = "client_id_preset_key"
    }
}
