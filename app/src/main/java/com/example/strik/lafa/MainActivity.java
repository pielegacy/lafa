package com.example.strik.lafa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateFlashSets();
    }

    @Override
    public void onAttachFragment(android.app.Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    /**
     * Populate the FlashSet lists in the homepage
     */
    private void populateFlashSets()
    {
        ArrayList<FlashSet> arrayListFlashSet = new ArrayList<>();
        arrayListFlashSet.add(dummyFlashSet());
        arrayListFlashSet.add(dummyFlashSet());
        arrayListFlashSet.add(dummyFlashSet());
        arrayListFlashSet.add(dummyFlashSet());
        arrayListFlashSet.add(dummyFlashSet());

        RecyclerView recyclerViewLibrary = (RecyclerView)findViewById(R.id.recyclerView_library);

        FlashSetRecyclerAdapter flashSetRecyclerAdapterLibrary
                = new FlashSetRecyclerAdapter(arrayListFlashSet);
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

    private FlashSet dummyFlashSet()
    {
        ArrayList<FlashCard> flashCards = new ArrayList<>();
        flashCards.add(new FlashCard(0, "Q1", "A1"));
        flashCards.add(new FlashCard(1, "Q2", "A2"));
        flashCards.add(new FlashCard(2, "Q3", "A3"));
        return new FlashSet(flashCards, "Test Flash Set");
    }
}
