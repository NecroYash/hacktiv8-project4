package com.example.travel.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel.BookATrip;
import com.example.travel.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterListBooking extends RecyclerView.Adapter<AdapterListBooking.ViewHolder> {

    Context context;
    ArrayList<String> date, from, to, nameBus, linkBus, seat, time, totalTime, totalDate;
    ArrayList<Double> price;
    ArrayList<Integer> longTime;
    private LayoutInflater mInflater;

    public AdapterListBooking(Context context, ArrayList<String> date, ArrayList<String> from, ArrayList<String> to, ArrayList<String> seat, ArrayList<String> nameBus , ArrayList<String> linkBus, ArrayList<Double> price, ArrayList<String> time, ArrayList<String> totalTime, ArrayList<String> totalDate, ArrayList<Integer> longTime) {
        this.context = context;
        this.date = date;
        this.from = from;
        this.to = to;
        this.seat = seat;
        this.nameBus = nameBus;
        this.linkBus = linkBus;
        this.price = price;
        this.mInflater = LayoutInflater.from(context);
        this.time = time;
        this.totalTime = totalTime;
        this.totalDate = totalDate;
        this.longTime = longTime;
    }


    @NonNull
    @Override
    public AdapterListBooking.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_booking, parent, false);
        return new AdapterListBooking.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterListBooking.ViewHolder holder, int position) {
        holder.dateBooking.setText(date.get(position));
        holder.fromBooking.setText(from.get(position));
        holder.toBooking.setText(to.get(position));
        holder.seatBooking.setText(seat.get(position));
        holder.nameBus.setText(nameBus.get(position));
        holder.priceBooking.setText(getPrice(price.get(position))+"");
        holder.timeBooking.setText(time.get(position));
        holder.totalTimeBooking.setText(totalTime.get(position));
        holder.totalDateBooking.setText(totalDate.get(position));
        holder.longTime.setText(longTime.get(position) + "H");
    }

    private String getPrice(double price){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(price);
    }

    @Override
    public int getItemCount() {
        return date.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout rowBooking;
        TextView dateBooking, fromBooking, toBooking,  seatBooking, priceBooking, timeBooking, totalTimeBooking, totalDateBooking, longTime, nameBus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateBooking = itemView.findViewById(R.id.dateBookingList);
            fromBooking = itemView.findViewById(R.id.fromBookingList);
            toBooking = itemView.findViewById(R.id.toBookingList);
            seatBooking = itemView.findViewById(R.id.seatBookingList);
            nameBus = itemView.findViewById(R.id.nameBusBookingList);
            priceBooking = itemView.findViewById(R.id.priceBookingList);

            timeBooking = itemView.findViewById(R.id.timeBookingList);
            totalTimeBooking = itemView.findViewById(R.id.totalTime);
            totalDateBooking = itemView.findViewById(R.id.totalDate);
            longTime = itemView.findViewById(R.id.longTime);

            rowBooking = itemView.findViewById(R.id.rowBookingProfile);

            rowBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, BookATrip.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("dateBooking", dateBooking.getText().toString());
                    intent.putExtra("fromBooking", fromBooking.getText().toString());
                    intent.putExtra("toBooking", toBooking.getText().toString());
                    intent.putExtra("seatBooking", seatBooking.getText().toString());
                    intent.putExtra("nameBus", nameBus.getText().toString());
                    intent.putExtra("linkBus", linkBus.get(getAdapterPosition()));
                    intent.putExtra("timeBooking", timeBooking.getText().toString());
                    intent.putExtra("totalTimeBooking", totalTimeBooking.getText().toString());
                    intent.putExtra("totalDateBooking", totalDateBooking.getText().toString());
                    intent.putExtra("longTime", longTime.getText().toString());
                    intent.putExtra("priceBooking", priceBooking.getText().toString());
                    intent.putExtra("context", "list");
                    context.startActivity(intent);
                }
            });
        }
    }

}
