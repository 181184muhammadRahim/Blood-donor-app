package com.example.assignment1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class DonorRegistry extends AppCompatActivity {
    private final String[] DonateStatus={"Ready to Donate","Can't Donate Now!"};
    private final String[] NumberVisibility={"Available","Hidden"};
    private final String[] BloodTypes={"A+","B+","AB+","O+","A-","B-","AB-","O-"};
    private Spinner DonateSelector;
    private Spinner VisibilitySelector;
    private Spinner BloodTypeSelector;
    private EditText NameField;
    private EditText DistrictField;
    private EditText PhoneField;
    private ArrayList<Donor> DonorList;
    private IDonerDAO dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NameField=(EditText) findViewById(R.id.NameField);
        DistrictField=(EditText) findViewById(R.id.DistrictField);
        PhoneField=(EditText) findViewById(R.id.PhoneField);
        DonateSelector = (Spinner) findViewById(R.id.DonationStatus);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, DonateStatus);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DonateSelector.setAdapter(adapter);
        VisibilitySelector = (Spinner) findViewById(R.id.NumberStatus);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,NumberVisibility);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        VisibilitySelector.setAdapter(adapter);
        BloodTypeSelector= (Spinner) findViewById(R.id.BgStatus);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,BloodTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BloodTypeSelector.setAdapter(adapter);
        dao=new DonerCloudDAO(new DonerCloudDAO.DataObserver() {
            @Override
            public void update() {
                DonorList=Donor.load(dao);
            }
        });
        /*dao=new DonerDBDAO(this);
        DonorList=Donor.load(dao);*/

    }
    public void ListPressed(View view){
        openSomeActivityForResult();
    }
    public void SubmitPressed(View view) {
        Integer result=isformfilled();
        if(result==0){
            Toast toast=Toast.makeText(this,"Success",Toast.LENGTH_SHORT);
            toast.show();
            Integer DonorOption=DonateSelector.getSelectedItemPosition();
            Integer PhoneOption=VisibilitySelector.getSelectedItemPosition();
            DonorList.add(new Donor(NameField.getText().toString(),DistrictField.getText().toString(),DonorOption,PhoneField.getText().toString(),PhoneOption,BloodTypes[BloodTypeSelector.getSelectedItemPosition()]));
            Donor donor=new Donor(NameField.getText().toString(),DistrictField.getText().toString(),DonorOption,PhoneField.getText().toString(),PhoneOption,BloodTypes[BloodTypeSelector.getSelectedItemPosition()],dao);
            donor.save();
        }else if(result==1){
            Toast toast=Toast.makeText(this,"Name field Empty",Toast.LENGTH_SHORT);
            toast.show();
        }else if(result==2){
            Toast toast=Toast.makeText(this,"District field Empty",Toast.LENGTH_SHORT);
            toast.show();
        }else if(result==3){
            Toast toast=Toast.makeText(this,"Phone field Empty",Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    private Integer isformfilled(){
        int result=0;
        if(this.NameField.getText().toString().compareTo("")==0){
            result=1;
        }
        if(this.DistrictField.getText().toString().compareTo("")==0){
            result=2;
        }
        if(this.PhoneField.getText().toString().compareTo("")==0){
            result=3;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        try{
            assert savedInstanceState!=null;
            DonorList=(ArrayList<Donor>) savedInstanceState.getSerializable("list");
        }catch (Exception e){
            Toast toast=Toast.makeText(this,"Problem in restoring state",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try{
            outState.putSerializable("list",DonorList);
        }catch (Exception e){
            Toast toast;
            toast=Toast.makeText(this,"Problem in saving state",Toast.LENGTH_LONG);
            toast.show();
        }
    }
    @SuppressWarnings("unchecked")
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data!=null;
                        DonorList=(ArrayList<Donor>) data.getSerializableExtra("list");
                    }
                }
            });
    public void openSomeActivityForResult() {
        Intent intent = new Intent(this, DonorList.class);
        intent.putExtra("list",DonorList);
        someActivityResultLauncher.launch(intent);
    }

}