# Concurrent programming in java with visualization in javaFX
This project shows a solution to the following problem using concurrent programming in the Java programming language with visualization done in javaFX.


## Problem to be solved

A ship with a capacity of N is standing at the quay. The ship with the land is connected by a bridge with capacity K (K<N). Passengers are trying to get on the ship, except that no more than N can get on the ship, and when entering the ship on the bridge there can be no more than K at the same time. The ship sets sail every one hour. At the time of departure, the ship's captain must ensure that there are no entering passengers on the bridge. At the same time, he must ensure that the number of passengers on the ship does not exceed N. Write the Passenger and Captain procedures, respectively, synchronized using any of the process synchronization methods.
## Program configuration
The user can change the number of seats on the bridge and ship, the time it takes to create new passengers, the time it takes for a passenger to enter/exit the bridge, the time it takes for a passenger to enter/exit the ship, and the time the ship is in the water and in port

Passengers are represented by red circles.


![image](https://github.com/KamilBorkowskiYB/Java_thread_synchronization/assets/142045004/d937c189-df0d-463c-8fec-a9883680e966)

Program in action:
![image](https://github.com/KamilBorkowskiYB/Java_thread_synchronization/assets/142045004/d22f2f8a-895b-4ddc-afd5-c079226b6305)

## Classes
### Passenger
Attempts to access ship via bridge by calling function in Ship_monitor class object.

After the cruise, it tries to disembark from the ship back to the port after which it terminates.

Creates a red circle that corresponds to the passenger in the visualization.

Sample code:
```java
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
```
       
### Captain
He waits at the port for the cruise, allowing passengers to board the ship.

If no one boards the ship then the cruise will not take place. 

The cruise cannot start while a passenger is on the bridge, so he closes the gate to the bridge and waits until everyone has boarded before sailing.

When returning from a cruise, he first lets passengers off the ship before allowing new ones to board and waits for the next cruise.

Sample code:
```java
        while (true) {
            try {
                Thread.sleep(in_port*1000);
                monitor.sail(bridge);
                monitor.open_gate();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
```

### Ship_monitor
It is used to synchronize the shared resources that are the bridge and deck spaces between the passenger processes and the captain process.

### Controller
Creates passenger class processes every given period of time.

Loads the .fxml file containing the visualization scene.

### Main
Launches application.
