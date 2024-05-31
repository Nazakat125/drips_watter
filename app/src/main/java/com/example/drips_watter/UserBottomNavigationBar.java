package com.example.drips_watter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserBottomNavigationBar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bottom_navigation_bar);
        String name = getIntent().getStringExtra("name");
        String number = getIntent().getStringExtra("number");
        String image = getIntent().getStringExtra("image");
        String token = getIntent().getStringExtra("token");
        String balance = getIntent().getStringExtra("balance");

        BottomNavigationView bottomNav = findViewById(R.id.user_bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Pass data to fragments when creating them
        Fragment homeFragment = new UserHome();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("number", number);
        bundle.putString("image", image);
        bundle.putString("token", token);
        bundle.putString("balance", balance);
        homeFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.user_container,
                homeFragment).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    if(item.getItemId() == R.id.user_home_item){
                        selectedFragment = new UserHome();
                    } else if(item.getItemId() == R.id.user_package_item){
                        selectedFragment = new UserPacageDetail();
                    }

                    if (selectedFragment != null) {
                        // Pass data to fragments when creating them
                        Bundle bundle = new Bundle();
                        bundle.putString("name", getIntent().getStringExtra("name"));
                        bundle.putString("number", getIntent().getStringExtra("number"));
                        bundle.putString("image", getIntent().getStringExtra("image"));
                        bundle.putString("token", getIntent().getStringExtra("token"));
                        bundle.putString("balance", getIntent().getStringExtra("balance"));
                        selectedFragment.setArguments(bundle);

                        getSupportFragmentManager().beginTransaction().replace(R.id.user_container,
                                selectedFragment).commit();
                        return true;
                    }
                    return false;
                }
            };
}
