package analz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Syntax Analyzer");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws Exception {
//        launch(args);
        Grammar g = Grammar.getInstance();
        g.init("Resources/tags.txt", "Resources/rules.txt");
        Parser p = new CYKParser();
//        p.parse(new Sentence("Đường/N đến/V võ_đài/N", t));
//        p.parse(new Sentence("Tôi/P nhìn/V bò/N trên/E núi/N ./."));
        p.parse(new Sentence("Bạn/N tôi/P Hy/X1 Hy/X2 đi_xem/V phim/N"));
    }
}
