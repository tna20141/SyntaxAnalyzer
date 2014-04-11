package analz;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jfx.messagebox.MessageBox;


public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ToggleGroup FileModeRadio;

    @FXML
    private Button analyzeButton;

    @FXML
    private RadioButton fileModeRadio1;

    @FXML
    private RadioButton fileModeRadio2;

    @FXML
    private RadioButton fileModeRadio3;

    @FXML
    private TextField fileTextField;

    @FXML
    private TextArea resultTextArea;

    @FXML
    private TextField ruleTextField;

    @FXML
    private TextArea sentenceTextArea;

    @FXML
    private TextField tagTextField;

    @FXML
    private Button updateButton;

    @FXML
    private ToggleGroup AlgorithmRadio;

    @FXML
    private RadioButton CYKRadio;

    @FXML
    private RadioButton EarleyRadio;

    private Stage primaryStage;


    @FXML
    void analyzeHandler(MouseEvent event) {
        String rawSentence = this.sentenceTextArea.getText();
        Sentence sentence;

        try {
            sentence = new Sentence(rawSentence);
        } catch (Exception e) {
            MessageBox.show(this.primaryStage, "Lỗi khi xử lý câu:\n" + e.getMessage(), "Lỗi",
                    MessageBox.ICON_ERROR | MessageBox.OK);
            return;
        }

        Parser parser;
        if (this.AlgorithmRadio.getSelectedToggle().getUserData().equals("CYK"))
            parser = CYKParser.getInstance();
        else
            parser = EarleyParser.getInstance();

        LinkedList<SyntaxNode> trees = parser.parse(sentence);

        LinkedList<String> results = Outputer.toStringList(trees);
        if (results.isEmpty()) {
            this.resultTextArea.setText("-- Không phân tích được câu đầu vào --");
            return;
        }

        String result = "";
        for (String str : results) {
            result += str + "\n";
        }

        this.resultTextArea.setText(result);
    }

    @FXML
    void updateHandler(MouseEvent event) {
        String tagFile = this.tagTextField.getText();
        String ruleFile = this.ruleTextField.getText();

        if (tagFile.isEmpty() || ruleFile.isEmpty()) {
            MessageBox.show(this.primaryStage, "Đường dẫn file không được để trống!", "Cảnh báo",
                    MessageBox.ICON_WARNING | MessageBox.OK);
            return;
        }

        initGrammar(tagFile, ruleFile);
    }

    @FXML
    void initialize() {
        assert AlgorithmRadio != null : "fx:id=\"AlgorithmRadio\" was not injected: check your FXML file 'sample.fxml'.";
        assert CYKRadio != null : "fx:id=\"CYKRadio\" was not injected: check your FXML file 'sample.fxml'.";
        assert EarleyRadio != null : "fx:id=\"EarleyRadio\" was not injected: check your FXML file 'sample.fxml'.";
        assert FileModeRadio != null : "fx:id=\"FileModeRadio\" was not injected: check your FXML file 'sample.fxml'.";
        assert analyzeButton != null : "fx:id=\"analyzeButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert fileModeRadio1 != null : "fx:id=\"fileModeRadio1\" was not injected: check your FXML file 'sample.fxml'.";
        assert fileModeRadio2 != null : "fx:id=\"fileModeRadio2\" was not injected: check your FXML file 'sample.fxml'.";
        assert fileModeRadio3 != null : "fx:id=\"fileModeRadio3\" was not injected: check your FXML file 'sample.fxml'.";
        assert fileTextField != null : "fx:id=\"fileTextField\" was not injected: check your FXML file 'sample.fxml'.";
        assert resultTextArea != null : "fx:id=\"resultTextArea\" was not injected: check your FXML file 'sample.fxml'.";
        assert ruleTextField != null : "fx:id=\"ruleTextField\" was not injected: check your FXML file 'sample.fxml'.";
        assert sentenceTextArea != null : "fx:id=\"sentenceTextArea\" was not injected: check your FXML file 'sample.fxml'.";
        assert tagTextField != null : "fx:id=\"tagTextField\" was not injected: check your FXML file 'sample.fxml'.";
        assert updateButton != null : "fx:id=\"updateButton\" was not injected: check your FXML file 'sample.fxml'.";

        this.primaryStage = Main.getPrimaryStage();

        this.fileModeRadio1.setUserData("append");
        this.fileModeRadio2.setUserData("write");
        this.fileModeRadio3.setUserData("none");

        this.CYKRadio.setUserData("CYK");
        this.EarleyRadio.setUserData("Earley");

        this.tagTextField.setText("Resources/tags.txt");
        this.ruleTextField.setText("Resources/rules.txt");

        initGrammar(this.tagTextField.getText(), this.ruleTextField.getText());

        CYKParser.getInstance();
        EarleyParser.getInstance();
    }

    private void initGrammar(String tagFile, String ruleFile) {
        Grammar grammar = Grammar.getInstance();
        try {
            grammar.init(tagFile, ruleFile);
        } catch (Exception e) {
            MessageBox.show(this.primaryStage, "Lỗi khi khởi tạo bộ ngữ pháp:\n" + e.getMessage() + "\n\n" +
                    "Xin chỉnh lại ở phần Tùy Chỉnh.", "Lỗi", MessageBox.ICON_ERROR | MessageBox.OK);
        }

    }
}
