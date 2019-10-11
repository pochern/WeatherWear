package add.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View.OnKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class Activity2 extends AppCompatActivity implements OnKeyListener {

    private EditText searchBarC;
    public final static String MESSAGE_KEY =  "STC";

    ImageButton closeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        searchBarC = findViewById(R.id.searchCity);
        searchBarC.setOnKeyListener(this);

        closeButton = findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity1(v);
            }
        });

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        String site = null;
        if (v == searchBarC) {
            //If the keyevent is a key-down event on the "enter" button
            if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (keyCode == android.view.KeyEvent.KEYCODE_ENTER)) {
                String searchString = searchBarC.getText().toString();
                openActivity1Data(v, searchString);
                return true;
            }

        }
        return false;
    }

    public void openActivity1(View view){
        closeButton.setColorFilter(Color.argb(255, 0, 0, 0)); // Black Tint
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        Intent openMainActivity= new Intent(this, MainActivity.class);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openMainActivity, 0);

    }

    public void openActivity1Data(View view, String searchString){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MESSAGE_KEY, searchString);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
