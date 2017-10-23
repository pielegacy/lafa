package com.example.strik.lafa;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Simple ArrayAdapter for FlashCard ordering
 */
public class FlashCardAdapter extends ArrayAdapter<FlashCard> {

    public FlashCardAdapter(Context context, List<FlashCard> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FlashCard flashCard = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.flashcard_list_row, parent);
        }

        TextView textViewFlashCardQuestion =
                convertView.findViewById(R.id.textView_flashCardQuestion);

        textViewFlashCardQuestion.setText(flashCard.getQuestion());

        return convertView;
    }
}
