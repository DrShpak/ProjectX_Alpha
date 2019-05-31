package sample.controllers;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import com.interpreter.lexer.Lexer;
import com.interpreter.parser.Parser;
import com.interpreter.parser.ast.statements.Statement;
import com.interpreter.token.Token;
import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import sample.Main;

public class Controller {

    @FXML
    private MenuItem run;

    @FXML
    private JFXTextArea textArea;

    @FXML
    private JFXTextArea output;

    public static StringBuilder sb = new StringBuilder();

    @FXML
    private MenuItem open;

    @FXML
    private MenuItem saveItem;

    @FXML
    public void saveFile(ActionEvent event) throws IOException {
        File save = fileChooser.showSaveDialog(Main.myStage);
        PrintWriter pw = new PrintWriter(new File(save.getAbsolutePath()));
        pw.println(textArea.getText());
        pw.close();

    }

    private FileChooser fileChooser = new FileChooser();

    @FXML
    void initialize() {
        textArea.setFont(new Font("Monaco", 16));
        output.setFont(new Font("Monaco", 16));
        openFile(open);
        runProgram(run);

    }

    private void runProgram(MenuItem run) {
        run.setOnAction(event -> {

            Lexer lexer = new Lexer(textArea.getText());
            ArrayList<Token> tokens = lexer.tokenize();

            Parser parser = new Parser(tokens);
//            Statement program = parser.parse();

//            program.execute();
            output.clear();
            sb.delete(0, sb.length());
            try {
                Statement program = parser.parse();
                program.execute();
                output.appendText(sb.toString());
            } catch (RuntimeException e) {
                output.appendText(e.toString());
            }
        });
    }

    private void openFile(MenuItem open) {
        open.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(Main.myStage);
            if (file != null) {
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    Files.readAllLines(Paths.get(file.getAbsolutePath()))
                            .forEach(line -> stringBuilder.append(line).append("\n"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textArea.clear();
                textArea.appendText(stringBuilder.toString());
            }
        });
    }
}
