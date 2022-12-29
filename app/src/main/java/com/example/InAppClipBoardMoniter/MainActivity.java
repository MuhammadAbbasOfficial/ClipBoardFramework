package com.example.InAppClipBoardMoniter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.example.translator.R;

public class MainActivity extends AppCompatActivity {

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

//working fine in app not in background when we copied text it will fire the notification
// set up the clipboard event listener
        ClipboardManager clipboard = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                // get the ClipData object representing the data on the clipboard
                ClipData clip = clipboard.getPrimaryClip();
                if (clip != null) {
                    // get the MIME type of the clipboard's data
                    String mimeType = clip.getDescription().getMimeType(0);
                    if (mimeType.equals("text/plain")) {
                        // the clipboard's data is plain text
                        // get the ClipData.Item object representing the first item in the clipboard
                        ClipData.Item item = clip.getItemAt(0);
                        // get the text on the clipboard
                        CharSequence text = item.getText();
                        // create the notification
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "clipboard_channel")
                                .setSmallIcon(R.drawable.ic_launcher_background)
                                .setContentTitle("Clipboard updated")
                                .setContentText("Copied text: " + text)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);
                        // create an Intent to open the app when the notification is clicked
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                        builder.setContentIntent(pendingIntent);
                        // display the notification
                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            // create a notification channel for Android 8.0 and higher
                            NotificationChannel channel = new NotificationChannel("clipboard_channel", "Clipboard updates", NotificationManager.IMPORTANCE_HIGH);
                            notificationManager.createNotificationChannel(channel);
                        }
                        notificationManager.notify(1, builder.build());
                    }
                }
            }
        });

//working fine only clipboard function
/*        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null) {
            String mimeType = clip.getDescription().getMimeType(0);
            if (mimeType.equals("text/plain")) {
                // the clipboard's data is plain text
                if (clip != null) {
                    ClipData.Item item = clip.getItemAt(0);
                    CharSequence text = item.getText();
                    // do something with the text
                    Log.e(TAG, "onClick: mimetype is __________________________________" + text);
                } else {
                    Log.e(TAG, "onClick: mimetype is __________________________________Empty");
                    Toast.makeText(getApplicationContext(), "Empty mimtype", Toast.LENGTH_SHORT).show();
                }
            }
        }*/

    }

}





