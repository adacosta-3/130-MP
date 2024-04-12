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

/**
 * The QMC Controller class is a JavaFX controller class for the Quine-McCluskey Calculator application.
 * It manages the user interface components and handles user interactions.
 * Contains methods and fields to initialize UI, handle button clicks and user input to perform the algorithm.
 *
 * @author Arianne Jayne Acosta
 * @author Christian Jesse Bonifacio
 * @version 2.0
 * @since 2024-04-10
 */
public class QMCController
{
    private QuineMcCluskeyCalculator QMCDriver;
    private String minterms;

    // Components of stage
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

    /**
     * Method to initialize GUI component.
     *
     * Sets default selections and text, and attaches event handlers to buttons.
     */
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

    /**
     * Method which handles "solve" button click event.
     *
     * Retrieves user input for minterms and variables.
     * Creates a QuineMcCluskeyCalculator object, simplifies function and displays solution if it is valid.
     */
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

    /**
     * Method which handles action triggered by clicking the "clear" button in the GUI.
     *
     * Clears text input fields for minterms, sets default text for variables, resets radio button selection to default.
     */
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


    /**
     * Method which handles event wherein Help hyperlink is clicked.
     *
     * User is redirected to their browser and is navigated to the user manual.
     * @throws URISyntaxException
     * @throws IOException
     */
    @FXML
    private void handleHyperlink() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.desmos.com/scientific"));
    }

    /**
     * Helper method which validates the input string containing minterms.
     *
     * Checks for formatting errors (such as consecutive commas) and displays an alert message indicating issue encountered.
     * @param s user inputted string of minterms
     * @return true if input is valid
     */
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

    /**
     * Method which creates and displays an error message window given the passed message and title.
     * @param msg body of the error
     * @param title type of error
     */
    public void showAlert(String msg, String title)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Method which processes a string input of custom variables, returning an array of variables
     *
     * Starts with default variables "A" to "J".
     * If the input is empty, it returns defaults; otherwise, it splits and trims the input.
     * Updates a text field for user notification.
     * @param s user inputted string of custom or default variables
     * @return variables validated list of 10 variables to use for the calculator
     */
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




