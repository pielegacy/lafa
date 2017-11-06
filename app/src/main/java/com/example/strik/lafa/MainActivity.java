package com.example.strik.lafa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateFlashSets();
        findViewById(R.id.fab_home_add)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent navIntent = new Intent(getApplicationContext(), CreateFlashSetActivity.class);
                        startActivity(navIntent);
                    }
                });
        if (!isNetworkAvailable())
            Snackbar.make(findViewById(R.id.layout_output),
                    "No internet connection, some features might be unavailable.",
                    Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAttachFragment(android.app.Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_download:
                if (isNetworkAvailable())
                    openDownloadDialog("");
                else
                    Toast.makeText(this,
                            "Unable to download FlashSets without an internet connection.",
                            Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_settngs:
                Intent navIntent = new Intent(this, SettingsActivity.class);
                startActivity(navIntent);
                return true;
            default:
                return false;
        }
    }

    private void openDownloadDialog(String defaultUrl) {
        if (defaultUrl.isEmpty()) {
            LayoutInflater inflater = getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_download_flashset, null);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Download FlashSet")
                    .setView(view)
                    .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText editTextUrl = view.findViewById(R.id.editText_set_endpoint);
                            if (!editTextUrl.getText().toString().isEmpty())
                                LAFA.downloadFlashSet(getApplicationContext(),
                                        MainActivity.this,
                                        editTextUrl.getText().toString());
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            dialog.show();

        }
    }

    /**
     * Populate the FlashSet lists in the homepage
     */
    public void populateFlashSets() {
        LAFA.loadSetsFromFile(this);

        RecyclerView recyclerViewLibrary = (RecyclerView) findViewById(R.id.recyclerView_library);

        FlashSetRecyclerAdapter flashSetRecyclerAdapterLibrary
                = new FlashSetRecyclerAdapter(LAFA.getAppSets());
        recyclerViewLibrary.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerViewLibrary.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLibrary.addItemDecoration(new FlashSetRecyclerDecoration());
        recyclerViewLibrary.setAdapter(flashSetRecyclerAdapterLibrary);
    }

    /**
     * Use to check if device is connected to a data network.
     *
     * @return true if connected, false if not.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
