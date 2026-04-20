public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/energy";
    private final ObjectMapper mapper = new ObjectMapper();

    public String fetchCurrent() throws Exception {
        URI uri = URI.create(BASE_URL + "/current");
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String fetchHistorical(String start, String end) throws Exception {
        URI uri = URI.create(BASE_URL + "/historical?start=" + start + "&end=" + end);
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}