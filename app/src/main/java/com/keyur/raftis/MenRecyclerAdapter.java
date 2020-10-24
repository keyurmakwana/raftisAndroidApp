package com.keyur.raftis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenRecyclerAdapter extends RecyclerView.Adapter<MenRecyclerAdapter.RecyclerViewAdapater> {
    String imageLink;

    @NonNull
    @Override
    public RecyclerViewAdapater onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.featured_card_design,parent,false);
        return new RecyclerViewAdapater(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapater holder, final int position) {
        FirebaseDatabase.getInstance().getReference("brand").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                switch (position){
                    case 0:
                        imageLink=snapshot.child("brand1").getValue().toString();
                        Glide.with(holder.itemView)
                                .load(imageLink)
                                .placeholder(R.drawable.loading)
                                .into(holder.imageView);
                        break;
                    case 1:
                        imageLink=snapshot.child("brand2").getValue().toString();
                        Glide.with(holder.itemView).load(imageLink).placeholder(R.drawable.loading).into(holder.imageView);
                        break;
                    case 2:
                        imageLink=snapshot.child("brand3").getValue().toString();
                        Glide.with(holder.itemView).load(imageLink).placeholder(R.drawable.loading).into(holder.imageView);
                        break;
                    case 3:
                        imageLink=snapshot.child("brand4").getValue().toString();
                        Glide.with(holder.itemView).load(imageLink).placeholder(R.drawable.loading).into(holder.imageView);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class RecyclerViewAdapater extends RecyclerView.ViewHolder{
        ImageView imageView;
        public RecyclerViewAdapater(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.fcd_image);
        }
    }

}
