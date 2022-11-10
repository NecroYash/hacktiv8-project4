package com.example.travel.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class AdapterListBooking extends RecyclerView.Adapter<AdapterListBooking.ViewHolder> {

    Context context;
    ArrayList<String> date, seat, time, totalTime, totalDate;
    ArrayList<Double> price;
    ArrayList<Integer> longTime;
    private LayoutInflater mInflater;

    public AdapterListBooking(Context context, ArrayList<String> date, ArrayList<String> seat, ArrayList<Double> price, ArrayList<String> time, ArrayList<String> totalTime, ArrayList<String> totalDate, ArrayList<Integer> longTime) {
        this.context = context;
        this.date = date;
        this.seat = seat;
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
        holder.seatBooking.setText(seat.get(position));
        holder.priceBooking.setText(getPrice(price.get(position)));

        holder.timeBooking.setText(time.get(position));
        holder.totalTimeBooking.setText(totalTime.get(position));
        holder.totalDateBooking.setText(totalDate.get(position));
        holder.longTime.setText(longTime.get(position) + "H");
    }

    private String getPrice(double price){
        DecimalFormat id = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        id.setDecimalFormatSymbols(formatRp);

        return String.format("%s %n", id.format(price));
    }

    @Override
    public int getItemCount() {
        return date.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateBooking, seatBooking, priceBooking, timeBooking, totalTimeBooking, totalDateBooking, longTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateBooking = itemView.findViewById(R.id.dateBookingList);
            seatBooking = itemView.findViewById(R.id.seatBookingList);
            priceBooking = itemView.findViewById(R.id.priceBookingList);

            timeBooking = itemView.findViewById(R.id.timeBookingList);
            totalTimeBooking = itemView.findViewById(R.id.totalTime);
            totalDateBooking = itemView.findViewById(R.id.totalDate);
            longTime = itemView.findViewById(R.id.longTime);
        }
    }

}
