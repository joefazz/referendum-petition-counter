package com.joefazzino.referendumpetitioncounter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public TextView txtCounter, txtTitle;
    private String LOG_TAG = "MainActivity";
//    private Button btnRefresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTitle = (TextView) findViewById(R.id.PetitionTitle);
        txtCounter = (TextView) findViewById(R.id.Counter);
//        btnRefresh = (Button) findViewById(R.id.Refresh);

        txtTitle.setText("Loading");
        txtCounter.setText("Loading");

        final JsonGrabber grabber = new JsonGrabber("https://petition.parliament.uk/petitions/131215.json", MainActivity.this);
        grabber.execute();

//        btnRefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                btnRefresh.setVisibility(View.GONE);
//                txtCounter.setText("Loading");
//                txtTitle.setText("Loading");
//            }
//        });






    }



}
