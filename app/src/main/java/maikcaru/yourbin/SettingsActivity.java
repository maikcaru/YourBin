package maikcaru.yourbin;

import android.os.Bundle;

public class SettingsActivity extends NavigationDrawerParent {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        createToolbar();

    }

}