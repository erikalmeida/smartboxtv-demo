package com.example.erikalmeida.smartbox_demo;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityPartidoDetail extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partido_detail);
        LinearLayout view = (LinearLayout) findViewById(R.id.detail_view);

        String versus = this.getIntent().getExtras().getString("versus");
        String score = this.getIntent().getExtras().getString("score");
        String estatus = this.getIntent().getExtras().getString("estatus");

        setTitle(versus);

        TextView versusDetail = view.findViewById(R.id.versus_detail);
        TextView scoreDetail = view.findViewById(R.id.score_detail);
        TextView estatusDetail = view.findViewById(R.id.estatus_detail);

        versusDetail.setText(versus);
        scoreDetail.setText(score);
        estatusDetail.setText(estatus);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
}
