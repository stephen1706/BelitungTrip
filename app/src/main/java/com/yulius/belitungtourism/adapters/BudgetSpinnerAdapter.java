package com.yulius.belitungtourism.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yulius.belitungtourism.FormattingUtil;

public class BudgetSpinnerAdapter extends ArrayAdapter<Integer> {
    private int mRange;
    private Context mContext;

    public BudgetSpinnerAdapter(Context context, int range) {
        super(context, android.R.layout.simple_dropdown_item_1line);
        mRange = range;
        mContext = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        String text = "Rp " + FormattingUtil.formatDecimal(getItem(position)) + " - Rp " + FormattingUtil.formatDecimal((getItem(position) + mRange));
        convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(text);

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String text = "Rp " + FormattingUtil.formatDecimal(getItem(position)) + " - Rp " + FormattingUtil.formatDecimal((getItem(position) + mRange));
        convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(text);

        return convertView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }

    public long getRange() {
        return mRange;
    }

}
