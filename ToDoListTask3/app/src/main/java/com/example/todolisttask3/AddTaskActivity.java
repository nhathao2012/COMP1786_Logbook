package com.example.todolisttask3;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.util.Calendar;


public class AddTaskActivity extends AppCompatActivity {
    private DatabaseHelper helper;
    private InputValidation validator;
    private EditText tbxTaskName, tbxDuration, tbxDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        helper = new DatabaseHelper(this);
        validator = new InputValidation(this);
        DatePicker dp = (DatePicker) findViewById(R.id.dpDeadline);
        Calendar c = Calendar.getInstance();
        dp.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);
        tbxTaskName = findViewById(R.id.tbxTaskName);
        tbxDuration = findViewById(R.id.tbxDuration);
        tbxDescription = findViewById(R.id.tbxDescription);
    }

    public void onClickAddTask(View v) throws ParseException {
        boolean valid = validator.validateTaskCreateInput(tbxTaskName, tbxDuration);
        if (valid) {
            String nameValue = tbxTaskName.getText().toString().trim();
            String durationValue = tbxDuration.getText().toString().trim();
            int durationInt = Integer.valueOf(durationValue);
            String descriptionValue = tbxDescription.getText().toString().trim();
            DatePicker dp = (DatePicker) findViewById(R.id.dpDeadline);

            String dateText = String.valueOf(dp.getDayOfMonth()) + "/" +
                    String.valueOf(dp.getMonth() + 1) + "/" +
                    String.valueOf(dp.getYear());

            helper.createNewTask(nameValue, dateText, durationInt, descriptionValue);
            Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
            finish();

        }
    }

}