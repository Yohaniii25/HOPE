package com.example.artists;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artists.adapters.FundList;
import com.example.artists.model.Fund;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FundListActivity extends AppCompatActivity {

    //we will use these constants later to pass the artist name and id to another activity
    public static final String FUND_NAME = "com.example.artists.eventName";
    public static final String FUND_ID = "com.example.artists.eventId";
    public static final String USER_ID = "com.example.artists.userId";

    TextView textViewFund;
    Button btnCreateFund;
    ListView listFunds;


    //a list to store all the artist from firebase database
    List<Fund> funds;

    //our database reference object
    DatabaseReference databaseFunds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_list);

        databaseFunds = FirebaseDatabase.getInstance().getReference("funds");

        /*
         * this line is important
         * this time we are not getting the reference of a direct node
         * but inside the node track we are creating a new child with the artist id
         * and inside that node we will store all the tracks with unique ids
         * */

        textViewFund = findViewById(R.id.textViewEvent);
        btnCreateFund =  findViewById(R.id.buttonAddFund);
        listFunds = findViewById(R.id.listViewFunds);

        //list to store funds
        funds = new ArrayList<>();

        listFunds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected fund
                Fund fund = funds.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), SinglePostActivity.class);

                //putting fund name and id to intent
                intent.putExtra(FUND_ID, fund.getFundId());
                intent.putExtra(FUND_NAME, fund.getFundName());

                //starting the activity with intent
                startActivity(intent);
            }
        });

        listFunds.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fund fund = funds.get(i);
                showOptionMenu(fund.getFundId(), fund.getFundName(),
                        fund.getFundDate(),fund.getFundDescription(),
                        fund.getFundAmount(),fund.getFundCategory(),
                        fund.getFundContactName(),fund.getFundContactNumber(),
                        fund.getFundContactEmail(),fund.getFundContactNIC());
                return true;
            }
        });

        btnCreateFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating an intent
                Intent intent = new Intent(FundListActivity.this, CreateFundActivity.class);

                //putting fund name and id to intent
                intent.putExtra(USER_ID,USER_ID);


                //starting the activity with intent
                startActivity(intent);
            }
        });


    }

    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseFunds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous fund list
                funds.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting fund
                    Fund fund = postSnapshot.getValue(Fund.class);
                    //adding fund to the list
                    funds.add(fund);
                }

                //creating adapter
                FundList fundAdapter = new FundList(FundListActivity.this, funds);
                //attaching adapter to the listview
                listFunds.setAdapter(fundAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showOptionMenu(final String fundId, String fundName,String fundDate,
                                String fundDescr, String fundAmount, String fundCategory,
                                String fundContactName, String fundContactNumber,
                                String fundContactEmail, String fundContactNIC) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.fund_list_menu, null);
        dialogBuilder.setView(dialogView);

        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateFund);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteFund);
        final Button buttonShare = (Button) dialogView.findViewById(R.id.buttonShareFund);

        dialogBuilder.setTitle(fundName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showUpdateMenu(fundId);
                b.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                showDeleteDialog(fundId,fundName);
                b.dismiss();
            }
        });

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareFund(fundName,fundDate,fundAmount,fundCategory,fundContactName,
                        fundContactNumber);
                b.dismiss();
            }
        });
    }

    private void showDeleteDialog(final String fundId, String fundName){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setTitle("Are you Sure to delete?");
        dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteFund(fundId);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        final AlertDialog b = dialogBuilder.create();
        b.show();


    }

    private void showUpdateMenu(final String fundId){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_fund_layout, null);
        dialogBuilder.setView(dialogView);

        final EditText fund_Name = (EditText) dialogView.findViewById(R.id.et_fundName);
        final EditText fund_Date = (EditText) dialogView.findViewById(R.id.et_fundDate);
        final EditText fund_Amount = (EditText) dialogView.findViewById(R.id.et_fundAmount);
        final EditText fund_Descr = (EditText) dialogView.findViewById(R.id.etml_fundDescription);
        final EditText fund_ContactName = (EditText) dialogView.findViewById(R.id.et_contact_name);
        final EditText fund_ContactNumber = (EditText) dialogView.findViewById(R.id.et_contact_phone);
        final EditText fund_ContactNic= (EditText) dialogView.findViewById(R.id.et_contactNic);
        final EditText fund_ContactEmail = (EditText) dialogView.findViewById(R.id.et_contactEmail);
        final Spinner fund_Category = (Spinner) dialogView.findViewById(R.id.sp_fundCategory);

        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.btn_updateFund);


        DatabaseReference fundRef = FirebaseDatabase.getInstance().getReference("funds").child(fundId);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Fund fund = dataSnapshot.getValue(Fund.class);
                fund_Name.setText(fund.getFundName());
                fund_Date.setText(fund.getFundDate());
                fund_Amount.setText(fund.getFundAmount());
                fund_Descr.setText(fund.getFundDescription());
                fund_ContactName.setText(fund.getFundContactName());
                fund_ContactNumber.setText(fund.getFundContactNumber());
                fund_ContactNic.setText(fund.getFundContactNIC());
                fund_ContactEmail.setText(fund.getFundContactEmail());

                String eventCategory = fund.getFundCategory();
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FundListActivity.this, R.array.fundCategories, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fund_Category.setAdapter(adapter);
                if (eventCategory != null) {
                    int spinnerPosition = adapter.getPosition(eventCategory);
                    fund_Category.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        fundRef.addValueEventListener(postListener);


        // Add the buttons
        dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                String fund_name =fund_Name.getText().toString().trim();
                String fund_date = fund_Date.getText().toString().trim();
                String fund_amount = fund_Amount.getText().toString().trim();
                String fund_description = fund_Descr.getText().toString().trim();
                String fund_contact_name = fund_ContactName.getText().toString().trim();
                String fund_contact_number = fund_ContactNumber.getText().toString().trim();
                String fund_contact_mail = fund_ContactNic.getText().toString().trim();
                String fund_contact_nic= fund_ContactEmail.getText().toString().trim();
                String fund_category = fund_Category.getSelectedItem().toString();

                updateFund(fundId,fund_name,fund_date,fund_amount,fund_description,
                        fund_contact_name,fund_contact_number,fund_contact_mail,
                        fund_contact_nic,fund_category);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });

        LayoutInflater inflater1 = getLayoutInflater();
        final View updateHeading = inflater1.inflate(R.layout.update_heading, null);
        dialogBuilder.setCustomTitle(updateHeading);
        final AlertDialog b = dialogBuilder.create();
        b.show();

    }

    private boolean updateFund(String fundId,String fund_name,String fund_date,
                                String fund_amount,String fund_description, String fund_contact_name,
                                String fund_contact_number,String fund_contact_mail,
                                String fund_contact_nic,String fund_category) {
        //getting the specified fund reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("funds").child(fundId);

        //updating fund
        Fund fund = new Fund(fundId,fund_name,fund_date,fund_amount,fund_description,
                fund_category,fund_contact_name,fund_contact_number,fund_contact_mail,
                fund_contact_nic);
        dR.setValue(fund);
        Toast.makeText(getApplicationContext(), "Fund Updated", Toast.LENGTH_LONG).show();
        return true;
    }


    private boolean deleteFund(String id) {
        //getting the specified fund reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("funds").child(id);
        //removing fund
        dR.removeValue();
        Toast.makeText(getApplicationContext(), "Fund Deleted", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean shareFund(String fundHeading, String fundDate,
                               String fundAmount, String fundDescription,
                               String contactName,String contactNumber) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,"Event heading : "+fundHeading + "\n\n"
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