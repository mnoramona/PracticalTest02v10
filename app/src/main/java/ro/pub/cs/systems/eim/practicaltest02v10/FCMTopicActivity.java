package ro.pub.cs.systems.eim.practicaltest02v10;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// import com.google.firebase.messaging.FirebaseMessaging;

public class FCMTopicActivity extends AppCompatActivity {

    private EditText topicNameInput;
    private Button subscribeButton;
    private Button unsubscribeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm_topic);

        // Inițializăm elementele UI
        topicNameInput = findViewById(R.id.topic_name_input);
        subscribeButton = findViewById(R.id.subscribe_button);
        unsubscribeButton = findViewById(R.id.unsubscribe_button);

//        // Funcționalitate pentru Subscribe
//        subscribeButton.setOnClickListener(v -> {
//            String topic = topicNameInput.getText().toString().trim();
//            if (topic.isEmpty()) {
//                Toast.makeText(this, "Please enter a topic name", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        });
//
//        // Funcționalitate pentru Unsubscribe
//        unsubscribeButton.setOnClickListener(v -> {
//            String topic = topicNameInput.getText().toString().trim();
//            if (topic.isEmpty()) {
//                Toast.makeText(this, "Please enter a topic name", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        });
    }
}
