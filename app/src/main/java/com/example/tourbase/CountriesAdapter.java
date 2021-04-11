package com.example.tourbase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.*;

class CountriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<String> country;
    Context context;
    public CountriesAdapter(Context context, List<String> temp)
    {
        this.context = context;
        country = temp;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        View view = holder.itemView;
        String item = country.get(position);
        TextView textView = view.findViewById(R.id.text_view_user_name);
        textView.setText(item);
        ImageView imageView = view.findViewById(R.id.image_view_icon);
        imageView.setVisibility(GONE);
    }

    @Override
    public int getItemCount() {
        return country.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public List<String> getList()
    {
        return country;
    }
}