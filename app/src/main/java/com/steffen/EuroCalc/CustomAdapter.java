package com.steffen.EuroCalc;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter implements SpinnerAdapter {
    Logic logic = new Logic();
    String[] country;
    Context context;
    int [] flaggen;
    public CustomAdapter(Context context, String[] country) {
        this.country = country;
        this.context = context;
        this.flaggen = logic.getFlaggsArray(country);
    }

    @Override
    public int getCount() {
        return country.length;
    }

    @Override
    public Object getItem(int position) {
        return country[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.spinner_selected,null);
        TextView textView = (TextView) view.findViewById(R.id.main);
        ImageView img = (ImageView) view.findViewById(R.id.flaggeSpinner);
        textView.setText(country[position]);
        return textView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view;
        view = View.inflate(context, R.layout.spinner_item_layout, null);
        final TextView textView = (TextView) view.findViewById(R.id.land);
        textView.setText(country[position]);

        ImageView img = (ImageView) view.findViewById(R.id.flaggeSpinner);
        img.setBackgroundResource(flaggen[position]);

        return view;
    }

    int getItemByString(String input) {
        int length = country.length;
        for (int i = 0; i < length; i++) {
            if (country[i].equals(input)) {
                return i;
            }
        }
        return 0;
    }
}
