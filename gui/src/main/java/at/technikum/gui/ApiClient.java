package at.technikum.gui;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/energy";

    //zum testen auf true wegen rest api
    private static final boolean USE_MOCK = true;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String fetchCurrent() throws Exception {
        if (USE_MOCK) {
            return """
                    {
                      "hour": "2025-01-10T14:00:00",
                      "communityDepleted": 78.54,
                      "gridPortion": 7.23
                    }
                    """;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/current"))
                .GET()
                .build();
        HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String fetchHistorical(String start, String end) throws Exception {
        if (USE_MOCK) {
            return """
                    [
                      {
                        "hour": "2025-01-10T14:00:00",
                        "communityProduced": 143.024,
                        "communityUsed": 130.101,
                        "gridUsed": 14.75
                      },
                      {
                        "hour": "2025-01-10T13:00:00",
                        "communityProduced": 120.5,
                        "communityUsed": 110.3,
                        "gridUsed": 12.1
                      }
                    ]
                    """;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/historical?start=" + start + "&end=" + end))
                .GET()
                .build();
        HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}