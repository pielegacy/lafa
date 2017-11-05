package com.example.strik.lafa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
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
        switch (item.getItemId())
        {
            case R.id.menu_item_download:
                openDownloadDialog("");
                return true;
            default:
                return false;
        }
    }

    private void openDownloadDialog(String defaultUrl)
    {
        if (defaultUrl.isEmpty())
        {
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
        // Add a sample set if need be
//        if (LAFA.getAppSets().size() == 0)
//            LAFA.addFlashSet(dummyFlashSet());
        LAFA.loadSetsFromFile(this);

        RecyclerView recyclerViewLibrary = (RecyclerView) findViewById(R.id.recyclerView_library);

        FlashSetRecyclerAdapter flashSetRecyclerAdapterLibrary
                = new FlashSetRecyclerAdapter(LAFA.getAppSets());
        recyclerViewLibrary.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));
        recyclerViewLibrary.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLibrary.addItemDecoration(new FlashSetRecyclerDecoration());
        recyclerViewLibrary.setAdapter(flashSetRecyclerAdapterLibrary);
//        RecyclerView recyclerViewExplore = (RecyclerView)findViewById(R.id.recyclerView_explore);
//
//        FlashSetRecyclerAdapter flashSetRecyclerAdapterExplore
//                = new FlashSetRecyclerAdapter(arrayListFlashSet);
//        recyclerViewExplore.setItemAnimator(new DefaultItemAnimator());
//        recyclerViewExplore.addItemDecoration(new FlashSetRecyclerDecoration());
//        recyclerViewExplore.setAdapter(flashSetRecyclerAdapterExplore);
    }

    private FlashSet dummyFlashSet() {
        ArrayList<FlashCard> flashCards = new ArrayList<>();
        flashCards.add(new FlashCard(0, "Q1", "A1"));
        flashCards.add(new FlashCard(1, "Q2", "A2"));
        flashCards.add(new FlashCard(2, "Q3", "A3"));
        return new FlashSet(flashCards, "Test Flash Set");
    }
}
