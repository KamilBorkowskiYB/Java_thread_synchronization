package org.example.java_multithreading_project;

import java.util.Random;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
class Pasenger implements Runnable {
    private final Ship_Monitor monitor;
    private final int id,on_bridge,on_board,arrival;
    private final Random random = new Random();
    private final Pane pane;
    private Circle circle;

    public Pasenger(Ship_Monitor monitor, int id,int on_bridge,int on_board,int arr_t, Pane pane) {
        this.monitor = monitor;
        this.id = id;
        this.on_bridge = on_bridge;
        this.on_board = on_board;
        this.arrival = arr_t;
        this.pane = pane;
    }

    @Override
    public void run() {
        Platform.runLater(() -> {//create passenger and move him to port
            circle = new Circle(10, Color.RED);
            double randomX = random.nextDouble() * pane.getWidth();
            circle.setLayoutX(randomX);
            circle.setLayoutY(pane.getHeight() + 20);
            pane.getChildren().add(circle);
            double targetY = (87 * pane.getHeight() / 100) + random.nextInt(61) - 30;
            double targetX = ( pane.getWidth() / 2) + random.nextInt(401) - 200;
            TranslateTransition transition = new TranslateTransition(Duration.seconds(arrival), circle);
            transition.setToY(targetY - circle.getLayoutY());
            transition.setToX(targetX - circle.getLayoutX());
            transition.play();
        });

        try {
            Thread.sleep(arrival*1000);
            monitor.entry_bridge(id);//move passenger from port to bridge
            Platform.runLater(() -> {
                TranslateTransition transition = new TranslateTransition(Duration.seconds(on_bridge), circle);
                double targetY = (25 * pane.getHeight() / 36);
                double targetX = ( pane.getWidth() / 2) + random.nextInt(201) - 100;
                transition.setToY(targetY - circle.getLayoutY());
                transition.setToX(targetX - circle.getLayoutX());
                transition.play();
            });
            Thread.sleep(on_bridge*1000);

            monitor.entry_ship(id);//move passenger from bridge to ship
            Platform.runLater(() -> {
                TranslateTransition transition = new TranslateTransition(Duration.seconds(on_board), circle);
                double targetY = (1 * pane.getHeight() / 2)+ random.nextInt(121) - 60;
                double targetX = (1 * pane.getWidth() / 2)+ random.nextInt(301) - 150;
                transition.setToY(targetY - circle.getLayoutY());
                transition.setToX(targetX - circle.getLayoutX());
                transition.play();
            });
            Thread.sleep(on_board*1000);

            monitor.leave_ship(id);//move passenger from ship to bridge
            Platform.runLater(() -> {
                TranslateTransition transition = new TranslateTransition(Duration.seconds(on_board), circle);
                double targetY = (25 * pane.getHeight() / 36);
                double targetX = ( pane.getWidth() / 2) + random.nextInt(201) - 100;
                transition.setToY(targetY - circle.getLayoutY());
                transition.setToX(targetX - circle.getLayoutX());
                transition.play();
            });
            Thread.sleep(on_board*1000);

            monitor.leave_bridge(id);//move passenger from pane
            Platform.runLater(() -> {
                TranslateTransition transition = new TranslateTransition(Duration.seconds(on_bridge), circle);
                double targetY = (pane.getHeight() + 36);
                transition.setToY(targetY - circle.getLayoutY());
                transition.play();
            });
            Thread.sleep(on_bridge*1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}