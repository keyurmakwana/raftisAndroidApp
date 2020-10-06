package com.keyur.raftis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class ImagesSliderAdapter extends SliderViewAdapter<SliderViewHolder> {
    Context context;
    int setTotalCount;
    String imageLink;

    public ImagesSliderAdapter(Context context,int setTotalCount) {
        this.setTotalCount=setTotalCount;
        this.context = context;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_layout,parent,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SliderViewHolder viewHolder, final int position) {
        //viewHolder.sliderImageView.setImageResource(imageSliderModelList.get(position).getImage());
        FirebaseDatabase.getInstance().getReference("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                switch (position){
                    case 0:
                        imageLink=snapshot.child("homepage1").getValue().toString();
                        Glide.with(viewHolder.itemView)
                                .load(imageLink)
                                .placeholder(R.drawable.loading)
                                .into(viewHolder.sliderImageView);
                        break;
                    case 1:
                        imageLink=snapshot.child("homepage2").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imageLink).placeholder(R.drawable.loading).into(viewHolder.sliderImageView);
                        break;
                    case 2:
                        imageLink=snapshot.child("homepage3").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imageLink).placeholder(R.drawable.loading).into(viewHolder.sliderImageView);
                        break;
                    case 3:
                        imageLink=snapshot.child("homepage4").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imageLink).placeholder(R.drawable.loading).into(viewHolder.sliderImageView);
                        break;
                    case 4:
                        imageLink=snapshot.child("homepage5").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imageLink).placeholder(R.drawable.loading).into(viewHolder.sliderImageView);
                        break;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getCount() {
        return setTotalCount;
    }
}
class SliderViewHolder extends SliderViewAdapter.ViewHolder {
    ImageView sliderImageView;
    View itemView;
    public SliderViewHolder(View itemView) {
        super(itemView);
        this.itemView=itemView;
        sliderImageView=itemView.findViewById(R.id.imageViewSlider);
    }
}
