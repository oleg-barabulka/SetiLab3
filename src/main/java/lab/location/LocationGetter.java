package lab.location;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class LocationGetter {
    private final String place;
    public LocationGetter(String place){
        this.place = place;
    }
    public LocationList getLocations(){
        CompletableFuture<String> str = makeRequest();
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        LocationList locationList;
        System.out.println("bebraa");
        try {
            return locationList = mapper.readValue(str.get(),  LocationList.class);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    CompletableFuture<String> makeRequest(){
        HttpClient httpClient = HttpClient.newHttpClient();

        String encodedQuery = place + "&locale=en";

        final String GEOCODING_RESOURCE = "https://graphhopper.com/api/1/geocode";
        final String API_KEY = "c54d1bcc-30d8-44cc-950a-60a073250f63";
        String requestURI = GEOCODING_RESOURCE + "?q=" + encodedQuery + "&key=" + API_KEY;

        HttpRequest geocodingRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(requestURI))
                .build();
        return httpClient.sendAsync(geocodingRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }
}
