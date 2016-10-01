package com.joefazzino.referendumpetitioncounter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by joefazzino on 25/06/2016.
 */
public class JsonGrabber {
    private String mUrl;
    private String mData;
    private String title, strCount;
    private Context mainContext;
    private int counter = 1;
    private String LOG_TAG = JsonGrabber.class.getSimpleName();

    public String getTitle() {
        return title;
    }

    public String getStrCount() {
        return strCount;
    }

    public JsonGrabber(String mUrl, Context context) {
        this.mUrl = mUrl;
        this.mainContext = context;


    }

    public void execute() {
        GetJsonData getJsonData = new GetJsonData();
        getJsonData.execute(mUrl);
    }

    public class GetJsonData extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {

        }

        @Override
        protected void onProgressUpdate(String... values) {
            mData = values[0];
            ProcessResults();

            ((MainActivity) mainContext).txtTitle.setText(getTitle());
            ((MainActivity) mainContext).txtCounter.setText(getStrCount());

            if (counter == 1) {
                counter++;
            }
            else {
                try {
                    Thread.sleep(5000);
                    counter++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            new GetJsonData().execute(mUrl);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            if (params == null)
                return null;

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                if (inputStream == null)
                    return null;

                StringBuffer buffer = new StringBuffer();

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    buffer.append(line + "\n");
                }


                Log.d(LOG_TAG, "DATA = " + buffer.toString());


                publishProgress(buffer.toString());
                return null;

            }
            catch (IOException e) {
                Log.d(LOG_TAG, "Error when downloading data", e);
                return null;
            }
            finally {
                if (urlConnection != null)
                    urlConnection.disconnect();

                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                        return null;
                    }
                    catch (final IOException e) {
                        Log.d(LOG_TAG, "Error closing reader", e);
                        return null;
                    }
                }
            }
        }
    }

    public void ProcessResults() {
        final String DATA = "data", ATTRIBUTES = "attributes", ACTION = "action", SIGNATURE_COUNT = "signature_count";

        try {
            JSONObject jsonObject = new JSONObject(mData);
            JSONObject jsonData = jsonObject.getJSONObject(DATA);
            JSONObject jsonAttributes = jsonData.getJSONObject(ATTRIBUTES);
            title = jsonAttributes.getString(ACTION);
            Integer count = jsonAttributes.getInt(SIGNATURE_COUNT);

            strCount = count.toString();

            Log.d(LOG_TAG, "Title = " + title + "\nCount = " + strCount);



        }
        catch (JSONException jsone) {
            jsone.printStackTrace();
            Log.e(LOG_TAG, "Error Processing JSON");
        }
    }



}
