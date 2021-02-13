package com.example.android.news;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<CustomObject> {

    public CustomAdapter(Context context, ArrayList<CustomObject> objectList){
        super(context, 0 , objectList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        CustomObject currentObject = getItem(position);

        TextView headlineTextView = (TextView)listItemView.findViewById(R.id.headline_id);
        headlineTextView.setText(getItem(position).getHeadline());

        TextView descriptionTextView = (TextView)listItemView.findViewById(R.id.description_id);
        descriptionTextView.setText(getItem(position).getDescription());

        TextView categoryTextView = (TextView)listItemView.findViewById(R.id.category_id);
        categoryTextView.setText(getItem(position).getCategory());

        TextView sectionTextView  = (TextView)listItemView.findViewById(R.id.section_id);
        sectionTextView.setText(getItem(position).getSection());

        ImageView imageView = (ImageView)listItemView.findViewById(R.id.image_id);
        Picasso.get().load(currentObject.getImageUrl()).into(imageView);


        return listItemView;

    }
}
