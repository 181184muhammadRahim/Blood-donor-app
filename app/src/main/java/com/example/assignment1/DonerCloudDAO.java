package com.example.assignment1;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

public class DonerCloudDAO implements IDonerDAO{
    private Context ctx;
    private FirebaseFirestore db;
    public interface DataObserver{
        public void update();
    }
    private ArrayList<Hashtable<String,String>> data;
    public DonerCloudDAO(DataObserver observer){
        db=FirebaseFirestore.getInstance();
        data = new ArrayList<>();
        db.collection("Donors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> obj = new HashMap<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                obj=document.getData();
                                Hashtable<String,String> dataitem=converter(obj);
                                data.add(dataitem);
                            }
                            observer.update();
                        }else{
                            Log.w("Failure","Error in fetching document", task.getException());
                        }
                    }
                });
    }
    private Hashtable<String,String> converter(Map<String ,Object> obj){
        Hashtable<String,String> data=new Hashtable<>();
        for (String i : obj.keySet()) {
            data.put(i.toLowerCase(),obj.get(i).toString());
        }
        return data;
    }
    @Override
    public void save(Donor donor) {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("Name",donor.Name);
        user.put("BloodGroup", donor.BloodGroup);
        user.put("District", donor.District);
        user.put("DonateStatus",donor.DonateStatus);
        user.put("PhoneNumber",donor.PhoneNumber);
        user.put("PhoneVisibility",donor.PhoneVisibility);


// Add a new document with a generated ID
        db.collection("Donors")
                .add(user)
                .addOnSuccessListener(documentReference -> Log.d("Success", "DocumentSnapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Failure","Error adding document", e);
                    }
                });
    }

    @Override
    public void save(ArrayList<Donor> objects) {
        for (Donor obj: objects){
            save(obj);
        }
    }

    @Override
    public ArrayList<Hashtable<String, String>> load() {
       if(data==null){
           data = new ArrayList<Hashtable<String,String>>();
       }
       return data;
    }

    @Override
    public Hashtable<String, String> load(String id) {
        return null;
    }
}
