package org.example.cs130mpfinal;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.TextAlignment;

public class QMCController
{
    private QuineMcCluskeyCalculator QMCDriver;
    private String minterms, variables;

    @FXML
    private TextField mtInput, varsInput;
    @FXML
    private Label customInfo;
    //    @FXML
//    private Alert a = new Alert(AlertType.NONE);
    @FXML
    private TextArea solution;
    @FXML
    private Button solve, clear, exit;
    @FXML
    private RadioButton yes;
    @FXML
    private RadioButton no;

    @FXML
    public void initialize()
    {
        solve.setOnAction(event -> handleSolveButton());
        clear.setOnAction(event -> handleClearButton());
        yes.setOnAction (event -> {
            mtInput.setEditable(true);
            mtInput.clear();
        });
        no.setOnAction (event -> {
            mtInput.setEditable(false);
            mtInput.setText("A, B, C, D, E, F, G, H, I, J");
        });


    }
    @FXML
    private void handleSolveButton()
    {
        minterms = mtInput.getText();
        QMCDriver = new QuineMcCluskeyCalculator(minterms);
        boolean mintermsAreValid = validMinterms(minterms);

        if (!mintermsAreValid)
        {
            showAlert("Input exceeds maximum (1023): " + minterms, "Error");
            mtInput.setText("");
        }

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
        varsInput.setEditable(false);
        solution.clear();

    }

    public boolean validMinterms(String s)
    {
        s = s.replace(", ",",");
        s = s.replace(","," ");
        String[] temp = s.trim().split(" +");

        int[] intMinterms = new int[temp.length];

        for (int i = 0; i < temp.length; i++)
        {
            try
            {
                intMinterms[i] = Integer.parseInt(temp[i]);
                if (intMinterms[i] > 1023)
                {
                    return false;
                }
            }
            catch (NumberFormatException e)
            {
                showAlert("Invalid Input: " + s, "Error");
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
            customInfo.setText("Variables missing, using default for the rest*");
        }

        for (int i = 0; i < Math.min(inputLength, 10); i++)
        {
            variables[i] = inputVariables[i].length() > 1 ? "(" + inputVariables[i] + ")" : inputVariables[i];
        }
        if (inputLength > 10) {
//            customInfo.setForeground(new Color(204,0,51));
            customInfo.setText("Input exceeds 10 variables, using only first 10*");
        }
        return variables;
    }
}




