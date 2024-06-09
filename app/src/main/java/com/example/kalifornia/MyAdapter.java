package com.example.kalifornia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

    Context context;
    int flags[];
    String[] country;

    String[] prices;
    LayoutInflater inflter;

    public MyAdapter(Context applicationContext, int[] flags, String[] country, String[] prices) {
        this.context = applicationContext;
        this.flags = flags;
        this.country = country;
        this.prices = prices;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return country.length;
    }

    @Override
    public Object getItem(int i) {
        return new ShopModel(country[i], prices[i]);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_item, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        TextView money = (TextView) view.findViewById(R.id.textView2);

        icon.setImageResource(flags[i]);
        names.setText(country[i]);
        money.setText(prices[i]);
        return view;
    }
}
