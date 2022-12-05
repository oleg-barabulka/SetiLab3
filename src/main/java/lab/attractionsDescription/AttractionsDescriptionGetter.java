package lab.attractionsDescription;


import lab.JsonAPIParser;
import lab.attraction.Attraction;
import lab.attraction.AttractionsList;
import lombok.Data;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;


@Data
public class AttractionsDescriptionGetter {

    private JsonAPIParser parser;


    public ArrayList<CompletableFuture<String>> getAttractions(CompletableFuture<String> attractionsListJson){
        AttractionsList attractionsList = JsonAPIParser.parseAttractionsList(attractionsListJson);
        HttpClient client = HttpClient.newHttpClient();

        final String API_KEY = "5ae2e3f221c38a28845f05b649f966a6e6af076412fbb189dcf2a972";
        final String ATTRACTIONS_DATA_RESOURCE = "https://api.opentripmap.com/0.1/en/places/xid/";
        ArrayList<CompletableFuture<String>> descriptions = new ArrayList<>();
        for (Attraction attraction:attractionsList.getFeatures()){
            String requestURI = ATTRACTIONS_DATA_RESOURCE + attraction.getProperties().getXid() + "?format=geojson"+"&apikey=" + API_KEY;
            HttpRequest geocodingRequest = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(requestURI))
                    .build();
            descriptions.add(client.sendAsync(geocodingRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body));
        }
        return descriptions;
    }
}
