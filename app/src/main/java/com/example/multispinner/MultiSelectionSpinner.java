package com.example.multispinner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MultiSelectionSpinner extends Spinner implements
                                                   DialogInterface.OnMultiChoiceClickListener {
    String[] _items = null;
    String[] _ids = null;
    boolean[] mSelection = null;
    boolean[] mSelectionAtStart = null;
    String _itemsAtStart = null;
    ArrayAdapter<String> simple_adapter;
    private String defaultText = "";
    private static final String TAG = MultiSelectionSpinner.class.getSimpleName();

    public MultiSelectionSpinner(Context context) {
        super(context);
        simple_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        simple_adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (mSelection != null && which < mSelection.length) {
            mSelection[which] = isChecked;
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        } else {
            throw new IllegalArgumentException(
                    "Argument 'which' is out of bounds.");
        }
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(defaultText);
        builder.setMultiChoiceItems(_items, mSelection, this);
        _itemsAtStart = getSelectedItemsAsString();
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mSelection != null)
                    System.arraycopy(mSelection, 0, mSelectionAtStart, 0, mSelection.length);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                simple_adapter.clear();
                if (_itemsAtStart.length() <= 1) {
                    simple_adapter.add(defaultText);
                }
                simple_adapter.add(_itemsAtStart);
                if (mSelection != null)
                    System.arraycopy(mSelectionAtStart, 0, mSelection, 0, mSelectionAtStart.length);
            }
        });
        builder.show();
        return true;
    }

    @Override
    public void setAdapter(android.widget.SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    public void setItems(List<String> items) {
        _items = items.toArray(new String[items.size()]);
        mSelection = new boolean[_items.length];
        mSelectionAtStart = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
    }

    public void setItems(List<String> items, String defaultText) {
        this.defaultText = defaultText;
        _items = items.toArray(new String[items.size()]);
        mSelection = new boolean[_items.length];
        mSelectionAtStart = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(defaultText);
        Arrays.fill(mSelection, false);
    }

    public void setSelection(List<String> selection) {
        try {
            for (int i = 0; i < mSelection.length; i++) {
                mSelection[i] = false;
                mSelectionAtStart[i] = false;
            }
            for (String sel : selection) {
                for (int j = 0; j < _items.length; ++j) {
                    if (_items[j].equals(sel)) {
                        mSelection[j] = true;
                        mSelectionAtStart[j] = true;
                    }
                }
            }
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSpinnerItems(String commaSepratedIds, ArrayList<String> textsOfSpinner, ArrayList<String> idValues) {
        String[] p = commaSepratedIds.split(",");
        List<String> selection = new ArrayList<>();
        for (String id : p) {
            for (String currentIdValue : idValues) {
                if (id.equals(currentIdValue)) {
                    selection.add(textsOfSpinner.get(idValues.indexOf(currentIdValue)));
                }
            }
        }
        if (selection.size() > 0) {
            try {
                for (int i = 0; i < mSelection.length; i++) {
                    mSelection[i] = false;
                    mSelectionAtStart[i] = false;
                }
                for (String sel : selection) {
                    for (int j = 0; j < _items.length; ++j) {
                        if (_items[j].equals(sel)) {
                            mSelection[j] = true;
                            mSelectionAtStart[j] = true;
                        }
                    }
                }
                simple_adapter.clear();
                simple_adapter.add(buildSelectedItemString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setSelection(int index) {
        if (mSelection != null) {
            for (int i = 0; i < mSelection.length; i++) {
                mSelection[i] = false;
                mSelectionAtStart[i] = false;
            }
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
                mSelectionAtStart[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index
                        + " is out of bounds.");
            }
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        }
    }

    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<>();
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selection.add(_items[i]);
            }
        }
        return selection;
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;
        if (mSelection.length > 0) {
            for (int i = 0; i < _items.length; ++i) {
                if (mSelection[i]) {
                    if (foundOne) {
                        sb.append(",");
                    }
                    foundOne = true;

                    sb.append(_items[i]);
                }
            }
        }
        if (sb.length() == 0) {
            sb.append(defaultText);
        }
        return sb.toString();
    }

    public String getSelectedItemsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;
        if (_items != null) {
            for (int i = 0; i < _items.length; ++i) {
                if (mSelection[i]) {
                    if (foundOne) {
                        sb.append(",");
                    }
                    foundOne = true;
                    sb.append(_items[i]);
                }
            }
        }
        return sb.toString();
    }

    public void setItems(List<String> items, String defaultText, List<String> ids) {
        this.defaultText = defaultText;
        _items = items.toArray(new String[items.size()]);
        _ids = ids.toArray(new String[ids.size()]);
        mSelection = new boolean[_items.length];
        mSelectionAtStart = new boolean[_items.length];
        simple_adapter.clear();
        simple_adapter.add(defaultText);
        Arrays.fill(mSelection, false);
    }

    public String getSelectedItemIds() {
        if (_ids == null) {
            Log.e(TAG, "getSelectedItemIds : " + " _ids are null");
            Log.e(TAG, "getSelectedItemIds : " +
                    " You must call setItems (List<String> items, String defaultText, List<String> ids)"
                    + " to use this method."
            );
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;
        if (_items != null) {
            for (int i = 0; i < _items.length; ++i) {
                if (mSelection[i]) {
                    if (foundOne) {
                        sb.append(",");
                    }
                    foundOne = true;
                    sb.append(_ids[i]);
                }
            }
        }
        return sb.toString();
    }


    /*public class SpinnerAdapter extends BaseAdapter implements Filterable {

        List<String> tempValues;
        List<String> values;
        LayoutInflater inflater;

        public SpinnerAdapter(Context context, List<String> values) {
            this.values = values;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }

        @Override
        public Filter getFilter() {
            return null;
        }
    }*/
}