package com.example.travel.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel.MainActivity;
import com.example.travel.R;
import com.example.travel.SeatsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    public static int lastIndex = 0;
    public static String dataSeat, pos;
    private String[] mData;
    private LayoutInflater mInflater;
    public Activity activity ;
    Context context;

    List<String> checkSeats;

    public MyRecyclerViewAdapter(Context context, Activity activity,  String[] data, List<String> checkSeats) {
        this.context = context;
        this.activity = activity;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.checkSeats = checkSeats;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recylerview_item, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.myTextView.setText(mData[position]);

        for(int x = position; x < mData.length; x++){
            if(checkSeats.size() != 0){
                for(int y = 0; y < checkSeats.size(); y++){
                    if(Integer.parseInt(checkSeats.get(y)) == position){
                        holder.myTextView.setBackgroundResource(R.drawable.ic_close);
                        holder.myTextView.setText("");
                    }
                    else if(lastIndex != 0 && Integer.parseInt(pos) == position){
                        holder.myTextView.setBackgroundResource(R.drawable.main_outlined_selected);
                        holder.myTextView.setTextColor(R.color.main_color);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView, getSeat;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.info_text);
            getSeat = itemView.findViewById(R.id.getSeat);
            myTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            
            if(lastIndex <= 1 || dataSeat == null){

                for(int x = 0; x < checkSeats.size(); x++){
                    if(getAdapterPosition() == Integer.parseInt(checkSeats.get(x))){
                        Toast.makeText(activity, "Sorry, this seat unavailable", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                dialog(getAdapterPosition(), myTextView);
            }
        }
    }

    private void dialog(int position, TextView textView){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Message").setMessage("Are you sure you select this seat? you cannot change your seat after this.").setCancelable(false).setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
            lastIndex = 10;
            dataSeat = " " + position + mData[position];
            pos = String.valueOf(position);
            Intent intent = new Intent(context, SeatsActivity.class);
            intent.putExtra("data", dataSeat);
            intent.putExtra("pos", pos);
            textView.setBackgroundResource(R.drawable.main_outlined_selected);
            textView.setTextColor(Color.GRAY);
            activity.startActivityForResult(intent, 1);
            activity.finish();
            activity.finish();
        }).setNegativeButton("Cancel", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
