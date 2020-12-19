package com.epayeats.epayeatsuser.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsuser.Activity.MainActivity;
import com.epayeats.epayeatsuser.Model.FeedbackModel;
import com.epayeats.epayeatsuser.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class UserFeedback_Fragment extends Fragment
{
    EditText userfeedback;
    Button userfeedbackbtn;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    public ProgressDialog progressDialog;

    TextView feedback_username;
    String a1, em;

    public UserFeedback_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_feedback_, container, false);

        sharedPreferences = this.getActivity().getSharedPreferences("data", 0);
        a1 = sharedPreferences.getString("userid", "");
        em= sharedPreferences.getString("useremail", "");


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Thank you for your feedback...");

        userfeedback = view.findViewById(R.id.userfeedback);
        userfeedbackbtn = view.findViewById(R.id.userfeedbackbtn);
        feedback_username = view.findViewById(R.id.feedback_username);

        feedback_username.setText(em);

        userfeedbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback();
            }
        });

        return view;
    }

    private void feedback()
    {
        String fedbck = userfeedback.getText().toString();
        if(fedbck.isEmpty())
        {
            Toast.makeText(getContext(), "Type Some Text", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.show();

            databaseReference = FirebaseDatabase.getInstance().getReference("user_feedback").child("users").child(a1);
            String pushkey = databaseReference.push().getKey();

            FeedbackModel model = new FeedbackModel(fedbck);
            databaseReference.child(pushkey).setValue(model);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext())
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                    .setContentTitle("Hey, "+ em)
                    .setContentText("Thank you for your feedback")
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL);

            Uri path = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(path);

            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "Your_Channel_ID";
                NotificationChannel channel = new NotificationChannel(channelId, "Chanel Human redable Text", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
                builder.setChannelId(channelId);
            }
            notificationManager.notify(1, builder.build());

            progressDialog.dismiss();

            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}