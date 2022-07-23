package com.example.assignment1;

import android.os.PersistableBundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public class Donor implements Serializable {
    public final String Name;
    public final String District;
    public Integer DonateStatus;
    public final String PhoneNumber;
    public Integer PhoneVisibility;
    public final String BloodGroup;
    private IDonerDAO dao = null;
    public Donor(String name,String district,Integer donatestatus,String phone,Integer phoneStatus,String BloodGroup,IDonerDAO D){
        this.Name=name;
        this.District=district;
        this.DonateStatus=donatestatus;
        this.PhoneNumber=phone;
        this.PhoneVisibility=phoneStatus;
        this.BloodGroup=BloodGroup;
        this.dao=D;
    }
    public Donor(String name,String district,Integer donatestatus,String phone,Integer phoneStatus,String BloodGroup){
        this.Name=name;
        this.District=district;
        this.DonateStatus=donatestatus;
        this.PhoneNumber=phone;
        this.PhoneVisibility=phoneStatus;
        this.BloodGroup=BloodGroup;
        this.dao=null;
    }
    public void save(){
        if (dao != null){
            dao.save(this);
        }
    }
    public void setdao(IDonerDAO d){
        dao=d;
    }
    public static ArrayList<Donor> load(IDonerDAO dao){
        ArrayList<Donor> donorlist = new ArrayList<>();
        if(dao != null){
            ArrayList<Hashtable<String,String>> objects = dao.load();
            for(Hashtable<String,String> obj : objects){
                Donor donor = new Donor(obj.get("name"),obj.get("district"),Integer.parseInt(obj.get("donatestatus")),obj.get("phonenumber"),Integer.parseInt(obj.get("phonevisibility")),obj.get("bloodgroup"));
                donorlist.add(donor);
            }
        }
        return donorlist;
    }
    public void ChangeDonateStatus(Integer value){
        this.DonateStatus=value;
    }
    public void ChangePhoneVisibility(Integer value){
        this.PhoneVisibility=value;
    }
}
