package com.example.submission.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.example.submission.R;
import com.example.submission.receiver.ReminderReceiver;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity {

    Switch switchRemindToggle;
    private SharedPreferences sharedPreferences;

    private String PREV_REMINDER = "prev_reminder";
    boolean isReminderActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        switchRemindToggle = findViewById(R.id.switch_remind_toggle);

        sharedPreferences = getSharedPreferences(PREV_REMINDER, Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            isReminderActive = sharedPreferences.getBoolean(PREV_REMINDER, false);

            if (isReminderActive){
                switchRemindToggle.setChecked(true);
            } else {
                switchRemindToggle.setChecked(false);
            }
        }
        onClickSwitch();

        getSupportActionBar().setTitle("Setting");
    }

    public void onClickSwitch(){
        switchRemindToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                setReminder(getApplicationContext());
                saveSetting(true);
            } else {
                setReminderOff(getApplicationContext());
                saveSetting(false);
            }
        });
    }

    private void setReminder(Context context) {
        Intent intent = new Intent(context, ReminderReceiver.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 102, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
        Toast.makeText(SettingActivity.this, "Notification Active", Toast.LENGTH_SHORT).show();
    }

    private void setReminderOff(Context context) {
        Intent intent = new Intent(context, ReminderReceiver.class);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 102, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(SettingActivity.this, "Notification Not Active", Toast.LENGTH_SHORT).show();
    }

    public void saveSetting(Boolean isActive) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREV_REMINDER, isActive);
        editor.apply();
    }
}
