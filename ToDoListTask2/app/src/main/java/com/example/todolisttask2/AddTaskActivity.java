package com.example.todolisttask2;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddTaskActivity extends AppCompatActivity {
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
        tbxTaskName = (EditText) findViewById(R.id.tbxTaskName);
        tbxDuration = (EditText) findViewById(R.id.tbxDuration);
        tbxDescription = (EditText) findViewById(R.id.tbxDescription);

        validator = new InputValidation(this);
        DatePicker dp = (DatePicker) findViewById(R.id.dpDeadline);
        Calendar c = Calendar.getInstance();
        dp.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);
    }

    public void onClickAddTask(View v) throws ParseException {
        boolean valid = validator.validateTaskCreateInput(tbxTaskName, tbxDuration);
        if (valid) {
            String name, des;
            int duration;
            name = ((EditText) findViewById(R.id.tbxTaskName)).getText().toString();
            des = ((EditText) findViewById(R.id.tbxDescription)).getText().toString();
            DatePicker dp = (DatePicker) findViewById(R.id.dpDeadline);
            duration = Integer.valueOf(((EditText) findViewById(R.id.tbxDuration)).getText().toString());
            String dateText = String.valueOf(dp.getDayOfMonth()) + "/" +
                    String.valueOf(dp.getMonth() + 1) + "/" +
                    String.valueOf(dp.getYear());
            Task t = new Task(name, new SimpleDateFormat("dd/MM/yyyy").parse(dateText), duration, des);
            MainActivity.taskList.add(t);
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}