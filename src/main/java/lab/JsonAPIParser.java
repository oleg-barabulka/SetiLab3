package lab;

import lab.attraction.AttractionsList;
import lab.attractionsDescription.WikiInfo;
import lab.weather.Weather;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class JsonAPIParser {
    public AttractionsList parseAttractionsList(CompletableFuture<String> jsonString){
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        AttractionsList attractionsList;
        try {
            return attractionsList = mapper.readValue(jsonString.get(),  AttractionsList.class);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public Weather parseWeather(CompletableFuture<String> jsonString){
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Weather weather;
        try {
            return weather = mapper.readValue(jsonString.get(),  Weather.class);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public ArrayList<WikiInfo> parseDescription(ArrayList<CompletableFuture<String>> jsonList){
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
