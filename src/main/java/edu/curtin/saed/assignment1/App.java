package edu.curtin.saed.assignment1;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;


import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;;

/**
 * This is demonstration code intended for you to modify. Currently, it sets up
 * a rudimentary
 * Swing GUI with the basic elements required for the assignment.
 *
 * (There is an equivalent JavaFX version of this, if you'd prefer.)
 *
 * You will need to use the GridArea object, and create various GridAreaIcon
 * objects, to represent
 * the on-screen map.
 *
 * Use the startBtn, endBtn, statusText and textArea objects for the other
 * input/output required by
 * the assignment specification.
 *
 * Break this up into multiple methods and/or classes if it seems appropriate.
 * Promote some of the
 * local variables to fields if needed.
 */
public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }); // Equivalent to JavaFX's Platform.runLater().
    }

    public static void start() throws IOException {
        var window = new JFrame("Air Traffic Simulator");

        // Set up the main "top-down" display area. This is an example only, and you
        // should
        // change this to set it up as you require.

        GridArea area = new GridArea(10, 10);
        // area.setGridLines(false); // If desired
        area.setBackground(new Color(0, 0x60, 0));

        // caption

        GridAreaIcon plane1Icon = new GridAreaIcon(
                5, 3, 45.0, 1.0,
                App.class.getClassLoader().getResource("plane.png"),
                "Plane 1");
        Plane plane1 = new Plane( "Plane 1", plane1Icon, 0, 0);
        area.getIcons().add(plane1Icon);

        setUpAirports(area, 10, 10);
        // Set up other key parts of the user interface. You'll also want to adjust
        // this.

        var startBtn = new JButton("Start");
        var endBtn = new JButton("End");
        var statusText = new JLabel("Label Text");
        var textArea = new JTextArea();
        textArea.append("Sidebar\n");
        textArea.append("Text\n");

        BlockingQueue<FlightRequest> flightQueue = new LinkedBlockingQueue<>();
        FlightHandler flightHandler = new FlightHandler(4);
        
        startBtn.addActionListener((event) -> {
            System.out.println("Start button pressed");
            textArea.append("Hello\n");
            

            for (int i = 0; i < 10; i++) {
                FlightRequestGenerator generator = new FlightRequestGenerator(10, i + 1, flightQueue);
                new Thread(generator).start();
            }

            while (true) {
                try {
                    FlightRequest request = flightQueue.take(); // Blocking call
                    flightHandler.addFlightRequest(request);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        endBtn.addActionListener((event) -> {
            System.out.println("End button pressed");
            flightHandler.shutdown();
        });

        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.out.println("Window closed");
            }
        });

        // Below is basically just the GUI "plumbing" (connecting things together).

        var toolbar = new JToolBar();
        toolbar.add(startBtn);
        toolbar.add(endBtn);
        toolbar.addSeparator();
        toolbar.add(statusText);

        var scrollingTextArea = new JScrollPane(textArea);
        scrollingTextArea.setBorder(BorderFactory.createEtchedBorder());

        var splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, area, scrollingTextArea);

        Container contentPane = window.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(toolbar, BorderLayout.NORTH);
        contentPane.add(splitPane, BorderLayout.CENTER);

        window.setPreferredSize(new Dimension(1200, 1000));
        window.pack();
        splitPane.setDividerLocation(0.75);
        window.setVisible(true);
    }

    

    public static void setUpAirports(GridArea area, int height, int width) {
        Set<String> points = new HashSet<>();
        Random rand = new Random();

        while (points.size() < 10) {
            int x = rand.nextInt(height);
            int y = rand.nextInt(width);

            String newPoint = x + "," + y;

            boolean tooClose = points.stream().anyMatch(point -> {
                String[] coords = point.split(",");
                int existingX = Integer.parseInt(coords[0]);
                int existingY = Integer.parseInt(coords[1]);
                return Math.abs(existingX - x) <= 1 && Math.abs(existingY - y) <= 1;
            });

            if (!tooClose) {
                points.add(newPoint);
            }
        }

        System.out.println("Airports created");

        List<AirPort> airPorts = new ArrayList<>();
        int i = 1;
        for (String point : points) {
            String[] coords = point.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);

            GridAreaIcon tempIcon = new GridAreaIcon(
                    x, // x
                    y, // y
                    0.0, // degrees rotation (clockwise)
                    1.0, // scale
                    App.class.getClassLoader().getResource("airport.png"), // URL for image resource
                    "AirPort " + i);

            // Place airport on grid
            area.getIcons().add(tempIcon);

            // Create airport instance
            AirPort temp = new AirPort("AirPort " + i, tempIcon,x, y);

            // Add to collection
            airPorts.add(temp);

            i++;
        }
    }

}