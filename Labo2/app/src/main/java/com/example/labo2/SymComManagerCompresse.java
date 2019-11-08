package com.example.labo2;

import android.os.AsyncTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class SymComManagerCompresse {

    /* Récupération du nom de la classe*/
    private static final String TAG = SymComManagerCompresse.class.getSimpleName();

    private List<CommunicationEventListener> theListeners= new LinkedList<>();
    private List<List<String>> requests= new LinkedList<>();
    private boolean status = true;

    private class HttpRequestAsyncTask extends AsyncTask<String, Void, String> {

        /* Valeurs a initialiser dans le header */
        private String POSTContentType = "text/plain";
        private String postData;
        private String X_Network = "CSD";
        private String X_Content_Encoding = "deflate";

        @Override
        protected String doInBackground(String... strUrl) {

            postData = strUrl[1];

            try {
                URL url = new URL(strUrl[0]);

                /* Attente de connection sur le server */
                while(!isConnectedToServer(strUrl[0], 1000)){
                    status = false;
                }
                status = true;

                /* Ouverture de la connection */
                HttpURLConnection handle = (HttpURLConnection) url.openConnection();

                /* Configuration du header */
                handle.setRequestMethod("POST");
                handle.setRequestProperty("Content-Type", POSTContentType);
                handle.setRequestProperty("X-Network", X_Network);
                handle.setRequestProperty("X-Content-Encoding", X_Content_Encoding);

                /* Compression de la requete et envoi */
                if (this.postData != null) {
                    DeflaterOutputStream writer = new DeflaterOutputStream(handle.getOutputStream(),  new Deflater(Deflater.DEFAULT_COMPRESSION,true));
                    writer.write(postData.getBytes(), 0, postData.getBytes().length);
                    writer.finish();
                    writer.flush();
                }
                /* Récupération de la donnée serveur */
                int responseCode = handle.getResponseCode();
                String x_content_encoding = handle.getHeaderField("X-Content-Encoding");

                /* Test du code d'erreur en réponse du serveur et de la compression */
                if (responseCode == HttpURLConnection.HTTP_OK
                        && x_content_encoding.equals("deflate")) {

                    /* Décompression des datas */
                    InflaterInputStream reader = new InflaterInputStream(handle.getInputStream(), new Inflater(true));
                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];

                    try{
                        /* Lecture des datas */
                        int value;
                        while((value = reader.read(buffer)) != -1){
                            result.write(buffer,0, value);
                        }
                        /* Retourne la réponse en string */
                        return result.toString("UTF-8");
                    }catch(IOException e){
                        e.printStackTrace();
                        return "error";
                    } finally {
                        /* Ferme le stream */
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return "error";
                        }
                    }
                } else {
                    /* Déconnection du serveur */
                    handle.disconnect();
                    return "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error";
            }

        }

        @Override
        /* Cette fonction permet que lorsqu'une requêtes est finie, elle crée un nouveau
            thread avec la requête suivante à éxecuter */
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

    /* Cette fonction permet de gérer les requêtes lorsque la connection n'a pas lieu;
    *   - Si la connection est établie : Lance un nouveau thread avec la requête
    *   - Si la connection n'est pas établie : Ajoute la requête dans la list de requêtes à éxecuter */
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

    /* Ajout le Listener au tableau s'il n'est pas encore dedans */
    public void setCommunicationEventListener(CommunicationEventListener listener) {
        if(!theListeners.contains(listener))
            theListeners.add(listener);
    }

    /* Effectue le test de connection sur le serveur */
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
