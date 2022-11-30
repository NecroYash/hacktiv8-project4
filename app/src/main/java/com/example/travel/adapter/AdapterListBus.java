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
import com.example.travel.SeatsActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterListBus extends RecyclerView.Adapter<AdapterListBus.ViewHolder> {

    Context context;
    ArrayList<String> from, to, listNameBus, time, listDate, listTotalDate, listTotalTime, linkBus;
    ArrayList<Double> price;
    ArrayList<Integer> listLongTime;
    private LayoutInflater mInflater;

    public AdapterListBus(Context context, ArrayList<String> from, ArrayList<String> to, ArrayList<String> listNameBus, ArrayList<Double> price, ArrayList<String> time,ArrayList<Integer> listLongTime, ArrayList<String> listDate, ArrayList<String> listTotalDate, ArrayList<String> listTotalTime, ArrayList<String> linkBus) {
        this.context = context;
        this.from = from;
        this.to = to;
        this.listNameBus = listNameBus;
        this.price = price;
        this.mInflater = LayoutInflater.from(context);
        this.time = time;
        this.listLongTime = listLongTime;
        this.listDate = listDate;
        this.listTotalDate = listTotalDate;
        this.listTotalTime = listTotalTime;
        this.linkBus = linkBus;
    }


    @NonNull
    @Override
    public AdapterListBus.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_booking_bus, parent, false);
        return new AdapterListBus.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AdapterListBus.ViewHolder holder, int position) {
        holder.fromBooking.setText(from.get(position));
        holder.toBooking.setText(to.get(position));
        holder.nameBus.setText(listNameBus.get(position));
        holder.priceBooking.setText(getPrice(price.get(position))+"");
        holder.timeBooking.setText(time.get(position));
        holder.longTime.setText(listLongTime.get(position) + "H");
        holder.date.setText(listDate.get(position));
        holder.totalTime.setText(listTotalTime.get(position));
        holder.totalDate.setText(listTotalDate.get(position));
    }

    private String getPrice(double price){
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        return formatRupiah.format(price);
    }

    @Override
    public int getItemCount() {
        return listDate.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout rowBooking;
        TextView  fromBooking, toBooking, priceBooking, timeBooking, longTime, nameBus, totalTime, totalDate, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fromBooking = itemView.findViewById(R.id.fromBusActivity);
            toBooking = itemView.findViewById(R.id.toBusActivity);
            nameBus = itemView.findViewById(R.id.nameBusActivity);
            priceBooking = itemView.findViewById(R.id.priceBusActivity);

            timeBooking = itemView.findViewById(R.id.timeBusActivity);
            longTime = itemView.findViewById(R.id.busLongTime);

            totalTime = itemView.findViewById(R.id.totalTimeBusActivity);
            totalDate = itemView.findViewById(R.id.totalDateBusActivity);
            date = itemView.findViewById(R.id.dateBusActivity);

            rowBooking = itemView.findViewById(R.id.rowBusActivity);

            rowBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SeatsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("from", from.get(getAdapterPosition()));
                    intent.putExtra("to", to.get(getAdapterPosition()));
                    intent.putExtra("date", listDate.get(getAdapterPosition()));
                    intent.putExtra("time", time.get(getAdapterPosition()));
                    intent.putExtra("price", price.get(getAdapterPosition()));
                    intent.putExtra("longTime", listLongTime.get(getAdapterPosition()));
                    intent.putExtra("totalTime", listTotalTime.get(getAdapterPosition()));
                    intent.putExtra("totalDate", listTotalDate.get(getAdapterPosition()));
                    intent.putExtra("nameBus", listNameBus.get(getAdapterPosition()));
                    intent.putExtra("linkBus", linkBus.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }

}
