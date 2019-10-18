package com.example.labo2;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SymComManager {

    private static final String TAG = SymComManager.class.getSimpleName();

    private CommunicationEventListener communicationEventListener = null;

    private HttpRequestAsyncTask hrat = new HttpRequestAsyncTask();

    private class HttpRequestAsyncTask extends AsyncTask<String, Void, String> {


        private String POSTContentType = "text/plain";
        private String Host = "";
        private String urlParameters = "";
        private String UserAgent = "";
        private String Accept = "text/*";
        private String Connection = "";
        JSONObject postData;

        @Override
        protected String doInBackground(String... strUrl) {

            Map<String, String> mapData = new HashMap<>();
            mapData.put("data",strUrl[1]);
            this.postData = new JSONObject(mapData);

            try {

                URL url = new URL(strUrl[0]);
                HttpURLConnection handle = (HttpURLConnection) url.openConnection();
                handle.setRequestMethod("POST");
               // handle.setRequestProperty("Host", Host);
               // handle.setRequestProperty("Content-Type", POSTContentType);
               // handle.setRequestProperty("User-Agent", UserAgent);
                handle.setRequestProperty("Accept", Accept);
               // handle.setRequestProperty("Connection", Connection);
               // handle.setUseCaches(false);
               // handle.setDoOutput(true);
               // handle.setDoInput(true);


                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(handle.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }
                int responseCode = handle.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(handle.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = reader.readLine()) != null) {
                        response.append(inputLine);
                    }

                    return response.toString();
                } else {
                    return "error";
                }


            } catch (Exception e) {
                // System.out.println("exception in jsonparser class ........");
                e.printStackTrace();
                return "error";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            communicationEventListener.handleServerResponse(result);
        }

    }


    public void sendRequest(String url, String request) {
        hrat.execute(url, request);
    }

    public void setCommunicationEventListener(CommunicationEventListener communicationEventListener) {
        this.communicationEventListener = communicationEventListener;
    }

}
