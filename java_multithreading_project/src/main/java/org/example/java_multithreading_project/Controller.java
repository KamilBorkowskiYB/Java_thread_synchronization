package org.example.java_multithreading_project;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.concurrent.Task;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Controller {

    @FXML
    private Button startSimBTN;
    @FXML
    private TextField ship_size;
    @FXML
    private TextField bridge_size;
    @FXML
    private TextField arrival_t;
    @FXML
    private TextField enter_bridge_t;
    @FXML
    private TextField enter_ship_t;
    @FXML
    private TextField sail_t;
    @FXML
    private TextField dock_t;
    @FXML
    private Pane MainPane;
    @FXML
    private Rectangle bridge_rec;
    int ss,bs,at,ebt,est,st,dt;
    int i=0;

    @FXML
    protected void StartSim() {
        startSimBTN.setText("STARTING!");

        try{
            ss = Integer.parseInt(ship_size.getText());
            bs = Integer.parseInt(bridge_size.getText());
            at = Integer.parseInt(arrival_t.getText());
            ebt = Integer.parseInt(enter_bridge_t.getText());
            est = Integer.parseInt(enter_ship_t.getText());
            st = Integer.parseInt(sail_t.getText());
            dt = Integer.parseInt(dock_t.getText());

            if(bs>=ss){
                startSimBTN.setText("Bridge size msut be lower than ship size");
            }
            else{
                startSimBTN.setDisable(true);
                ship_size.setDisable(true);
                bridge_size.setDisable(true);
                arrival_t.setDisable(true);
                enter_bridge_t.setDisable(true);
                enter_ship_t.setDisable(true);
                sail_t.setDisable(true);
                dock_t.setDisable(true);

                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Ship_Monitor monitor = new Ship_Monitor(ss, bs, st);
                        new Thread(new Captain(monitor, 1, dt,bridge_rec)).start();
                        while (true) {
                            try {
                                // new passenger appears after some time
                                new Thread(new Pasenger(monitor, i + 1, ebt, est, at, MainPane)).start();
                                System.out.println("Passenger nr: " + (i + 1) + " has arrived");
                                i++;
                                Thread.sleep(at*1000);
                            } catch (InterruptedException e) {
                                System.out.printf("INTERRUPTED");
                                break;
                            }
                        }
                        return null;
                    }
                };
                new Thread(task).start();
            }



        }catch (NumberFormatException e){
            System.out.println(e);
            startSimBTN.setText("ENTER INTEGERS!");
        }
    }
}