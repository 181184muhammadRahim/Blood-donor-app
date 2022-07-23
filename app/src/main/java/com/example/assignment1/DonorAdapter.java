package com.example.assignment1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DonorAdapter extends ArrayAdapter<Donor> implements Filterable {
    private final String[] DonateStatus={"Ready to Donate","Can't Donate Now!"};
    private final String[] NumberVisibility={"Available","Hidden"};
    private Filter filter;
    private String value;
    private final ArrayList<Donor> DonorList;
    private ArrayList<Donor> FilteredDonors;
    public DonorAdapter(Context context,ArrayList<Donor> donor){
        super(context,0,donor);
        DonorList=donor;
        FilteredDonors=donor;
    }

    @Nullable
    @Override
    public Donor getItem(int position) {
        return FilteredDonors.get(position);
    }

    @Override
    public int getCount() {
        return FilteredDonors.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Donor donor=getItem(position);
        if(convertView == null){
            LayoutInflater inflater= LayoutInflater.from(getContext());
            convertView=inflater.inflate(R.layout.listdisplay,parent,false);
        }
        TextView BloodTypeView= convertView.findViewById(R.id.typelabel);
        assert donor != null;
        BloodTypeView.setText(donor.BloodGroup);
        TextView DonorView= convertView.findViewById(R.id.Donorinfo);
        DonorView.setText("📋 "+donor.Name+"\n"+"📍 "+donor.District+"\n"+"\uD83E\uDE78 "+DonateStatus[donor.DonateStatus]+"\n"+"\uD83D\uDCDE "+NumberVisibility[donor.PhoneVisibility]);
        DonorView.setTag(donor.PhoneNumber);
        ImageView callview=convertView.findViewById(R.id.Callview);
        callview.setTag(donor.PhoneNumber);
        return convertView;
    }

    @NonNull
    public Filter getFilter(String val) {
        if(filter==null){
            filter=new DonorFilter();
        }
        this.value=val;
        return filter;
    }
    private class DonorFilter extends Filter{
        Boolean includeResult(int index,CharSequence cs){
            if(value.compareTo("Name")==0){
                return DonorList.get(index).Name.contains(cs);
            }else if(value.compareTo("District")==0){
                return DonorList.get(index).District.contains(cs);
            }else if(value.compareTo("Name&District")==0){
                return DonorList.get(index).Name.contains(cs) || DonorList.get(index).District.contains(cs);
            }else if(value.compareTo("Blood Group")==0){
                return DonorList.get(index).BloodGroup.contains(cs);
            }
            return false;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results=new FilterResults();
            if(charSequence !=null && charSequence.length()>0 ){
                ArrayList<Donor> FilteredValues=new ArrayList<>();
                for (int i = 0; i < FilteredDonors.size(); i++) {

                    if(includeResult(i,charSequence)){
                        FilteredValues.add(DonorList.get(i));
                    }
                }
                results.values=FilteredValues;
                results.count=FilteredValues.size();
            }else{
                results.values= DonorList;
                results.count=DonorList.size();
            }
            return results;
        }
        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            FilteredDonors=(ArrayList<Donor>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
