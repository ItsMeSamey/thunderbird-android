package net.thunderbird.core.preference.clientid

data class ClientIdPreference(
    val presetKey: String = THUNDERBIRD_MOBILE_KEY,
) {
    companion object {
        const val THUNDERBIRD_MOBILE_KEY = "thunderbird_mobile"
        const val THUNDERBIRD_DESKTOP_KEY = "thunderbird_desktop"
    }
}
