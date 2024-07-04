package com.application.tourlica;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.application.tourlica.ui.map_view.MapFragment;

public class MapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        Bundle bundle = new Bundle();
        bundle.putString("TOURDATA", getIntent().getStringExtra("TOURDATA"));
        bundle.putString("LOCATION", getIntent().getStringExtra("LOCATION"));
        if (savedInstanceState == null) {
            MapFragment fragment = MapFragment.newInstance();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.map_activity, fragment)
                    .commitNow();
        }
    }
}
