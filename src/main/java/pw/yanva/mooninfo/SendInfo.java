package pw.yanva.mooninfo;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SendInfo {
    String URL;

    public SendInfo(String url) {
        this.URL = url;
    }

    public void sendMoonPhase(int phase, long worldTime) {
        CompletableFuture.runAsync(() -> {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("phase", phase);
            jsonObject.addProperty("time", worldTime);
            postRequest(this.URL, "Content-Type", "application/json", jsonObject);
        });
    }

    private static JsonObject postRequest(String url, String headerName, String headerValue, JsonObject jsonBody) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .uri(URI.create(url))
                .setHeader(headerName, headerValue)
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        List<String> responseList = new ArrayList<>();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseList::add)
                .join();
        JsonElement root = JsonParser.parseString(String.join("", responseList));
        return root.getAsJsonObject();
    }
}
