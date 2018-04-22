package com.amr.atlas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

public class MainActivity extends AppCompatActivity {

    public static int MapType = GoogleMap.MAP_TYPE_NORMAL;
    public EditText Search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        Search = findViewById(R.id.search);
        setSupportActionBar(toolbar);

        Search.setOnEditorActionListener(new TypingFinished(Search));
        Toast.makeText(this, "this app was developed by amr", Toast.LENGTH_LONG).show();

    }

    public void openMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("place", (String) null);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.normal:
                MapType = GoogleMap.MAP_TYPE_NORMAL;
                item.setChecked(true);
                break;

            case R.id.hybrid:
                MapType = GoogleMap.MAP_TYPE_HYBRID;
                item.setChecked(true);
                break;

            case R.id.terrain:
                MapType = GoogleMap.MAP_TYPE_TERRAIN;
                item.setChecked(true);
                break;

            case R.id.satellite:
                MapType = GoogleMap.MAP_TYPE_SATELLITE;
                item.setChecked(true);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    class TypingFinished implements EditText.OnEditorActionListener {

        public EditText Search;

        public TypingFinished(EditText Search) {
            this.Search = Search;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null &&
                            event.getAction() == KeyEvent.ACTION_DOWN &&
                            event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                if (event == null || !event.isShiftPressed()) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("place", Search.getText().toString());
                    startActivity(intent);

                    return true;
                }
            }
            return false;
        }
    }
}

