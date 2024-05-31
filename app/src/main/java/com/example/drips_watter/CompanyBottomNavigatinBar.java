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

public class CompanyBottomNavigatinBar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_bottom_navigatin_bar);

        String name = getIntent().getStringExtra("name");
        String number = getIntent().getStringExtra("number");
        String image = getIntent().getStringExtra("image");
        String token = getIntent().getStringExtra("token");

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        BottomNavigationView bottomNav = findViewById(R.id.company_bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Pass data to fragments when creating them
        Fragment homeFragment = new CompanyHome();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("number", number);
        bundle.putString("image", image);
        bundle.putString("token", token);
        homeFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.company_container,
                homeFragment).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    if(item.getItemId() == R.id.company_home_item){
                        selectedFragment = new CompanyHome();
                    } else if(item.getItemId() == R.id.company_package_item){
                        selectedFragment = new CompanyOrderDetail();
                    }

                    if (selectedFragment != null) {
                        // Pass data to fragments when creating them
                        Bundle bundle = new Bundle();
                        bundle.putString("name", getIntent().getStringExtra("name"));
                        bundle.putString("number", getIntent().getStringExtra("number"));
                        bundle.putString("image", getIntent().getStringExtra("image"));
                        bundle.putString("token", getIntent().getStringExtra("token"));
                        selectedFragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.company_container,
                                selectedFragment).commit();
                        return true;
                    }
                    return false;
                }
            };
}
