package com.tushar.multiselectspinner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.Arrays;
import java.util.List;

public class MultiSelectSpinner extends Spinner implements DialogInterface.OnMultiChoiceClickListener {
    private final ArrayAdapter<Object> simpleAdapter;
    private List<String> items; // Stores original values
    private String itemsAtStart; // Stores original values as string
    private List<String> ids; // Stores original value ids
    private String defaultText; // Default text like "Select - One"
    private String titleText; // Dialogbox title text
    private boolean[] selectedValues; // Stores selected values
    private boolean[] selectionAtStart; // Original selected values
    private Context context;

    public MultiSelectSpinner(Context context) {
        super(context);
        this.context = context;
        simpleAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simpleAdapter);
    }

    public MultiSelectSpinner(Context arg0, AttributeSet arg1) { // System required constructor
        super(arg0, arg1);
        this.context = arg0;
        simpleAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simpleAdapter);
    }

    public MultiSelectSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        this.context = arg0;
        simpleAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simpleAdapter);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which, boolean isSelected) {
        if (selectedValues != null && selectedValues.length > 0) {
            selectedValues[which] = isSelected;
            simpleAdapter.clear();
            simpleAdapter.add(getSelectedItemsAsString());
        } else {
            throw new IllegalStateException("Clicked on null object.");
        }
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] ItemsAsArray = new String[items.size()];
        builder.setMultiChoiceItems(items.toArray(ItemsAsArray), selectedValues, this);
        itemsAtStart = getSelectedItemsAsString();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog, null);
        builder.setTitle(titleText);
        builder.setView(view);
        AlertDialog alertDialog = builder.show();
        init(view, alertDialog);
        return true;
    }

    private void init(View view, final AlertDialog alertDialog) {
        Button ok = (Button) view.findViewById(R.id.buttonOk);
        Button cancel = (Button) view.findViewById(R.id.buttonCancel);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < items.size(); i++) {
                        alertDialog.getListView().setItemChecked(i, true);
                        selectedValues[i] = true;
                        simpleAdapter.clear();
                        simpleAdapter.add(getSelectedItemsAsString());
                    }
                    if (selectedValues != null)
                        System.arraycopy(selectedValues, 0, selectionAtStart, 0, selectedValues.length);
                } else {
                    for (int i = 0; i < items.size(); i++) {
                        alertDialog.getListView().setItemChecked(i, false);
                        selectedValues[i] = false;
                    }
                    simpleAdapter.clear();
                    simpleAdapter.add(getSelectedItemsAsString());
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedValues != null)
                    System.arraycopy(selectedValues, 0, selectionAtStart, 0, selectedValues.length);
                alertDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleAdapter.clear();
                if (items.size() <= 1) {
                    simpleAdapter.add(defaultText);
                }
                simpleAdapter.add(itemsAtStart);
                if (selectedValues != null)
                    System.arraycopy(selectionAtStart, 0, selectedValues, 0, selectionAtStart.length);
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        super.setAdapter(adapter);
        throw new RuntimeException("setAdapter is not allowed." +
                " Instead call 'setItems()'.");
    }

    public void setItems(List<String> items, List<String> ids, String defaultText, String titleText) {
        this.items = items;
        this.ids = ids;
        this.defaultText = defaultText;
        this.titleText = titleText;
        selectedValues = new boolean[items.size()];
        selectionAtStart = new boolean[items.size()];
        simpleAdapter.clear();
        simpleAdapter.add(defaultText);
        Arrays.fill(selectedValues, false);
    }

    public String getSelectedItemsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = false;
        for (int i = 0; i < items.size(); i++) {
            if (selectedValues[i]) {
                if (isFirst) { // stops appending "," before string.
                    sb.append(", ");
                }
                isFirst = true;
                sb.append(items.get(i));
            }
        }
        if (sb.length() == 0) {
            sb.append(defaultText);
        }
        return sb.toString();
    }

    public String getSelectedIdsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = false;
        for (int i = 0; i < ids.size(); i++) {
            if (selectedValues[i]) {
                if (isFirst) { // stops appending "," before string.
                    sb.append(",");
                }
                isFirst = true;
                sb.append(ids.get(i));
            }
        }
        if (sb.length() == 0) {
            sb.append(defaultText);
        }
        return sb.toString();
    }
}