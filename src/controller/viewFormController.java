package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import tm.runingappTM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class viewFormController {
    public Button btnActiveOnTime;
    public Label lbltimecurrent;
    public ComboBox cmbxreminding;
    @FXML
    private ListView<runingappTM> lstRuningapp;
    @FXML
    private AnchorPane mainformroot;

    @FXML
    private Label lblontimemaching;

   LocalTime date1;

    public void initialize(){
        listLoadRunningApp();
        setTimeC();
        remidway();
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
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
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
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

        notifi.text("YOU ARE WORK WITH :->: "+dura);
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
            int i=0;
            int j=0;
            long x=0;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    Thread.sleep(1000);


                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
// mouse moved identification
                Point p =MouseInfo.getPointerInfo().getLocation();
                if(!p.equals(lastPoint)){
                    System.out.println(i);

                    i++;



                }

                if(i>=0 && i<=5){
                    x-=2;
                }

                if(x>=20){
                  //  System.out.println("take a break!");
                    x=0;
                    i=0;


                }
                else if(x==0 || x<2){
                  //  System.out.println("user not hear!");
                }

                lastPoint=p;
            }




        };
    Timer timer = new Timer(100, al);
    timer.start();


}
    public  String [] remind={"All 20 Minites Reminding Me","System process Remind Me"};
    public void cmbxremindingOnAction(ActionEvent event) {
String remindr= (String) cmbxreminding.getSelectionModel().getSelectedItem();


// selected reminder type
        if (remind[0]==remindr) {
            System.out.println("All 20 Minites Reminding Me");
        }
        else{
            System.out.println("x");
        }


    }

    public void remidway(){
        cmbxreminding.setItems(FXCollections.observableArrayList(remind));
    }





}





