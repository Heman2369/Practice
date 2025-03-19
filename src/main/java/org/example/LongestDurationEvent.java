package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class LongestDurationEvent {

    public static void main(String[] args) {

        System.out.println("working");
        String organizer = "empower integrated markets";
        String genre = "Reggae";
        String longestEvent = findLongestDurationEvent(organizer, genre);
        System.out.println("Longest Duration Event: " + longestEvent);
    }

    public static String findLongestDurationEvent(String organizer, String genre) {
        int maxDuration = -1;
        String longestEventName = "";
        int page = 1;
        int totalPages = 1;

        try {
            while (page <= totalPages) {
                URL url = new URL("https://jsonmock.hackerrank.com/api/events?page=" + page);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                conn.disconnect();

                JSONObject response = new JSONObject(content.toString());
                totalPages = response.getInt("total_pages");
                JSONArray data = response.getJSONArray("data");

                for (int i = 0; i < data.length(); i++) {
                    JSONObject event = data.getJSONObject(i);
                    if (event.getString("organized_by").equals(organizer)) {
                        JSONArray genres = event.getJSONArray("genres");
                        boolean containsGenre = false;
                        for (int j = 0; j < genres.length(); j++) {
                            if (genres.getString(j).equals(genre)) {
                                containsGenre = true;
                                break;
                            }
                        }
                        if (containsGenre) {
                            int duration = event.getInt("duration");
                            if (duration > maxDuration) {
                                maxDuration = duration;
                                longestEventName = event.getString("name");
                            }
                        }
                    }
                }

                page++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return longestEventName;
    }
}