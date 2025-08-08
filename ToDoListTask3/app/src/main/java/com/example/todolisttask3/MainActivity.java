package com.example.todolisttask3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.cursoradapter.widget.ResourceCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper helper;
    private TaskCursorAdapter taskAdapter;

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
        helper = new DatabaseHelper(this);
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem clearDoneTask = menu.findItem(R.id.clearDoneTask);
        clearDoneTask.setOnMenuItemClickListener(item -> {
            helper.clearDoneTask();
            loadAllTask();
            Toast.makeText(this, "Done task cleared", Toast.LENGTH_SHORT).show();
            return false;
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void onClickAdd(View v) {
        Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
        startActivity(intent);
    }

    protected void onStart() {
        super.onStart();
        loadAllTask();
    }

    //Method to load all task from database
    private void loadAllTask() {
        Cursor result = helper.readAllTask();
        if (taskAdapter == null) {
            taskAdapter = new TaskCursorAdapter(this, R.layout.task_item, result, 0);

        } else {
            taskAdapter.changeCursor(result);
        }
        ListView lvTask = findViewById(R.id.lvTask);
        lvTask.setAdapter(taskAdapter);
        lvTask.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(getApplicationContext(), DetailTask.class);
            i.putExtra("id", id);
            startActivity(i);
        });
    }
}

class TaskCursorAdapter extends ResourceCursorAdapter {
    private DatabaseHelper helper;

    public TaskCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
        helper = new DatabaseHelper(context);
    }

    @SuppressLint("Range")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvTaskName = view.findViewById(R.id.tvTaskName);
        TextView tvDue = view.findViewById(R.id.inputDueOn);
        TextView tvDuration = view.findViewById(R.id.inputDuration);
        TextView tvDetail = view.findViewById(R.id.inputDetail);
        CheckBox checkBoxDone = view.findViewById(R.id.checkBoxDone);

        final long taskId = cursor.getLong(cursor.getColumnIndexOrThrow("_id")); //final to make sure that the taskId not change
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String deadline = cursor.getString(cursor.getColumnIndexOrThrow("deadline"));
        int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        int isDone = cursor.getInt(cursor.getColumnIndexOrThrow("isTaskDone"));

        tvTaskName.setText(name);
        if (deadline != null) {
            tvDue.setText(deadline.substring(0, Math.min(deadline.length(), 10)));
        } else {
            tvDue.setText("");
        }
        tvDuration.setText(String.valueOf(duration));
        tvDetail.setText(description);
        checkBoxDone.setChecked(isDone == 1); // true 1; false 0

        if (isDone == 1) {
            tvTaskName.setTextColor(Color.GRAY);
        } else {
            tvTaskName.setTextColor(Color.BLACK);
        }

        checkBoxDone.setOnCheckedChangeListener(null);
        checkBoxDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            helper.updateTaskStatus(taskId, isChecked);
            if (isChecked) {
                tvTaskName.setTextColor(Color.GRAY);
            } else {
                tvTaskName.setTextColor(Color.BLACK);
            }
        });
    }
}
