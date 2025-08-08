package com.example.todolisttask3;

import android.annotation.SuppressLint;
import android.database.Cursor;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailTask extends AppCompatActivity {
    private EditText tbxDtTaskName, tbxDtDuration, tbxDtDescription;
    private DatePicker dpDtDeadline;
    private Button btnDelete, btnSave;
    private DatabaseHelper helper;
    private InputValidation validator;
    private long taskId = -1;


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

        helper = new DatabaseHelper(this);
        validator = new InputValidation(this);

        tbxDtTaskName = findViewById(R.id.tbxDtTaskName);
        tbxDtDuration = findViewById(R.id.tbxDtDuration);
        tbxDtDescription = findViewById(R.id.tbxDtDescription);
        dpDtDeadline = findViewById(R.id.dpDtDeadline);
        btnDelete = findViewById(R.id.btnDelete);
        btnSave = findViewById(R.id.btnSave);

        if (getIntent().hasExtra("id")) {
            taskId = getIntent().getLongExtra("id", -1);
        }


        btnSave.setOnClickListener(v -> saveTaskDetails());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }
    @Override
    protected void onStart() {
        super.onStart();
        loadTaskDetails(taskId);
    }

    @SuppressLint("Range")
    public void loadTaskDetails(long taskId) {
        Cursor cursor = helper.getTaskById(taskId);
        if (cursor != null && cursor.moveToFirst()) {

            tbxDtTaskName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            tbxDtDuration.setText(cursor.getString(cursor.getColumnIndexOrThrow("duration")));
            tbxDtDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow("description")));

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date deadline = sdf.parse(cursor.getString(cursor.getColumnIndexOrThrow("deadline")));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(deadline);
                dpDtDeadline.updateDate(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));


            } catch (ParseException e) {
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveTaskDetails() {
        String name = tbxDtTaskName.getText().toString().trim();
        int duration = Integer.parseInt(tbxDtDuration.getText().toString().trim());
        String description = tbxDtDescription.getText().toString().trim();

        boolean valid = validator.validateTaskCreateInput(tbxDtTaskName, tbxDtDuration);
        if (valid) {
            int day = dpDtDeadline.getDayOfMonth();
            int month = dpDtDeadline.getMonth();
            int year = dpDtDeadline.getYear();
            String deadline = String.format(Locale.getDefault(), "%02d/%02d/%d", day, month + 1, year);

            if (taskId != -1) {
                helper.updateTask(taskId, name, deadline, duration, description);
                Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error updating task", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Course")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton)
                        -> deleteTask())
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void deleteTask() {
        if (taskId != -1) {
            helper.deleteTask(taskId);
            Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}