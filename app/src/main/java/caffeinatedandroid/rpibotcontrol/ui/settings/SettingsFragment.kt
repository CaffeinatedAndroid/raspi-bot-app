package caffeinatedandroid.rpibotcontrol.ui.settings

import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import caffeinatedandroid.rpibotcontrol.R

class SettingsFragment : PreferenceFragmentCompat() {

    /**
     * Called when preparing the `Preferences` to be displayed.
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // Work-around: setting EditTextPreference with "android:inputType='numberDecimal'"
        // does not stop letters being entered, but intervening here does. See:
        // https://stackoverflow.com/a/56755861/508098
        val editTextPreference =
            preferenceManager.findPreference<EditTextPreference>(getString(R.string.pref_key_connection_udp_port))
        editTextPreference?.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_NUMBER
        }

        preferenceManager.findPreference<EditTextPreference>(getString(R.string.pref_key_connection_tcp_port))
            ?.setOnBindEditTextListener {
                it.inputType = InputType.TYPE_CLASS_NUMBER
            }
    }
}