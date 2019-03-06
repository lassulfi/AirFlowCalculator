package com.poseidonos.airflowcalculator;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.poseidonos.airflowcalculator.adapter.FarmAdapter;
import com.poseidonos.airflowcalculator.data.FarmContract;

import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.COLUMN_FARM_NAME;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry.CONTENT_URI;
import static com.poseidonos.airflowcalculator.data.FarmContract.FarmEntry._ID;

public class RetriveProjectActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FARM_LOADER = 0;

    private FarmAdapter adapter;

    private Toolbar toolbar;

    private ListView listView;

    private boolean confirmDeleteVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_project);

        toolbar = findViewById(R.id.load_data_toolbar);
        toolbar.setTitle(R.string.toolbar_load_data_title);

        listView = findViewById(R.id.farms_listview);

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportLoaderManager().initLoader(FARM_LOADER, null, this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_load_data, menu);
        MenuItem confirmItem = menu.findItem(R.id.action_delete_confirm);
        if(confirmDeleteVisible == true){
            confirmItem.setVisible(true);
        } else {
            confirmItem.setVisible(false);
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete_forever:
                invalidateOptionsMenu();
                showOrHideConfirmButtonAndCheckBoxes();
                return true;
            case R.id.action_delete_confirm:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_builder_delete_forever_message);
                builder.setPositiveButton(R.string.dialog_builder_delete_forever_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteSelectedUri();
                        invalidateOptionsMenu();
                    }
                });
                builder.setNegativeButton(R.string.dialog_builder_delete_forever_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if(dialog != null){
                            dialog.dismiss();
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showOrHideConfirmButtonAndCheckBoxes() {
        if(listView.getCount() > 0) {
            if (confirmDeleteVisible == true) {
                for (int i = 0; i < listView.getCount(); i++) {
                    View view = listView.getChildAt(i);
                    CheckBox checkBox = view.findViewById(R.id.list_item_checkbox);
                    checkBox.setVisibility(View.INVISIBLE);
                }
                confirmDeleteVisible = false;
            } else {
                for (int i = 0; i < listView.getCount(); i++) {
                    View view = listView.getChildAt(i);
                    CheckBox checkBox = view.findViewById(R.id.list_item_checkbox);
                    checkBox.setChecked(false);
                    checkBox.setVisibility(View.VISIBLE);
                }
                confirmDeleteVisible = true;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void deleteSelectedUri() {
        for(int i = 0; i < listView.getCount(); i++){
            View view = listView.getChildAt(i);
            CheckBox checkBox = view.findViewById(R.id.list_item_checkbox);
            TextView textView = view.findViewById(R.id.site_name_textview);
            if(checkBox.isChecked()){
                String[] projection = {_ID, FarmContract.FarmEntry.COLUMN_FARM_NAME};
                String selection = FarmContract.FarmEntry.COLUMN_FARM_NAME + " = ?";
                String[] selectionArgs = {textView.getText().toString()};
                Cursor cursor = getContentResolver().query(CONTENT_URI,
                        projection, selection, selectionArgs, null, null);
                Uri uri = null;
                if(cursor.moveToFirst()){
                    uri = ContentUris.withAppendedId(CONTENT_URI,
                            cursor.getInt(cursor.getColumnIndex(_ID)));
                    deleteUri(uri);
                    confirmDeleteVisible = false;
                } else {
                    Toast.makeText(RetriveProjectActivity.this,
                            "Error deleting farm", Toast.LENGTH_SHORT).show();
                }
            }
            checkBox.setVisibility(View.INVISIBLE);
        }
    }

    private boolean deleteUri(Uri uri) {
        if(uri != null){
            int lines = getContentResolver().delete(uri, null, null);
            if(lines == 0){
                Toast.makeText(getApplicationContext(),
                        R.string.toast_error_deleting_farm,
                        Toast.LENGTH_SHORT).
                        show();
                return false;
            } else{
                Toast.makeText(getApplicationContext(),
                        R.string.toast_deleting_farm_confirmed,
                        Toast.LENGTH_SHORT).show();
                getSupportLoaderManager().restartLoader(FARM_LOADER, null, this);
                return true;
            }
        } else {
            return false;
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
