package com.example.strik.lafa;


import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FlashSetRecyclerAdapter
        extends RecyclerView.Adapter<FlashSetRecyclerAdapter.FlashSetViewHolder> {

    private ArrayList<FlashSet> flashSetList;

    class FlashSetViewHolder extends RecyclerView.ViewHolder {
        TextView textViewFlashSetName;
        TextView textViewFlashSetDetails;
        CardView cardViewFlashSet;

        FlashSetViewHolder(View view) {
            super(view);
            textViewFlashSetName = view.findViewById(R.id.textView_flashSetName);
            textViewFlashSetDetails = view.findViewById(R.id.textView_flashSetDetails);
            cardViewFlashSet = view.findViewById(R.id.cardView_flashSetDetails);
        }
    }

    public FlashSetRecyclerAdapter(ArrayList<FlashSet> flashSetList)
    {
        this.flashSetList = flashSetList;
    }

    @Override
    public FlashSetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.flashset_list_row,
                parent,
                false
        );
        return new FlashSetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FlashSetViewHolder holder, int position) {
        final FlashSet flashSet = flashSetList.get(position);
        holder.textViewFlashSetName.setText(flashSet.getName());
        holder.textViewFlashSetDetails.setText(
                "Created by " + flashSet.getAuthor() + "\n" +
                flashSet.getSet().size() + " card/s"
        );
        holder.cardViewFlashSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navIntent = new Intent(view.getContext(), ViewFlashSetActivity.class);
                navIntent.putExtra("data", flashSet);
                view.getContext().startActivity(navIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flashSetList.size();
    }

}
