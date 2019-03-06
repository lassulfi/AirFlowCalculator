package com.poseidonos.airflowcalculator.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.poseidonos.airflowcalculator.R;
import com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry;

public class FarmAdapter extends CursorAdapter {

    private Context mContext;
    private Cursor mCursor;

    public FarmAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = view.findViewById(R.id.site_name_textview);

        int siteNameColumnIndex = cursor.getColumnIndex(FarmEntry.COLUMN_FARM_NAME);
        String siteName = cursor.getString(siteNameColumnIndex);

        textView.setText(siteName);
    }
}
