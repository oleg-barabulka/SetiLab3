package lab;

import lab.attraction.AttractionsList;
import lab.attractionsDescription.WikiInfo;
import lab.location.LocationList;
import lab.weather.Weather;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class JsonAPIParser {
    public static LocationList getLocations(CompletableFuture<String> jsonString){
        CompletableFuture<String> str = jsonString;
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return mapper.readValue(str.get(),  LocationList.class);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static AttractionsList parseAttractionsList(CompletableFuture<String> jsonString){
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(jsonString.get(),  AttractionsList.class);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static Weather parseWeather(CompletableFuture<String> jsonString){
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(jsonString.get(),  Weather.class);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static ArrayList<WikiInfo> parseDescription(List<CompletableFuture<String>> jsonList){
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ArrayList<WikiInfo> infoArrayList = new ArrayList<>();
        for (CompletableFuture<String> jsonString: jsonList){
            try {
                infoArrayList.add(mapper.readValue(jsonString.get(), WikiInfo.class));
            } catch (JsonProcessingException | ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return infoArrayList;
    }
}
