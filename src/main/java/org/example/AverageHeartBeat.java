import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class AverageHeartBeat {

    private static final Gson gson = new Gson();

    public static int averageHeartBeat(String marathon, String sex) {
        try {
            String baseUrl = "https://jsonmock.hackerrank.com/api/marathon?sex=" + sex;
            int page = 1, totalPages = 1, totalHeartRate = 0, count = 0;
            while (page <= totalPages) {
                // Fetch paginated API response
                String urlString = baseUrl + "&page=" + page;
                String jsonResponse = getApiResponse(urlString);

                // Parse JSON response using Gson
                //dummy
                JsonObject jsonObject = gson.fromJson(jsonResponse,
                        JsonObject.class);
                totalPages = jsonObject.get("total_pages").getAsInt();
                JsonArray data = jsonObject.getAsJsonArray("data");

                // Filter and calculate average heartbeat
                for (int i = 0; i < data.size(); i++) {
                    JsonObject runner = data.get(i).getAsJsonObject();
                    if (runner.get("marathon_name").getAsString().equalsIgnoreCase(marathon)) {
                       // if(runner.get("avgheartbeat")!=null) {
                            totalHeartRate += runner.get("avgheartbeat").getAsInt();

                        //}
                        count++;
                    }
                }
                page++;
            }
            // Compute and return the average rounded down
            return (count == 0) ? 0 : (int) Math.floor((double) totalHeartRate / count);

        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Indicating an error
        }
    }

    private static String getApiResponse(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();
        return response.toString();
    }

    public static void main(String[] args) {
        // Example Input
        String marathon = "Cityscape Marathon";
        String sex = "female";

        // Function call
        int result = averageHeartBeat(marathon, sex);
        System.out.println(result);
    }
}