package org.example.java_multithreading_project;


import javafx.application.Platform;
import javafx.scene.shape.Rectangle;


import java.util.Random;

class Captain implements Runnable {
    private final Ship_Monitor monitor;
    private final int id, in_port;
    private final Random random = new Random();

    private Rectangle bridge;

    public Captain(Ship_Monitor monitor, int id, int in_port,Rectangle br) {
        this.monitor = monitor;
        this.id = id;
        this.in_port = in_port;
        this.bridge = br;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(in_port*1000);
                monitor.sail(bridge);
                monitor.open_gate();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}