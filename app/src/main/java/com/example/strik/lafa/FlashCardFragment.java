package com.example.strik.lafa;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class FlashCardFragment extends Fragment {

    /**
     * The related object to the fragment
     */
    private FlashCard card;

    /**
     * Determines which part of the flashcard is visible
     */
    private boolean isViewingAnswer = false;

    public FlashCardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            card = getArguments().getParcelable("card");
        }
    }

    /**
     * Apply the data of the flashcard to the UI view
     *
     * @param view View the fragment view
     */
    private void applyCardDetails(View view)
    {
        if (card != null)
        {
            ((TextView)view.findViewById(R.id.text_view_question)).setText(card.getQuestion());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flashcard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyCardDetails(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCardMode(view);
            }
        });
    }

    /**
     * Set the current contents of the card depending on if the answer is being
     * viewed or not.
     *
     * @param view the view for the fragment
     */
    private void setCardMode(View view)
    {
        TextView textViewOutput = view.findViewById(R.id.text_view_question);
        textViewOutput.setText("");
        YoYo.with(Techniques.Shake).duration(100).playOn(view);
        textViewOutput.setText((isViewingAnswer = !isViewingAnswer)
                ? card.getAnswer() : card.getQuestion());
    }
}
