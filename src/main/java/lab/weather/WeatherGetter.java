package lab.weather;

import lab.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Data
@AllArgsConstructor
public class WeatherGetter {
    private Location location;
    public CompletableFuture<String> getWeather(){
        HttpClient client = HttpClient.newHttpClient();
        final String API_KEY = "509d4e381181c5059d9df42fda934c97";
        final String WEATHER_DATA_RESOURCE = "http://api.openweathermap.org/data/2.5/weather";
        String requestURI = WEATHER_DATA_RESOURCE + "?lat=" + this.location.getPoint().getLat() + "&lon=" + this.location.getPoint().getLng()+ "&appid=" + API_KEY;
        HttpRequest geocodingRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestURI))
                .build();
        return client.sendAsync(geocodingRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }
}
