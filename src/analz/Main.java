package analz;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.LinkedList;


public class Main extends Application {

    private static Stage primaryStage = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Syntax Analyzer");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    public static void main(String[] args) throws Exception {
        launch(args);

//        Sentence s1 = new Sentence("Đường/N đến/V võ_đài/N");
//        Sentence s2 = new Sentence("Tôi/P nhìn/V bò/N trên/E núi/N ./.");
//        Sentence s3 = new Sentence("Bạn/N tôi/P Hy/X1 Hy/X2 đi_xem/V phim/N");
//        Parser p =
//                new CYKParser();
//                new EarleyParser();
//        LinkedList<SyntaxNode> trees1 = p.parse(s1);
//        LinkedList<SyntaxNode> trees2 = p.parse(s2);
//        LinkedList<SyntaxNode> trees3 = p.parse(s3);

//        LinkedList<String> strs = SyntaxNode.toString(trees);
//        Outputer.write("/home/tna2/tmp/aab.prd", strs, true, false);

        return;
    }
}
