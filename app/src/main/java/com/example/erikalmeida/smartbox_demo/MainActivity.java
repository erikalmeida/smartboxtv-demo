package com.example.erikalmeida.smartbox_demo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Object> partidos = new ArrayList<>();
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginServicio ls = new LoginServicio();
        new LoginServicio().execute();
    }



    public void showListPartidos() {
        final List<PartidoDTO> listItems = new ArrayList<>();
        if(partidos!=null) {
            mListView = findViewById(R.id.partidos_list_view);
            for(int i = 0; i < partidos.size(); i++){
                PartidoDTO partidoDTO = new PartidoDTO();
                LinkedHashMap<String, Object> p = (LinkedHashMap<String, Object>) partidos.get(i);

                LinkedHashMap<String,Object> homeTeam = (LinkedHashMap<String, Object>) p.get("homeTeam");
                LinkedHashMap<String,Object> awayTeam = (LinkedHashMap<String, Object>) p.get("awayTeam");
                LinkedHashMap<String,Object> eventStatus = (LinkedHashMap<String, Object>) p.get("eventStatus");
                LinkedHashMap<String,Object> eventStatusName = (LinkedHashMap<String, Object>) eventStatus.get("name");
                LinkedHashMap<String,Object> matchDay = (LinkedHashMap<String, Object>) p.get("matchDay");
                partidoDTO.setHomeTeam((String) homeTeam.get("name"));
                partidoDTO.setAwayTeam((String) awayTeam.get("name"));
                partidoDTO.setHomeScore(p.get("homeScore").toString());
                partidoDTO.setAwayScore(p.get("awayScore").toString());
                partidoDTO.setEventStatus((String) eventStatusName.get("es"));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                String dateInString = matchDay.get("start").toString();
                partidoDTO.setVersus(partidoDTO.getHomeTeam() + " vs " + partidoDTO.getAwayTeam());
                partidoDTO.setScore(partidoDTO.getHomeScore() + " - " + partidoDTO.getAwayScore());

                Date date = null;
                try {
                    date = formatter.parse(dateInString.replaceAll("Z$", "+0000"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                partidoDTO.setDate(sdf.format(date));
                listItems.add(partidoDTO);
            }
            PartidoAdapter adapter = new PartidoAdapter(this, listItems);
            mListView.setAdapter(adapter);
        }

        final Context context = this;
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PartidoDTO selectedPartido = listItems.get(position);
                Intent detailIntent = new Intent(context, ActivityPartidoDetail.class);

                detailIntent.putExtra("estatus", selectedPartido.getEventStatus());
                detailIntent.putExtra("versus", selectedPartido.getVersus());
                detailIntent.putExtra("score", selectedPartido.getScore());

                startActivity(detailIntent);
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private class LoginServicio extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {}

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //first request (POST)
                final String url = "http://fxservicesstaging.nunchee.com/api/1.0/auth/users/login/anonymous";// url for login
                RestTemplate restTemplate = new RestTemplate(true);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("Authorization", "Basic cHJ1ZWJhc2RldjpwcnVlYmFzZGV2U2VjcmV0");
                String reqjson = "{" +
                        " \"user\": {" +
                        " \"profile\": {" +
                        " \"language\": \"es\"" +
                        " }" +
                        " }," +
                        " \"device\": {" +
                        " \"deviceId\": \"1234567\"," +
                        " \"name\": \"MyPhone\"," +
                        " \"version\": \"4.4.4\"," +
                        " \"width\": \"640\"," +
                        " \"heigth\": \"960\"," +
                        " \"model\": \"Awesome Model 6\"," +
                        " \"platform\": \"android\"" +
                        " }," +
                        " \"app\": {" +
                        " \"version\": \"1.0.0\"" +
                        " }" +
                        "}";
                HttpEntity<String> httpEntity = new HttpEntity<String>(reqjson,headers);
                String response = restTemplate.postForObject(url,httpEntity, String.class);
                JSONObject responseBody = new JSONObject(response);
                String data = responseBody.getJSONObject("data").get("accessToken").toString();

                // second request (GET)
                final String url2 = "http://fxservicesstaging.nunchee.com/api/1.0/sport/events";// url for partidos
                RestTemplate restTemplate2 = new RestTemplate(true);
                HttpHeaders headers2 = new HttpHeaders();
                headers2.add("Authorization", "Bearer" + " " + data);
                HttpEntity<String> httpEntity2 = new HttpEntity<String>(headers2);
                ResponseEntity<Object> response2 = restTemplate.exchange(url2, HttpMethod.GET, httpEntity2, Object.class);//restTemplate2.getForEntity(url2,Object.class,httpEntity2);
                LinkedHashMap<String, Object> responseData = (LinkedHashMap<String, Object>) response2.getBody();
                LinkedHashMap<String, Object> completeData = (LinkedHashMap<String, Object>) responseData.get("data");
                List<Object> items = (List<Object>) completeData.get("items");
                Log.i("accessToken", data);
                partidos = items;
            } catch (Exception e) {
                Log.e("Login", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showListPartidos();
        }
    }
}
