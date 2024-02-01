package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import tm.runingappTM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class viewFormController {
    public Button btnActiveOnTime;
    public Label lbltimecurrent;
    public ComboBox cmbxreminding;
    public Label lblStopwatched;
    @FXML
    private ListView<runingappTM> lstRuningapp;
    @FXML
    private AnchorPane mainformroot;
    @FXML
    private Label lblontimemaching;
   LocalTime date1;
    boolean x ;
    LocalTime startFalse;
    // Duration time sice first false come to second true;
    Duration durationTimeFalse;
    LocalTime activeWorkTimeStart;
    LocalTime activeWorkTime;
    Duration activeDura;
    int breakDuration;
    int timeActive;

    public void initialize(){
        x=false;
        lblStopwatched.setText("Description About Mode");

        listLoadRunningApp();
        setTimeC();
        remidway();
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        date1 = LocalTime.parse(currentTime.format(dateFormatter));

        lblontimemaching.setText(currentTime.format(dateFormatter));
    }
    @FXML
    void btnminimizeOnAction(ActionEvent event) {
        Stage stage=(Stage)mainformroot.getScene().getWindow();
        stage.setIconified(true);
    }
  public void   listLoadRunningApp(){
      ObservableList<runingappTM> items = lstRuningapp.getItems();
      items.clear();
      try {
          Process process =Runtime.getRuntime().exec("tasklist");
          BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
          String line;

          while ((line = reader.readLine()) != null) {
              String[] parts = line.split("\\s+");

              // Assuming the application name is in the first part
              String appName = parts[0];

              // Add the application name to the list
              items.add(new runingappTM(appName));
          }

          reader.close();

      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }
    public void btnSystemOntimeAction(ActionEvent actionEvent) {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        LocalTime date2 = LocalTime.parse(currentTime.format(dateFormatter));
      //get duration  Scene are on and use
        Duration duration= Duration.between(date2,date1);
        String dura = String.valueOf(duration);
        dura =dura.substring(3,dura.length());
        Image img = new Image("image/c.png");
        ImageView imgV =new ImageView(img);
        imgV.setFitHeight(50);
        imgV.setFitWidth(50);
        Notifications notifi = Notifications.create();
        notifi.graphic(imgV);
        notifi.text("YOU ARE WORK WITH : "+dura );
        notifi.title("REMINDER ARE ACTIVE TIME !");
        notifi.hideAfter(javafx.util.Duration.seconds(2));
        notifi.position(Pos.BASELINE_RIGHT);
        notifi.darkStyle();
        notifi.show();
    }
    public void btnLogOutOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }
    // real time display in lable
    public void setTimeC(){
       Thread thread =  new Thread(()->{
          SimpleDateFormat simpleDateFormat =new SimpleDateFormat("hh:mm:ss a");

          while(true){
              try {
                  Thread.sleep(1000);
              } catch (InterruptedException e) {
                  throw new RuntimeException(e);
              }
              final  String time = simpleDateFormat.format(new Date());
              Platform.runLater(()->{
                    lbltimecurrent.setText(time);
              });
          }

        });
thread.start();
    }
    // Event of mouse
public viewFormController(){

        ActionListener al =new ActionListener() {
            Point lastPoint;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    Thread.sleep(4500);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
// mouse moved identification
                Point p =MouseInfo.getPointerInfo().getLocation();
                if(!p.equals(lastPoint)){
                  x=true;
                }
                else {
                    x=false;
                }
                lastPoint=p;
               // System.out.println(x);
            }
        };
    Timer timer = new Timer(100, al);
    timer.start();
}

//************
    public  String [] remind={"All 20 Minutes Reminding Me","System process Remind Me"};
    public void cmbxremindingOnAction(ActionEvent event) {
                String remindr= (String) cmbxreminding.getSelectionModel().getSelectedItem();
//************All 20 Minites Reminding Me?
        if(remindr.equals(remind[0])){
            lblStopwatched.setText("Reminding You, All 20 minutes\nNot Counting Your Time Interval!..");
            check.stop();
            alt.setCycleCount(Timeline.INDEFINITE);
            alt.play();
        }
//************System process Remind Me
        else if(remindr.equals(remind[1])){
            lblStopwatched.setText("Its Reminding You,\nCounting Your Time Interval!..");
            alt.stop();
            check.setCycleCount(Timeline.INDEFINITE);
            check.play();

        }



    }
    public void remidway(){
        cmbxreminding.setItems(FXCollections.observableArrayList(remind));
    }
    // Notification popup of selected all 20 Times reminding
        Timeline alt =new Timeline(new KeyFrame(javafx.util.Duration.seconds(5),event -> {

            Image img = new Image("image/alwas.png");

            ImageView imgV =new ImageView(img);
            imgV.setFitHeight(50);
            imgV.setFitWidth(50);
            Notifications notifi = Notifications.create();

            notifi.graphic(imgV);

            notifi.text("Take a Break For Protect Your Health...");
            notifi.title("MODE : All 20 Minutes Reminding Me");
            notifi.hideAfter(javafx.util.Duration.seconds(5));
            notifi.position(Pos.BASELINE_RIGHT);

            notifi.darkStyle();
            Platform.runLater(()->{
                notifi.show();
            });


        }));

//**********timely checked Mouse event
   Timeline check =new   Timeline(new KeyFrame(javafx.util.Duration.seconds(5),event->{
   //mouse move this if is true and calculate the time
        if(x){

            activeWorkTimeStart = LocalTime.now();
           // System.out.println(now);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            do{
                {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    activeWorkTime = LocalTime.now();
                     activeDura = Duration.between(activeWorkTimeStart, activeWorkTime);
                    System.out.println(activeDura);


                }
            }
            while (x);
         int activeDuraTIme= (int) activeDura.getSeconds();
         if(activeDuraTIme>20){
             System.out.println("Time is Greter Than 20");
         }
//            if(breakDuration>20){
//                activeDuraTIme=activeDuraTIme-breakDuration;
//                if(activeDuraTIme>20){
//                    System.out.println("time to break");
//                    activeDura=null;
//                    //give the time for user to get the break
//                }
//            }
//            else if(activeDuraTIme>20 && breakDuration<20){
//                System.out.println("Mouse freezed time less than 20 get the break");
//                activeDura=null;
//                //give the time for user to get the
//            }


        }
   //mouse is not moving
        else if(x==false){
            try {
                startFalse= LocalTime.now();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        //after 5 second check the mouse are moving
            if(x==false){
            // mouse is moving

                if(x==true){

                    startFalse =null;

                }
                else{
                    // after 5 second mouse are not moving
                    while (!x){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        LocalTime falseDuringTime = LocalTime.now();
                        durationTimeFalse =Duration.between(startFalse,falseDuringTime);
                         breakDuration = (int) durationTimeFalse.getSeconds();
                        if(breakDuration >20){

                            System.out.println("Duration if Grater Than >30 : "+ breakDuration);
                            if(breakDuration>120){

                            }
                        }


                    }
                }
            }
        }
    }));


}





