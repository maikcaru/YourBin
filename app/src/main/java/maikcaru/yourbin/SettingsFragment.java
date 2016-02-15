package maikcaru.yourbin;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by michael.carr on 11/02/16.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
