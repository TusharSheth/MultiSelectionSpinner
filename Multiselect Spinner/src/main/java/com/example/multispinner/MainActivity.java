package com.example.multispinner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tushar.multiselectspinner.MultiSelectSpinner;

public class MainActivity extends AppCompatActivity {
    private static final String DEFAULT_TEXT = "Select - Value";
    private GetDummyValues getDummyValues;
    private Button button;
    private MultiSelectSpinner myMultispinner;
    private String titleText = "Your Title Here";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        myMultispinner = (MultiSelectSpinner) findViewById(R.id.view);
        button = (Button) findViewById(R.id.buttonPress);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) findViewById(R.id.textViewIds)).setText(myMultispinner.getSelectedIdsAsString());
                ((TextView) findViewById(R.id.textViewNames)).setText(myMultispinner.getSelectedItemsAsString());
            }
        });

        getDummyValues = new GetDummyValues();
        myMultispinner.setItems(getDummyValues.getValues(), getDummyValues.getIds(), DEFAULT_TEXT,titleText);
    }
}
