package lab;

import lab.location.LocationGetter;
import lab.location.LocationList;

import javax.swing.*;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CompletableFuture;

public class SearchFrame extends JFrame {
    private JTextField input = new JTextField("Enter location", 5);
    private JButton button = new JButton("search");
    private LocationGetter locationGetter = new LocationGetter();
    public SearchFrame(){
        super("Location getter");
        this.setSize(300, 100);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button.addActionListener(new ButtonEventListener());
        input.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                input.setText("");
            }
        });
        Container container = this.getContentPane();
        container.setLayout(new GridLayout(1 , 2, 0 , 2));
        container.add(input);
        container.add(button);
    }
    class ButtonEventListener implements ActionListener{
        public void actionPerformed (ActionEvent e){
            String requestString;
            requestString = input.getText();
            requestString = requestString.replaceAll(" ", "+");
            CompletableFuture<String> locationString = locationGetter.getLocations(requestString);
            Runnable task = () -> {
                LocationFrame locationFrame = new LocationFrame(locationString);
                locationFrame.setVisible(true);
            };
            SwingUtilities.invokeLater(task);
        }
    }
}
