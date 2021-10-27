package com.example.artists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.artists.model.Fund;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class SinglePostActivity extends AppCompatActivity {

    TextView fundHeading, fundDate, fundAmount,
            fundDescription, contactName, contactNumber;
    RelativeLayout shareButton;

    DatabaseReference databaseSingleEvent;

    private Fund fund;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post);

        fundHeading = findViewById(R.id.postHeading);
        fundDate = findViewById(R.id.tv_eventPostDateData);
        fundAmount = findViewById(R.id.tv_eventPostDVenueData);
        fundDescription= findViewById(R.id.tv_eventPostDescriptData);
        contactName= findViewById(R.id.tv_eventPostContactNameData);
        contactNumber= findViewById(R.id.tv_eventPostContactNumberData);
        shareButton= findViewById(R.id.shareButton);


        Intent intent = getIntent();

        databaseSingleEvent = FirebaseDatabase.getInstance().getReference("funds").child(intent.getStringExtra(FundListActivity.FUND_ID));


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                fund = dataSnapshot.getValue(Fund.class);
                fundHeading.setText(fund.getFundName());
                fundDate.setText(fund.getFundDate());
                fundAmount.setText(fund.getFundAmount());
                fundDescription.setText(fund.getFundDescription());
                contactName.setText(fund.getFundContactName());
                contactNumber.setText(fund.getFundContactNumber());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        databaseSingleEvent.addValueEventListener(postListener);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareFund(fund.getFundName(), fund.getFundDate(),
                        fund.getFundAmount(), fund.getFundDescription(),
                        fund.getFundContactName(),fund.getFundContactNumber());
            }
        });
    }

//    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
//        try {
//            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//    int rDays = (int)getDateDiff(new SimpleDateFormat("dd/MM/yyyy"),)

    private boolean shareFund(String fundHeading, String fundDate,
                               String fundAmount, String fundDescription,
                               String contactName,String contactNumber) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,"Fund heading : "+fundHeading + "\n\n"
                                                    +"Fund date : " +fundDate + "\n\n"
                                                    +"Fund amount : " +fundAmount + "\n\n"
                                                    +"Fund description : " +fundDescription + "\n\n"
                                                    +"Contact name : " +contactName + "\n\n"
                                                    +"Contact number : " +contactNumber + "\n\n"
                                                    +"----Shared from HOPE app----");
        sendIntent.setType("text/plain");
        Intent.createChooser(sendIntent,"Share via");
        startActivity(sendIntent);
        return true;
    }
}