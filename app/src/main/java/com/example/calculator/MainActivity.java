package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private TextView historyDisplay;
    private StringBuilder input;
    private StringBuilder history;
    private ArrayList<Double> numbers;
    private char operator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
        historyDisplay = findViewById(R.id.display);
        input = new StringBuilder();
        history = new StringBuilder();
        numbers = new ArrayList<>();
        operator = ' ';

    }

    // Define button click handlers
    public void onButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        switch (buttonText) {
            case "C":
                clearDisplay();
                break;
            case "=":
                calculate();
                break;
            case "+":
            case "-":
            case "x":
            case "/":
                if (input.length() > 0) {
                    setOperator(buttonText.charAt(0));
                }
                break;
            default:
                appendInput(buttonText);
        }
    }

    // Handle delete button click
    public void onDeleteButtonClick(View view) {
        if (input.length() > 0) {
            input.deleteCharAt(input.length() - 1);
            display.setText(input.toString());
        }
    }

    private void clearDisplay() {
        input.setLength(0);
        display.setText("0");
        history.setLength(0);
        historyDisplay.setText("");
        numbers.clear();
        operator = ' ';
    }

    private void appendInput(String text) {
        input.append(text);
        history.append(text);
        display.setText(input.toString());
        historyDisplay.setText(history.toString());
    }

    private void setOperator(char op) {
        if (input.length() > 0) {
            numbers.add(Double.parseDouble(input.toString()));
            history.append(" " + op + " ");
            display.setText("");
            operator = op;
            input.setLength(0); // clear input for next number
            historyDisplay.setText(history.toString());
        }
    }

    private void calculate() {
        if (input.length() > 0) {
            numbers.add(Double.parseDouble(input.toString()));
            double result = numbers.get(0);
            for (int i = 1; i < numbers.size(); i++) {
                switch (operator) {
                    case '+':
                        result += numbers.get(i);
                        break;
                    case '-':
                        result -= numbers.get(i);
                        break;
                    case 'x':
                        result *= numbers.get(i);
                        break;
                    case '/':
                        if (numbers.get(i) != 0)
                            result /= numbers.get(i);
                        else {
                            display.setText("Error");
                            return;
                        }
                        break;
                }
            }
            display.setText(String.valueOf(result));
            history.append(" = ").append(result);
            historyDisplay.setText(history.toString());

        }
    }

    private void showQuadraticEquationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter A, B, C");

        final EditText editTextA = new EditText(this);
        final EditText editTextB = new EditText(this);
        final EditText editTextC = new EditText(this);

        editTextA.setHint("Enter A");
        editTextB.setHint("Enter B");
        editTextC.setHint("Enter C");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(editTextA);
        layout.addView(editTextB);
        layout.addView(editTextC);
        builder.setView(layout);

        builder.setPositiveButton("Calculate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double a = Double.parseDouble(editTextA.getText().toString());
                double b = Double.parseDouble(editTextB.getText().toString());
                double c = Double.parseDouble(editTextC.getText().toString());
                solveQuadraticEquation(a, b, c);
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.create().show();
    }
    public void solveQuadraticEquation(double a, double b, double c) {
        double delta = b * b - 4 * a * c;
        double x1, x2;
        if (delta > 0) {
            x1 = (-b + Math.sqrt(delta)) / (2 * a);
            x2 = (-b - Math.sqrt(delta)) / (2 * a);
            display.setText("Roots are: x1 = " + x1 + ", x2 = " + x2);
        } else if (delta == 0) {
            x1 = -b / (2 * a);
            display.setText("Root is: x = " + x1);
        } else {
            display.setText("No real roots exist.");
        }
    }

}