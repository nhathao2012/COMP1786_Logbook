package com.example.unitconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText tbxOutput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tbxOutput = findViewById(R.id.tbxOutput);
    }
    public void onConvertClick(View view) {
        //get the data from the textboxes and spinners
        Spinner spUnitInput = findViewById(R.id.spUnitInput);
        Spinner spUnitOutput = findViewById(R.id.spUnitOutput);
        String unitInput = spUnitInput.getSelectedItem().toString();
        String unitOutput = spUnitOutput.getSelectedItem().toString();

        EditText tbxInput = findViewById(R.id.tbxInput);
        String valueInputCheck = tbxInput.getText().toString();

        if (valueInputCheck.isEmpty()) {
            tbxInput.setError("Value input cannot be empty");
        } else {
            tbxInput.setError(null);
            double valueInput = Double.parseDouble(valueInputCheck);
            double valueOutput = convert(valueInput, unitInput, unitOutput);
            tbxOutput.setText(String.format(Locale.getDefault(),"%.2f",valueOutput));
        }
    }
     /*--Unit Conversion Steps (not include getting data from textboxes and spinners)--
        1. I convert the unit to a standard unit (I choose the meter).
        2. From that standard unit, it will be converted to another unit and returned.
    */
    private double convert(double valueInput, String unitInput, String unitOutput) {
        double stdUnit = 0.0;
        switch (unitInput) {
            case "Meter":
                stdUnit = valueInput;
                break;
            case "Millimeter":
                stdUnit = valueInput / 1000.0;
                break;
            case "Mile":
                stdUnit = valueInput * 1609.34;
                break;
            case "Foot":
                stdUnit = valueInput * 0.3048;
                break;
        }
        double valueOutput = 0.0;
        switch (unitOutput) {
            case "Meter":
                valueOutput = stdUnit;
                break;
            case "Millimeter":
                valueOutput = stdUnit * 1000.0;
                break;
            case "Mile":
                valueOutput = stdUnit / 1609.34;
                break;
            case "Foot":
                valueOutput = stdUnit / 0.3048;
                break;
        }
        return valueOutput;
    }
}