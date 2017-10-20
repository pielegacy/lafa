package com.example.strik.lafa;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class ViewFlashSetActivity extends AppCompatActivity {

    private FlashSet flashSet;
    private FlashSetManager flashSetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flash_set);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        flashSet = getIntent().getExtras().getParcelable("data");
        flashSetManager = new FlashSetManager(this, R.id.layout_flashset, flashSet);
        flashSetManager.pushCardToList(0);
        flashSetManager.replaceCardInList(1);
        setTitle(flashSet.getName());
    }

}
