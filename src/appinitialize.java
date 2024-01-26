import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class appinitialize extends Application {


double x;
double y;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("view/viewForm.fxml")));
        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        parent.setOnMousePressed((MouseEvent event)->{
            x =event.getSceneX();
            y =event.getSceneY();
        });
        parent.setOnMouseDragged((MouseEvent e)->{
            primaryStage.setX(e.getScreenX()-x);
            primaryStage.setY(e.getScreenY()-y);
            primaryStage.setOpacity(.4);
        });
        parent.setOnMouseReleased((MouseEvent e)->{
            primaryStage.setOpacity(1);
        });
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }
}
