package com.example.labo2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe recuperant les donnees de la base de donnees
 */
public class GraphQLApi {
    /**
     * Retourne la liste des auteurs dans la base de donnees
     */
    public static List<Autors> allAutors(){
        List<Autors> result = new ArrayList<Autors>();
        JSONObject jsonData;
        Map<String, String> mapData = new HashMap<>();
        mapData.put("query","{allAuthors{id first_name last_name}}");
        jsonData = new JSONObject(mapData);
        String response = sendRequest("http://sym.iict.ch/api/graphql", jsonData.toString());
        try {
            JSONObject reader = new JSONObject(response);
            JSONObject data  = reader.getJSONObject("data");
            JSONArray authors  = data.getJSONArray("allAuthors");
            for(int i = 0; i < authors.length(); i++){
                JSONObject author = authors.getJSONObject(i);
                Autors aut = new Autors(author.getString("first_name"),author.getString("last_name"),author.getInt("id"));
                result.add(aut);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retourne la liste des posts d'un auteur
     */
    public static List<Post> allPostByAutor(int autorId){
        List<Post> result = new ArrayList<Post>();
        JSONObject jsonData;
        Map<String, String> mapData = new HashMap<>();
        mapData.put("query","{allPostByAuthor(authorId:" + autorId + "){id title description content date}}");
        jsonData = new JSONObject(mapData);
        String response = sendRequest("http://sym.iict.ch/api/graphql", jsonData.toString());
        try {
            JSONObject reader = new JSONObject(response);
            JSONObject data  = reader.getJSONObject("data");
            JSONArray posts  = data.getJSONArray("allPostByAuthor");
            for(int i = 0; i < posts.length(); i++){
                JSONObject post = posts.getJSONObject(i);
                Post po = new Post(post.getInt("id"),post.getString("title"),post.getString("description"),post.getString("content"),post.getString("date"));
                result.add(po);
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Envois de la requete graphql
     */
    private static String sendRequest(String urlStr, String data){

        String POSTContentType = "application/json";
        String Accept = "text/*";

        String postData = data;

        try {

            URL url = new URL(urlStr);
            HttpURLConnection handle = (HttpURLConnection) url.openConnection();
            handle.setRequestMethod("POST");
            handle.setRequestProperty("Accept", Accept);
            handle.setRequestProperty("Content-Type", POSTContentType);


            if (postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(handle.getOutputStream());
                writer.write(postData);
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
}
