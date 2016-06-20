package com.example.multispinner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String DEFAULT_TEXT = "Select - Value";
    private MultiSelectionSpinner mySpinner;
    private GetDummyValues getDummyValues;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mySpinner = (MultiSelectionSpinner) findViewById(R.id.viewMyView);
        button = (Button) findViewById(R.id.buttonPress);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) findViewById(R.id.textViewIds)).setText(mySpinner.getSelectedItemIds());
                ((TextView) findViewById(R.id.textViewNames)).setText(mySpinner.getSelectedItemsAsString());
            }
        });


        getDummyValues = new GetDummyValues();
        mySpinner.setItems(getDummyValues.getValues(), DEFAULT_TEXT, getDummyValues.getIds());
    }
}
