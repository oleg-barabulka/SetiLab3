package lab;

import lab.attraction.AttractionsGetter;
import lab.location.Location;
import lab.location.LocationList;
import lab.weather.Temperature;
import lab.weather.Weather;
import lab.weather.WeatherGetter;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LocationFrame extends JFrame {
    private AttractionsGetter attractionsGetter = new AttractionsGetter();
    private JPanel[] panels;
    private final JPanel panel = new JPanel();
    public LocationFrame(CompletableFuture<String> locationString){
        super("Location chooser");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        LocationList locationList = JsonAPIParser.getLocations(locationString);
        WeatherGetter weatherGetter = new WeatherGetter();
        List<CompletableFuture<String>> weatherFutureList = locationList.getHits().stream()
                .map(weatherGetter::getWeather)
                .toList();
        Runnable task = () -> {
            JScrollPane scrollPane = new JScrollPane(new LocationsPane(locationList, weatherFutureList));
            this.setSize(500, 300);
            this.add(scrollPane);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        };
        SwingUtilities.invokeLater(task);
    }
    private class LocationsPane extends JPanel implements Scrollable {
        public LocationsPane(LocationList locationList, List<CompletableFuture<String>> weatherList) {

            setLayout(new GridBagLayout());
            this.setSize(300, 300);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;

            panels = new JPanel[locationList.getHits().size()];
            int iteration = 0;
            for (JPanel panel: panels){

                panel = new JPanel();
                panel.setPreferredSize(new Dimension(300, 110));
                panel.setBorder(new LineBorder(Color.BLUE));

                LocationLabel placeLabel = new LocationLabel(locationList.getHits().get(iteration));
                placeLabel.setBorder(new LineBorder(Color.BLACK));
                placeLabel.setPreferredSize(new Dimension(300, 33));
                placeLabel.setText(locationList.getHits().get(iteration).getName() + " Country: " + locationList.getHits().get(iteration).getCountry());
                placeLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        CompletableFuture<String> attractionsString = attractionsGetter.getAttractions(placeLabel.getMyLocation());
                        Runnable task = () -> {
                            AttractionsFrame locationFrame = new AttractionsFrame(attractionsString);
                            locationFrame.setVisible(true);
                        };
                        SwingUtilities.invokeLater(task);
                    }
                });

                JLabel coordsLabel = new JLabel();
                coordsLabel.setBorder(new LineBorder(Color.BLACK));
                coordsLabel.setPreferredSize(new Dimension(300, 33));

                JLabel weatherLabel = new JLabel();
                coordsLabel.setBorder(new LineBorder(Color.BLACK));
                coordsLabel.setPreferredSize(new Dimension(300, 33));


                coordsLabel.setText("Lat = " + locationList.getHits().get(iteration).getPoint().getLat() + " Lng = " + locationList.getHits().get(iteration).getPoint().getLng());


                Temperature weather = JsonAPIParser.parseWeather(weatherList.get(iteration)).getMain();
                weatherLabel.setText( "temp = " + weather.getTemp() +" feels like = "+ weather.getFeels_like());

                panel.setLayout(new GridLayout(3, 1, 1 ,0));
                panel.add(placeLabel);
                panel.add(coordsLabel);
                panel.add(weatherLabel);
                add(panel, gbc);

                iteration++;
                gbc.gridy++;
            }
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(100, 50);
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 32;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 32;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return getPreferredSize().width <= getWidth();
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

    }

    private class LocationLabel extends JLabel{
        private Location location;
        public LocationLabel(Location location){
            this.location = location;
        }

        public Location getMyLocation() {
            return location;
        }
    }
}
