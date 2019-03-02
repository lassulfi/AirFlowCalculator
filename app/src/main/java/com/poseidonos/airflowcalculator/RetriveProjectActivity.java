package com.poseidonos.airflowcalculator;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.poseidonos.airflowcalculator.adapter.FarmAdapter;
import com.poseidonos.airflowcalculator.controller.FarmController;

import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_FARM_NAME;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.CONTENT_URI;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry._ID;

public class RetriveProjectActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FARM_LOADER = 0;

    FarmAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_project);

        ListView listView = findViewById(R.id.farms_listview);

        View emptyView = findViewById(R.id.empty_view_layout);
        listView.setEmptyView(emptyView);

        adapter = new FarmAdapter(this, null);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(RetriveProjectActivity.this, MainActivity.class);
                Uri clickedFarmUri = ContentUris.withAppendedId(CONTENT_URI, id);

                intent.setData(clickedFarmUri);
                startActivity(intent);
            }
        });

        //Enable button click if the listview is empty
        if(listView.getAdapter().getCount() == 0){
            Button btnStartNewConfiguration = findViewById(R.id.btn_start_new_calc);
            btnStartNewConfiguration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(RetriveProjectActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        getLoaderManager().initLoader(FARM_LOADER, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {_ID, COLUMN_FARM_NAME};
        return new CursorLoader(this, CONTENT_URI, projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
