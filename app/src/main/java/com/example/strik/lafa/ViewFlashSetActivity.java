package com.example.strik.lafa;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.TouchTypeDetector;

public class ViewFlashSetActivity extends AppCompatActivity {

    private FlashSet flashSet;
    private FlashSetManager flashSetManager;
    private LAFASpeech lafaSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sensey.getInstance().init(this);
        Sensey.getInstance().startTouchTypeDetection(this, touchTypListener);
        setContentView(R.layout.activity_view_flash_set);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.DKGRAY);
        setSupportActionBar(toolbar);
        if (getIntent().getExtras().getParcelable("data") != null)
            loadFlashSet((FlashSet) getIntent().getExtras().getParcelable("data"));
        else
            Snackbar.make(findViewById(R.id.layout_flashset),
                    "Invalid FlashSet Passed", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flashset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                if (!isNetworkAvailable()) {
                    Snackbar.make(findViewById(R.id.layout_flashset),
                            "Uploading & Sharing FlashSet...", Snackbar.LENGTH_LONG).show();
                    LAFA.shareFlashSet(this, flashSet);
                } else
                    Toast.makeText(this,
                            "Unable to share FlashSet without an internet connection.",
                            Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_shuffle:
                flashSetManager.shuffleCards();
                Snackbar.make(findViewById(R.id.layout_flashset),
                        "Shuffled FlashSet...", Snackbar.LENGTH_SHORT).show();
                applyFlashCardData();
                return true;
            case R.id.menu_item_listen:
                lafaSpeech.speak(flashSetManager.getCurrentCard().getQuestion());
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onDestroy() {
        Sensey.getInstance().stop();
        super.onDestroy();
    }

    /**
     * Apply the data of the FlashCard to the view
     */
    private void applyFlashCardData() {
        ((TextView) findViewById(R.id.textView_flashSetCurrentIndex))
                .setText(flashSetManager.getSetLabel());
    }

    /**
     * Load the data of a FlashSet into the current Activity
     *
     * @param flashSet the set of flashcards being passed through
     */
    private void loadFlashSet(FlashSet flashSet) {
        this.flashSet = flashSet;
        flashSetManager = new FlashSetManager(this, R.id.layout_flashset, flashSet);
        flashSetManager.pushCard(0);
        setTitle(this.flashSet.getName());
        applyFlashCardData();
        lafaSpeech = new LAFASpeech(this);
    }

    TouchTypeDetector.TouchTypListener touchTypListener = new TouchTypeDetector.TouchTypListener() {
        @Override
        public void onTwoFingerSingleTap() {

        }

        @Override
        public void onThreeFingerSingleTap() {

        }

        @Override
        public void onDoubleTap() {

        }

        @Override
        public void onScroll(int i) {

        }

        @Override
        public void onSingleTap() {

        }

        @Override
        public void onSwipe(int i) {
            GridLayout layoutFlashSet = (GridLayout) findViewById(R.id.layout_flashset);
            switch (i) {
                case TouchTypeDetector.SWIPE_DIR_RIGHT:
                    flashSetManager.previousCard();
                    applyFlashCardData();
                    break;
                case TouchTypeDetector.SWIPE_DIR_LEFT:
                    flashSetManager.nextCard();
                    applyFlashCardData();
                    break;
            }
        }

        @Override
        public void onLongPress() {

        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // Setup onTouchEvent for detecting type of touch gesture
        Sensey.getInstance().setupDispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
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
