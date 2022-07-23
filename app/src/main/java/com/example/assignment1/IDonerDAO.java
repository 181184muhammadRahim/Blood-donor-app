package com.example.assignment1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;

public interface IDonerDAO {
    void save(Donor donor);
    void save(ArrayList<Donor> objects);
    ArrayList<Hashtable<String,String>> load();
    Hashtable<String,String> load(String id);
}
