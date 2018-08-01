package it.docdev.mypiggybank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import it.docdev.mypiggybank.R;

public class CustomAdapter extends BaseAdapter {

    private int flags[];
    private String[] countryNames;
    private LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, int[] flags, String[] countryNames) {
        this.flags = flags;
        this.countryNames = countryNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return flags.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflter.inflate(R.layout.spinner_item, viewGroup, false);
        }
        ViewHolder holder;

        holder = new ViewHolder();
        holder.flag = view.findViewById(R.id.flagImage);
        holder.txt = view.findViewById(R.id.flagCurrency);
        holder.flag.setImageResource(flags[i]);
        holder.txt.setText(countryNames[i]);

        return view;
    }

    private class ViewHolder {
        public TextView txt;
        public ImageView flag;
    }
}


