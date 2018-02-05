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

        TextView teams = rowView.findViewById(R.id.partido_list_vs);
        TextView status = rowView.findViewById(R.id.partido_list_estatus);
        TextView date = rowView.findViewById(R.id.partido_date);
        TextView score = rowView.findViewById(R.id.partido_marcador);

        PartidoDTO p = (PartidoDTO) getItem(position);
        if (position % 2 == 1) {
            rowView.setBackgroundColor(Color.LTGRAY);
        } else {
            rowView.setBackgroundColor(Color.GRAY);
        }
        String versus = p.getHomeTeam() + " vs " + p.getAwayTeam();
        teams.setText(versus);
        status.setText(p.getEventStatus());
        date.setText(p.getDate());
        String marcador = p.getHomeScore() + " - " + p.getAwayScore();
        score.setText(marcador);

        return rowView;
    }
}
