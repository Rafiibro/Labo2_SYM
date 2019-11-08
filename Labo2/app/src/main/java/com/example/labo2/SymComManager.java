/**
 * Auteurs: Bacso, Vuagniaux, Da Cunha
 */
package com.example.labo2;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

public class SymComManager {

    /* Récupération du nom de la classe*/
    private static final String TAG = SymComManager.class.getSimpleName();

    private List<CommunicationEventListener> theListeners= new LinkedList<>();
    private List<List<String>> requests= new LinkedList<>();
    private boolean status = true;

    private class HttpRequestAsyncTask extends AsyncTask<String, Void, String> {

        /* Valeurs a initialiser dans le header */
        private String POSTContentType = "text/plain";
        private String Accept = "text/*";
        private String postData;

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

                /* Attente de connection sur le server */
                while(!isConnectedToServer(strUrl[0], 1000)){
                    status = false;
                }
                status = true;

                /* Ouverture de la connection */
                HttpURLConnection handle = (HttpURLConnection) url.openConnection();

                /* Configuration du header */
                handle.setRequestMethod("POST");
                handle.setRequestProperty("Accept", Accept);
                handle.setRequestProperty("Content-Type", POSTContentType);

                /* Envoie de la requête */
                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(handle.getOutputStream());
                    writer.write(postData);
                    writer.flush();
                }

                /* Récupération de la donnée serveur */
                int responseCode = handle.getResponseCode();

                /* Test du code d'erreur en réponse du serveur */
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    /* Lecture des datas */
                    BufferedReader reader = new BufferedReader(new InputStreamReader(handle.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = reader.readLine()) != null) {
                        response.append(inputLine);
                    }

                    /* Déconnection du serveur */
                    handle.disconnect();

                    /* Retourne la réponse en string */
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

        /* Cette fonction permet que lorsqu'une requêtes est finie, elle crée un nouveau
            thread avec la requête suivante à éxecuter */
        @Override
        protected void onPostExecute(String result) {
            if(requests.size() > 0) {
                requests.remove(0);
            }
            for(CommunicationEventListener cel: theListeners) {
                if(cel.handleServerResponse(result)){
                    break;
                }
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
