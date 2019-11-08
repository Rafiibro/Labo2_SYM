package com.example.labo2;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.json.JSONObject;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class SymComManagerCompresse {

    private static final String TAG = SymComManagerCompresse.class.getSimpleName();

    private List<CommunicationEventListener> theListeners= new LinkedList<>();
    private List<List<String>> requests= new LinkedList<>();
    private boolean status = true;

    private class HttpRequestAsyncTask extends AsyncTask<String, Void, String> {


        private String POSTContentType = "text/plain";
        private String Host = "";
        private String urlParameters = "";
        private String UserAgent = "";
        private String Accept = "text/*";
        private String Accept_encoding = "deflate";
        private String Connection = "";
        private String postData;
        private String X_Network = "CSD";
        private String X_Content_Encoding = "deflate";

        @Override
        protected String doInBackground(String... strUrl) {

            if(strUrl[2].equals("xml")){
                POSTContentType = "application/xml";
            } else if(strUrl[2].equals("json")){
                POSTContentType = "application/json";
            }

            postData = strUrl[1];

            try {

                URL url = new URL(strUrl[0]);
                while(!isConnectedToServer(strUrl[0], 1000)){
                    status = false;
                }
                status = true;
                HttpURLConnection handle = (HttpURLConnection) url.openConnection();
                handle.setRequestMethod("POST");
                handle.setRequestProperty("Accept", Accept);
                handle.setRequestProperty("Content-Type", POSTContentType);
                handle.setRequestProperty("X-Network", X_Network);
                handle.setRequestProperty("X-Content-Encoding", X_Content_Encoding);

                if (this.postData != null) {

                    DeflaterOutputStream writer = new DeflaterOutputStream(handle.getOutputStream(),  new Deflater(Deflater.BEST_COMPRESSION,true));
                    writer.write(postData.getBytes());
                    writer.finish();
                    writer.flush();
                }
                int responseCode = handle.getResponseCode();
                String x_content_encoding = handle.getHeaderField("X-Content-Encoding");

                if (responseCode == HttpURLConnection.HTTP_OK && x_content_encoding.equals("deflate")) {
                    InflaterInputStream reader = new InflaterInputStream(handle.getInputStream());
                    StringBuffer response = new StringBuffer();
                    int count;
                    byte[] buffer = new byte[1024];
                    count = reader.read(buffer);
                    while (count != -1) {
                        response.append(buffer.toString());
                        count = reader.read(buffer);
                    }

                    handle.disconnect();

                    return response.toString();
                } else {
                    handle.disconnect();
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
            if(requests.size() > 0) {
                requests.remove(0);
            }
            for(CommunicationEventListener cel: theListeners) {
                if(cel.handleServerResponse(result)) break;
            }
            if(requests.size() > 0){
                HttpRequestAsyncTask connectedTask = new HttpRequestAsyncTask();
                connectedTask.execute(requests.get(0).get(0), requests.get(0).get(1), requests.get(0).get(2));
            }
        }

    }


    public void sendRequest(String url, String request, String format) {

        if(status){
            HttpRequestAsyncTask hrat = new HttpRequestAsyncTask();
            hrat.execute(url,request, format);
        } else {
            List<String> req = new LinkedList<String>();
            req.add(url);
            req.add(request);
            req.add(format);
            requests.add(new LinkedList<String>(req));
        }
    }

    public void setCommunicationEventListener(CommunicationEventListener listener) {
        if(!theListeners.contains(listener))
            theListeners.add(listener);
    }

    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            // Handle your exceptions
            return false;
        }
    }

}