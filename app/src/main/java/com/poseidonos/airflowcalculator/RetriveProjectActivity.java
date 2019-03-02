package com.poseidonos.airflowcalculator;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.poseidonos.airflowcalculator.adapter.FarmAdapter;
import com.poseidonos.airflowcalculator.controller.FarmController;

import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_FARM_NAME;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.CONTENT_URI;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry._ID;

public class RetriveProjectActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FARM_LOADER = 0;

    private FarmAdapter adapter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_project);

        toolbar = findViewById(R.id.load_data_toolbar);
        toolbar.setTitle(R.string.toolbar_load_data_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

        registerForContextMenu(listView);

        getLoaderManager().initLoader(FARM_LOADER, null, this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.farms_listview){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_load_data, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_delete_forever:
                showDeleteConfirmDialog(info);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void showDeleteConfirmDialog(AdapterView.AdapterContextMenuInfo info) {
        final Uri deleteUri = ContentUris.withAppendedId(CONTENT_URI, info.id);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_builder_delete_forever_message);
        builder.setPositiveButton(R.string.dialog_builder_delete_forever_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUri(deleteUri);
            }
        });
        builder.setNegativeButton(R.string.dialog_builder_delete_forever_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteUri(Uri uri) {
        if(uri != null){
            int lines = getContentResolver().delete(uri, null, null);
            if(lines == 0){
                Toast.makeText(getApplicationContext(),
                        R.string.toast_error_deleting_farm,
                        Toast.LENGTH_SHORT).
                        show();
            } else{
                Toast.makeText(getApplicationContext(),
                        R.string.toast_deleting_farm_confirmed,
                        Toast.LENGTH_SHORT).show();
            }
        }
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
