package at.technikum.producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherClient {

    // Standort: Wien
    private static final double LATITUDE  = 48.21;
    private static final double LONGITUDE = 16.37;

    private static final String URL =
            "https://api.open-meteo.com/v1/forecast"
                    + "?latitude=" + LATITUDE
                    + "&longitude=" + LONGITUDE
                    + "&current=shortwave_radiation";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper   = new ObjectMapper();

    /**
     * Holt die aktuelle Sonneneinstrahlung in W/m².
     * 0 bedeutet Nacht, ~1000 bedeutet starke Mittagssonne.
     * Bei Fehlern wird 0 zurückgegeben.
     */
    public double getCurrentRadiation() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());
            return root.get("current").get("shortwave_radiation").asDouble();

        } catch (Exception e) {
            System.out.println("Weather API Fehler: " + e.getMessage());
            return 0.0;
        }
    }
}