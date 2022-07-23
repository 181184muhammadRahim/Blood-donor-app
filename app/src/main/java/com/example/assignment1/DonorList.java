package com.example.assignment1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DonorList extends AppCompatActivity {
    private final String[] SearchOptions={"Name","District","Name&District","Blood Group"};
    private ArrayList<Donor> DonorList;
    private ListView list_display;
    private TextView search_bar;
    private Spinner search_option;
    private DonorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_donor_list);
        Intent intent=getIntent();
        DonorList=(ArrayList<Donor>) intent.getSerializableExtra("list");
        if(DonorList==null){
            DonorList= new ArrayList<>();
        }
        search_bar=findViewById(R.id.search_bar);
        list_display=findViewById(R.id.listMode);
        search_option=findViewById(R.id.search_option);
        ArrayAdapter<CharSequence> search_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SearchOptions);
        search_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        search_option.setAdapter(search_adapter);
        adapter=new DonorAdapter(this,DonorList);
        list_display.setAdapter(adapter);
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Filter fil=adapter.getFilter(SearchOptions[search_option.getSelectedItemPosition()]);
                fil.filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public void smsLauncher(View view){
        TextView v=(TextView) view;
        String number=(String) v.getTag();
        Uri uri = Uri.parse("smsto:"+number);
        Intent smsintent = new Intent(Intent.ACTION_SENDTO, uri);
        smsintent.putExtra("sms_body", "I need blood!!!");
        try {
            startActivity(smsintent);
        } catch (ActivityNotFoundException e) {
            Toast toast=Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT);
        }
    }
    public void callLauncher(View view){
        ImageView v=(ImageView) view;
        String number=(String) v.getTag();
        Uri uri = Uri.parse("tel:"+number);
        Intent callIntent = new Intent(Intent.ACTION_DIAL, uri);
        try {
            startActivity(callIntent);
        } catch (ActivityNotFoundException e) {
            Toast toast=Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT);
        }
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
    @Override
    public void onBackPressed() {
        Intent intent =new Intent();
        intent.putExtra("list",DonorList);
        setResult(RESULT_OK,intent);
        super.onBackPressed();
    }
}