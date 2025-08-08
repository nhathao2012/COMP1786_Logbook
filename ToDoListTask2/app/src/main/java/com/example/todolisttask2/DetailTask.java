package com.example.todolisttask2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

public class DetailTask extends AppCompatActivity {
    private EditText tbxDtTaskName, tbxDtDuration, tbxDtDescription;
    private DatePicker dpDtDeadline;
    private Button btnDelete, btnSave;
    private InputValidation validator;
    private int taskId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        validator = new InputValidation(this);

        tbxDtTaskName = findViewById(R.id.tbxDtTaskName);
        tbxDtDuration = findViewById(R.id.tbxDtDuration);
        tbxDtDescription = findViewById(R.id.tbxDtDescription);
        dpDtDeadline = findViewById(R.id.dpDtDeadline);
        btnDelete = findViewById(R.id.btnDelete);
        btnSave = findViewById(R.id.btnSave);

        if (getIntent().hasExtra("id")) {
            taskId = getIntent().getIntExtra("id", -1);
        }

        btnSave.setOnClickListener(v -> saveTaskDetails());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadTaskDetails(taskId);
    }

    private void loadTaskDetails(int taskId) {
        Task task = MainActivity.taskList.get(taskId);
        tbxDtTaskName.setText(task.name);
        tbxDtDuration.setText(String.valueOf(task.duration));
        tbxDtDescription.setText(task.description);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(task.deadline);
        dpDtDeadline.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

    }

    private void saveTaskDetails() {
        String name = tbxDtTaskName.getText().toString().trim();
        String durationStr = tbxDtDuration.getText().toString().trim();
        String description = tbxDtDescription.getText().toString().trim();

        boolean valid = validator.validateTaskCreateInput(tbxDtTaskName, tbxDtDuration);
        if (valid) {
            int duration = Integer.parseInt(durationStr);
            int day = dpDtDeadline.getDayOfMonth();
            int month = dpDtDeadline.getMonth();
            int year = dpDtDeadline.getYear();

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);

            Task task = MainActivity.taskList.get(taskId);
            task.name = name;
            task.duration = duration;
            task.description = description;
            task.deadline = calendar.getTime();

            Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> deleteTask())
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void deleteTask() {
        MainActivity.taskList.remove(taskId);
        Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
        finish();

    }
}