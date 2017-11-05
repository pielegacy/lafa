package com.example.strik.lafa;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sensey.getInstance().init(this);
        Sensey.getInstance().startTouchTypeDetection(this ,touchTypListener);
        setContentView(R.layout.activity_view_flash_set);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getIntent().getExtras().getParcelable("data") != null)
            loadFlashSet((FlashSet)getIntent().getExtras().getParcelable("data"));
        else
            Snackbar.make(findViewById(R.id.layout_flashset),
                    "Invalid FlashSet Passed", Snackbar.LENGTH_SHORT).show();

        ImageButton buttonShuffle = (ImageButton)findViewById(R.id.button_shuffle);
        buttonShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashSetManager.shuffleCards();
                Snackbar.make(findViewById(R.id.layout_flashset),
                        "Shuffled FlashSet...", Snackbar.LENGTH_SHORT).show();
                applyFlashCardData();
            }
        });
        buttonShuffle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(), "Shuffle", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ImageButton buttonShare = (ImageButton)findViewById(R.id.button_share);
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(findViewById(R.id.layout_flashset),
                        "Uploading & Sharing FlashSet...", Snackbar.LENGTH_LONG).show();
                LAFA.shareFlashSet(view.getContext(), flashSet);
            }
        });
        buttonShare.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(), "Share", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        Sensey.getInstance().stop();
        super.onDestroy();
    }

    /**
     * Apply the data of the FlashCard to the view
     */
    private void applyFlashCardData()
    {
        ((TextView)findViewById(R.id.textView_flashSetCurrentIndex))
                .setText(flashSetManager.getSetLabel());
    }
    /**
     * Load the data of a FlashSet into the current Activity
     * @param flashSet the set of flashcards being passed through
     */
    private void loadFlashSet(FlashSet flashSet)
    {
        this.flashSet = flashSet;
        flashSetManager = new FlashSetManager(this, R.id.layout_flashset, flashSet);
        flashSetManager.pushCard(0);
        setTitle(this.flashSet.getName());
        applyFlashCardData();
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
            GridLayout layoutFlashSet = (GridLayout)findViewById(R.id.layout_flashset);
            switch (i){
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
}
