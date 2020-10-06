package com.keyur.raftis;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toolbar;


import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.keyur.raftis.NavigationActivities.ContactUs;
import com.keyur.raftis.NavigationActivities.ThankYou;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    CircleImageView scroll1,scroll2,scroll3,scroll4,scroll5;

    String url1="https://firebasestorage.googleapis.com/v0/b/raftis-386c1.appspot.com/o/homepage%2Fhome_scroll_man.jpg?alt=media&token=c8e7d058-6ef0-459e-8794-42f7cb2a3dee";
    String url2="https://firebasestorage.googleapis.com/v0/b/raftis-386c1.appspot.com/o/homepage%2Fhome_scroll_woman.jpg?alt=media&token=33bb9188-a753-4c15-aa18-bd5e387bfb67";
    String url3="https://firebasestorage.googleapis.com/v0/b/raftis-386c1.appspot.com/o/homepage%2Fhome_scroll_boy.jpg?alt=media&token=4c9624e6-c965-4871-88e9-dbf7d7867944";
    String url4="https://firebasestorage.googleapis.com/v0/b/raftis-386c1.appspot.com/o/homepage%2Fhome_scroll_girl.jpg?alt=media&token=3df98c99-9133-4a5b-9b7d-ab591f83cd9f";
    String url5="https://firebasestorage.googleapis.com/v0/b/raftis-386c1.appspot.com/o/homepage%2Fhome_scroll_wedding.jpg?alt=media&token=c6a59cf9-bb39-4d77-aac8-7573bd8fd9f8";

    RecyclerView featuredRecycler;

    SliderView sliderView;
    int setTotal;

    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference();
    DatabaseReference c1=databaseReference.child("cardImages/card1");
    DatabaseReference c2=databaseReference.child("cardImages/card2");
    DatabaseReference c3=databaseReference.child("cardImages/card3");
    DatabaseReference c4=databaseReference.child("cardImages/card4");
    DatabaseReference c5=databaseReference.child("cardImages/card5");
    DatabaseReference c6=databaseReference.child("cardImages/card6");

    ImageView cv1,cv2,cv3,cv4,cv5,cv6;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menuIcon;
    static final float END_SCALE=0.7f;
    ConstraintLayout content;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        sliderView=findViewById(R.id.imageSlider);

        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SCALE_DOWN);
        sliderView.setSliderTransformAnimation(SliderAnimations.ZOOMOUTTRANSFORMATION);
        FirebaseDatabase.getInstance().getReference("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long counts=snapshot.getChildrenCount();
                setTotal=counts.intValue();

                sliderView.setSliderAdapter(new ImagesSliderAdapter(HomePage.this,setTotal));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        scroll1=findViewById(R.id.scroll_image1);
        scroll2=findViewById(R.id.scroll_image2);
        scroll3=findViewById(R.id.scroll_image3);
        scroll4=findViewById(R.id.scroll_image4);
        scroll5=findViewById(R.id.scroll_image5);

        cv1=findViewById(R.id.card1);
        cv2=findViewById(R.id.card2);
        cv3=findViewById(R.id.card3);
        cv4=findViewById(R.id.card4);
        cv5=findViewById(R.id.card5);
        cv6=findViewById(R.id.card6);

        featuredRecycler=findViewById(R.id.home_featured_brand);

        featuredRecycler.setLayoutManager(new LinearLayoutManager(this));
        featuredRecycler.setAdapter(new RecyclerAdapter());
        featuredRecycler();


        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        menuIcon=findViewById(R.id.home_menu);
        content=findViewById(R.id.content);

        navigatioDrawer();


    }

    private void navigatioDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        animateNavigationDrawer();

    }

    private void animateNavigationDrawer() {
        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                content.setScaleX(offsetScale);
                content.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = content.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                content.setTranslationX(xTranslation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(),HomePage.class));
                break;
            case R.id.nav_contact_us:
                startActivity(new Intent(getApplicationContext(), ContactUs.class));
                break;
            case R.id.nav_thank_you:
                startActivity(new Intent(getApplicationContext(), ThankYou.class));
                break;
            case R.id.nav_share:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody="Hello raftis family is inviting you to download\n Raftis Android application \nPlease click on below link to download\n" +
                        getString(R.string.raftisUrl) +
                        "\nThank you for joining us" +
                        "\nWe hearty welcoming you" +
                        "\nFor any query reach to us:\n" +
                        getString(R.string.raftisEmail);
                String shareSub="Join Raftis family";
                intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(intent,"Share Using"));
                break;
        }
        return true;
    }

    private void featuredRecycler() {

        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));


    }

    @Override
    protected void onStart() {
        super.onStart();
        glide(url1,scroll1);
        glide(url2,scroll2);
        glide(url3,scroll3);
        glide(url4,scroll4);
        glide(url5,scroll5);

        card(c1,cv1);
        card(c2,cv2);
        card(c3,cv3);
        card(c4,cv4);
        card(c5,cv5);
        card(c6,cv6);


    }

    private void card(DatabaseReference c1, final ImageView cv1) {
        c1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link=snapshot.getValue(String.class);
                Glide.with(HomePage.this)
                        .load(link)
                        .placeholder(R.drawable.loading)
                        .into(cv1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void glide(String url,CircleImageView circleImageView) {

        Glide.with(HomePage.this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.loading)
                .into(circleImageView);

    }

}