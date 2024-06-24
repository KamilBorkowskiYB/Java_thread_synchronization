package org.example.java_multithreading_project;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
class Ship_Monitor {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition bridgeCondition = lock.newCondition();
    private final Condition captainCondition = lock.newCondition();
    private int passengers_on_board =0;
    private int passengers_on_bridge = 0;
    private int ship_size, bridge_size, in_sail;

    private int gate_in = 1, gate_out = 0;
    public Ship_Monitor(int N, int K,int sail) {
        this.bridge_size = K;
        this.ship_size = N;
        this.in_sail = sail;
    }
    public void entry_ship(int id) throws InterruptedException {
        lock.lock();
        try {
            passengers_on_bridge--;
            passengers_on_board++;
            System.out.println("Passenger nr: " + id + " has boarded the ship");
            captainCondition.signal();
            bridgeCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void entry_bridge(int id) throws InterruptedException {
        lock.lock();
        try {
            while (passengers_on_bridge >= bridge_size || (passengers_on_board+passengers_on_bridge) >= ship_size || gate_in==0) {//sprawdza czy jest miejsce na mostku i pokladzie
                bridgeCondition.await();
            }
            passengers_on_bridge++;
            System.out.println("Passenger nr: " + id + " is on the bridge");
        } finally {
            lock.unlock();
        }
    }
    public void leave_ship(int id) throws InterruptedException {
        lock.lock();
        try {
            while (passengers_on_bridge >= bridge_size || gate_out==0) {//sprawdza czy jest miejsce na mostku
                bridgeCondition.await();
            }
            passengers_on_bridge++;
            passengers_on_board--;
            System.out.println("Passenger nr: " + id + " is on the bridge(left ship)");
        } finally {
            lock.unlock();
        }
    }

    public void leave_bridge(int id) throws InterruptedException {
        lock.lock();
        try {
            passengers_on_bridge--;
            System.out.println("Passenger nr: " + id + " is in the port)");
            captainCondition.signal();
            bridgeCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void sail(Rectangle bridge) throws InterruptedException {
        lock.lock();
        try {
            System.out.println("Captain wants to sail the ship.");
            gate_in = 0;
            while (passengers_on_bridge > 0) {
                captainCondition.await();
            }
            if(passengers_on_board==0){
                System.out.println("Ship is empty no sailing");
            }
            else{
                System.out.println("Sailing with " + passengers_on_board + " passengers on board." + " [PsgOnBridge: "+passengers_on_bridge+"]");
                Thread.sleep(900);
                Platform.runLater(() -> bridge.setVisible(false));
                Thread.sleep(in_sail*1000);
                System.out.println("Ship came back with " + passengers_on_board + " passengers on board.");
                Platform.runLater(() -> bridge.setVisible(true));
            }
            gate_out = 1;
            bridgeCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }
    public void open_gate() throws InterruptedException {
        lock.lock();
        try {
            while (passengers_on_bridge > 0 || passengers_on_board > 0) {
                captainCondition.await();
            }
            System.out.println("Every passenger left the ship");
            gate_out = 0;
            gate_in = 1;
            bridgeCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}