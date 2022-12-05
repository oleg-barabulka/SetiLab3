package lab;

import lab.attraction.AttractionsList;
import lab.attractionsDescription.AttractionsDescriptionGetter;
import lab.attractionsDescription.WikiInfo;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AttractionsFrame extends JFrame {

    private JPanel[] panels;
    private JPanel panel = new JPanel();
    public AttractionsFrame(CompletableFuture<String> attractionsString){
        super("Attractions chooser");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        Runnable task = () -> {
            AttractionsList attractionsList = null;
            while (attractionsList == null) {
                attractionsList = JsonAPIParser.parseAttractionsList(attractionsString);
            }
            AttractionsDescriptionGetter attractionsDescriptionGetter = new AttractionsDescriptionGetter();
            List<CompletableFuture<String>> attractionDescriptionsList = attractionsDescriptionGetter.getAttractions(attractionsString);
            JScrollPane scrollPane = new JScrollPane(new DescriptionsPane(attractionsList, attractionDescriptionsList));
            this.add(scrollPane);
            this.revalidate();
        };
        SwingUtilities.invokeLater(task);
    }
    private class DescriptionsPane extends JPanel implements Scrollable {
    public DescriptionsPane(AttractionsList attractionsList, List<CompletableFuture<String>> descriptionsList) {
        final int substringSize = 80;
        setLayout(new GridBagLayout());
        this.setSize(500, 500);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        List<WikiInfo> descriptions = JsonAPIParser.parseDescription(descriptionsList);

        panels = new JPanel[attractionsList.getFeatures().size()];
        int iteration = 0;
        for (JPanel panel: panels){
            panel =  new JPanel();
            panel.setPreferredSize(new Dimension(500 , 400));
            panel.setBorder(new LineBorder(Color.BLUE));

            JLabel attractionLabel = new JLabel();
            attractionLabel.setBorder(new LineBorder(Color.BLACK));

            attractionLabel.setText(attractionsList.getFeatures().get(iteration).getProperties().getName());

            JLabel descriptionLabel = new JLabel();
            descriptionLabel.setBorder(new LineBorder(Color.BLACK));


            panel.setLayout(new GridLayout(2, 1, 1 ,0));
            panel.add(attractionLabel);

            descriptionLabel.setText("<html></html>");
            if (descriptions.get(iteration).getWikipedia_extracts() == null) {
                descriptionLabel.setText("No info");
            }else{
                String str = descriptions.get(iteration).getWikipedia_extracts().getText();
                String[] tokens = str.split("(?<=\\G.{" + substringSize + "})");
                for (String line : tokens) {
                    String currentText = descriptionLabel.getText();
                    String newLine = currentText.substring(0, currentText.length()-7);
                    descriptionLabel.setText(newLine + "<br> " + line + "</html>");
                }
            }
            panel.add(descriptionLabel);
            add(panel, gbc);
            iteration++;
            gbc.gridy++;
        }
        gbc.gridy++;
    }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return null;
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 0;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 0;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return false;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }
}

