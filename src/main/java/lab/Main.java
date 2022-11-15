package lab;

import lab.attraction.AttractionsGetter;
import lab.attractionsDescription.AttractionsDescriptionGetter;
import lab.attractionsDescription.WikiInfo;
import lab.location.LocationGetter;
import lab.location.LocationList;
import lab.weather.WeatherGetter;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        JsonAPIParser parser = new JsonAPIParser();
        LocationGetter lg = new LocationGetter(sc.nextLine());
        LocationList locationList = lg.getLocations();
        locationList.getHits().forEach(System.out::println);
        String locationChoose = sc.nextLine();
        System.out.println(locationList.getHits().get(Integer.parseInt(locationChoose) - 1).toString());
        WeatherGetter wg = new WeatherGetter(locationList.getHits().get(Integer.parseInt(locationChoose) - 1));
        CompletableFuture<String> weather = wg.getWeather();
        System.out.println(parser.parseWeather(weather));
        AttractionsGetter ag = new AttractionsGetter(locationList.getHits().get(Integer.parseInt(locationChoose) - 1));
        CompletableFuture<String> attractions = ag.getAttractions();
        AttractionsDescriptionGetter attractionsDescriptionGetter = new AttractionsDescriptionGetter(attractions, parser);
        ArrayList<CompletableFuture<String>> list  = attractionsDescriptionGetter.getAttractions();
        list.stream().map(s -> {
            try {
                return s.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).toList().forEach(System.out::println);

        ArrayList<WikiInfo> infoList = parser.parseDescription(list);
        infoList.forEach(System.out::println);
    }
}