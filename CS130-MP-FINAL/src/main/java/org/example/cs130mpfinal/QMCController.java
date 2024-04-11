package org.example.cs130mpfinal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class QMCController
{
    private QuineMcCluskeyCalculator QMCDriver;
    private String minterms;

    @FXML
    private TextField mtInput, varsInput;
    @FXML
    private Hyperlink help;
    @FXML
    private Label customInfo;
    @FXML
    private TextArea solution;
    @FXML
    private Button solve, clear;
    @FXML
    private RadioButton yes;
    @FXML
    private RadioButton no;

    @FXML
    public void initialize()
    {
        ToggleGroup group = new ToggleGroup();
        yes.setToggleGroup(group);
        no.setToggleGroup(group);

        no.setSelected(true);
        customInfo.setVisible(false);
        varsInput.setText("A, B, C, D, E, F, G, H, I, J");

        solve.setOnAction(event -> handleSolveButton());
        clear.setOnAction(event -> handleClearButton());
        yes.setOnAction (event -> {
            varsInput.setEditable(true);
            varsInput.clear();
        });
        no.setOnAction (event -> {
            varsInput.setEditable(false);
            varsInput.setText("A, B, C, D, E, F, G, H, I, J");
        });


    }
    @FXML
    private void handleSolveButton()
    {
        minterms = mtInput.getText();
        QMCDriver = new QuineMcCluskeyCalculator(minterms);
        boolean mintermsAreValid = validMinterms(minterms);
        customInfo.setVisible(true);

        String variables = varsInput.getText();
        QMCDriver.solve();
        if (mintermsAreValid && minterms.matches("[\\d,\\s]+"))
        {
            solution.setText(QMCDriver.printResults(customVars(variables)));
        }
    }

    @FXML
    private void handleClearButton()
    {
        mtInput.clear();
        varsInput.setText("A, B, C, D, E, F, G, H, I, J");
        no.setSelected(true);
        customInfo.setVisible(false);
        varsInput.setEditable(false);
        solution.clear();

    }


    @FXML
    private void handleHyperlink() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.desmos.com/scientific"));
    }

    public boolean validMinterms(String s)
    {
        s = s.replace(", ",",");
        s = s.replace(","," ");
        String[] temp = s.trim().split(" +");

        int[] intMinterms = new int[temp.length];

        for (int i = 0; i < temp.length; i++)
        {
            if (temp[i].equals("")) {
                showAlert("Your input must not contain consecutive commas.","Invalid Input");
                return false;
            }

            try
            {
                intMinterms[i] = Integer.parseInt(temp[i]);
                if (intMinterms[i] > 1023)
                {
                    showAlert("Inputted minterms must not exceed maximum number 1023\nYour input: " + s, "Invalid Input");
                    return false;
                }
            }
            catch (NumberFormatException e)
            {
                showAlert("Your input must only contain integers and be comma or space delimited\ne.g. 1,2,3; 1 2 3; 1, 2, 3\n\nYour input: " + s, "Invalid Input");
                mtInput.setText("");
                return false;
            }
        }

        return true;
    }

    public void showAlert(String msg, String title)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public String[] customVars(String s)
    {
        String[] variables = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        if (s.trim().isEmpty())
        {
            return variables;
        }

        String[] inputVariables = s.replace(", ", ",").replace(",", " ").trim().split(" +");
        int inputLength = inputVariables.length;
        if (inputLength < 10)
        {
            customInfo.setText("Missing variables padded with default");
        }

        for (int i = 0; i < Math.min(inputLength, 10); i++)
        {
            variables[i] = inputVariables[i].length() > 1 ? "(" + inputVariables[i] + ")" : inputVariables[i];
        }
        if (inputLength > 10) {
            customInfo.setText("Using only first 10 inputted variables");
        }
        return variables;
    }
}




