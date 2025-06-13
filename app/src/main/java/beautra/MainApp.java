package beautra;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

public class MainApp extends Application {
    public static User currentUser;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("BEAUTY AURA");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
