package lab.attraction;

import lab.location.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Data
public class AttractionsGetter {
    private Location location;
    public CompletableFuture<String> getAttractions(){
        HttpClient client = HttpClient.newHttpClient();
        final String API_KEY = "5ae2e3f221c38a28845f05b649f966a6e6af076412fbb189dcf2a972";
        final String ATTRACTIONS_DATA_RESOURCE = "https://api.opentripmap.com/0.1/en/places/radius";
        final String RADIUS = "100";
        String requestURI = ATTRACTIONS_DATA_RESOURCE + "?radius=" + RADIUS + "&lon=" + this.location.getPoint().getLng()+ "&lat="+ this.location.getPoint().getLat()+"&apikey=" + API_KEY;
        HttpRequest geocodingRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestURI))
                .build();
        return client.sendAsync(geocodingRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }
}
