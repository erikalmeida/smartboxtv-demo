package com.example.erikalmeida.smartbox_demo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * Created by erikalmeida on 05/02/2018.
 */

public class PartidoAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater mInflater;
    private List<PartidoDTO> mDataSource;

    public PartidoAdapter(Context context, List<PartidoDTO> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }


    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_partido, parent, false);
        View rowView2 = mInflater.inflate(R.layout.list_item_partido_2, parent, false);

        TextView teams;
        TextView status;
        TextView date;
        TextView score;

        PartidoDTO p = (PartidoDTO) getItem(position);
        String marcador = p.getHomeScore() + " - " + p.getAwayScore();

        if (position % 2 != 0) {
            teams = rowView.findViewById(R.id.partido_list_vs);
            status = rowView.findViewById(R.id.partido_list_estatus);
            date = rowView.findViewById(R.id.partido_date);
            score = rowView.findViewById(R.id.partido_marcador);

            teams.setText(p.getVersus());
            status.setText(p.getEventStatus());
            date.setText(p.getDate());
            score.setText(marcador);

            rowView.setBackgroundColor(Color.LTGRAY);
            return rowView;
        } else {
            teams = rowView2.findViewById(R.id.partido_list_vs);
            status = rowView2.findViewById(R.id.partido_list_estatus);
            date = rowView2.findViewById(R.id.partido_date);
            score = rowView2.findViewById(R.id.partido_marcador);

            teams.setText(p.getVersus());
            status.setText(p.getEventStatus());
            date.setText(p.getDate());
            score.setText(marcador);

            rowView2.setBackgroundColor(Color.GRAY);
            return rowView2;
        }
    }
}
