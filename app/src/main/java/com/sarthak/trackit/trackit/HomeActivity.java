package com.sarthak.trackit.trackit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    NavigationView navigationView;
    View navigationHeaderView;

    TextView mEmailVerifyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        launchMapsFragment();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationHeaderView = navigationView.getHeaderView(0);
        mEmailVerifyTv = navigationHeaderView.findViewById(R.id.nav_header_email_verification);

        mEmailVerifyTv.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else {

            FirebaseAuth.getInstance().getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {

                        mEmailVerifyTv.setText("Welcome");
                        mEmailVerifyTv.setClickable(false);
                    } else {

                        mEmailVerifyTv.setText("Not verified");
                        mEmailVerifyTv.setClickable(true);
                    }
                }
            });
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else {

            FirebaseAuth.getInstance().getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {

                        mEmailVerifyTv.setText("Welcome");
                        mEmailVerifyTv.setClickable(false);
                    } else {

                        mEmailVerifyTv.setText("Not verified");
                        mEmailVerifyTv.setClickable(true);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.nav_header_email_verification:

                String url = "http://www.example.com/verify?uid=" + FirebaseAuth.getInstance().getCurrentUser().getUid();
                ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                        .setUrl(url)
                        // The default for this is populated with the current android package name.
                        .setAndroidPackageName("com.sarthak.trackit.trackit", false, null)
                        .build();

                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(HomeActivity.this, "Email sent.", Toast.LENGTH_SHORT).show();
                                } else {

                                    Toast.makeText(HomeActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        launchSelectedFragment(item.getItemId());

        return true;
    }

    private void launchSelectedFragment(int itemId) {

        Fragment fragment = null;

        switch (itemId) {

            case R.id.nav_camera:

                fragment = new MapsFragment();
                break;

            case R.id.nav_gallery:

                fragment = new GroupsFragment();
                break;
        }

        //replacing the fragment
        if (fragment != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void launchMapsFragment() {

        launchSelectedFragment(R.id.nav_camera);
    }
}
