package com.epayeats.epayeatsuser.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.epayeats.epayeatsuser.Fragment.About_Fragment;
import com.epayeats.epayeatsuser.Fragment.Account_Fragment;
import com.epayeats.epayeatsuser.Fragment.Cart_Fragment;
import com.epayeats.epayeatsuser.Fragment.Dashboard_Fragment;
import com.epayeats.epayeatsuser.Fragment.Logout_Fragment;
import com.epayeats.epayeatsuser.Fragment.MyOrders_Fragment;
import com.epayeats.epayeatsuser.Fragment.Search_Fragment;
import com.epayeats.epayeatsuser.Fragment.UserFeedback_Fragment;
import com.epayeats.epayeatsuser.R;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToogle;
    String currentFragment = "other";
    TextView headerEmail;

    SharedPreferences sharedPreferences;
    String a1;

    MeowBottomNavigation welcome_bottomnavigation;
    private static int HOME_ID = 1;
    private static int SEARCH_ID = 2;
    private static int ACCOUNT_ID = 3;
    private static int CART_ID = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.items);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerToogle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mDrawerToogle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));
        mDrawerLayout.addDrawerListener(mDrawerToogle);
        mDrawerToogle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerEmail = navigationView.getHeaderView(0).findViewById(R.id.header_email);
        sharedPreferences = getSharedPreferences("data", 0);
        a1 = sharedPreferences.getString("useremail", "");

        headerEmail.setText(a1);

        welcome_bottomnavigation = findViewById(R.id.welcome_bottomnavigation);
        welcome_bottomnavigation.add(new MeowBottomNavigation.Model(HOME_ID, R.drawable.ic_baseline_home_24));
        welcome_bottomnavigation.add(new MeowBottomNavigation.Model(SEARCH_ID, R.drawable.ic_baseline_search_24));
        welcome_bottomnavigation.add(new MeowBottomNavigation.Model(ACCOUNT_ID, R.drawable.ic_baseline_account_circle_24));
        welcome_bottomnavigation.add(new MeowBottomNavigation.Model(CART_ID, R.drawable.ic_shopping_cart));

        Dashboard_Fragment fragment = new Dashboard_Fragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment,"Home");
        fragmentTransaction.commit();

        welcome_bottomnavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item)
            {
                switch (item.getId())
                {
                    case 1:

                        Dashboard_Fragment fragment1 = new Dashboard_Fragment();
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.frame_layout, fragment1,"Dash");
                        fragmentTransaction1.commit();

                        break;

                    case 2:

                        Search_Fragment search_fragment1 = new Search_Fragment();
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frame_layout, search_fragment1,"Search");
                        fragmentTransaction2.commit();

                        break;

                    case 3:
                        Account_Fragment account_fragment = new Account_Fragment();
                        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.frame_layout, account_fragment,"Account");
                        fragmentTransaction3.commit();

                        break;

                    case 4:

                        Cart_Fragment fragment5 = new Cart_Fragment();
                        FragmentTransaction fragmentTransaction5 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction5.replace(R.id.frame_layout, fragment5,"Cart");
                        fragmentTransaction5.commit();

                        break;
                }
            }
        });
        welcome_bottomnavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

            }
        });

        welcome_bottomnavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
                welcome_bottomnavigation.show(HOME_ID,true);

            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.dashboard)
        {
            currentFragment = "other";
            Dashboard_Fragment fragment1 = new Dashboard_Fragment();
            FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.replace(R.id.frame_layout, fragment1,"Dash");
            fragmentTransaction1.commit();
        }
        else if (id == R.id.myorders)
        {
            currentFragment = "other";
            MyOrders_Fragment fragment= new MyOrders_Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,fragment,"My Order");
            fragmentTransaction.commit();
        }
        else if (id == R.id.mycart)
        {
            currentFragment = "other";
            Cart_Fragment fragment = new Cart_Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment,"Cart");
            fragmentTransaction.commit();
        }
        else if (id == R.id.feedback) {
            currentFragment = "other";
            UserFeedback_Fragment fragment = new UserFeedback_Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Feedback");
            fragmentTransaction.commit();
        }
        else if (id == R.id.about) {
            currentFragment = "other";
            About_Fragment fragment = new About_Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "About");
            fragmentTransaction.commit();

        }
        else if (id == R.id.logout)
        {
            currentFragment = "other";
            Logout_Fragment fragment = new Logout_Fragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment, "Logout");
            fragmentTransaction.commit();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (currentFragment != "home")
            {
                Dashboard_Fragment fragment1 = new Dashboard_Fragment();
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.frame_layout, fragment1,"Dash");
                fragmentTransaction1.commit();
            } else {
                super.onBackPressed();
            }
        }
    }


   @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.item_account:
                Account_Fragment account_fragment = new Account_Fragment();
                FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.frame_layout, account_fragment,"Account");
                fragmentTransaction3.commit();
                return true;

            case R.id.item_cart:
                 Cart_Fragment fragment = new Cart_Fragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment,"Cart");
                fragmentTransaction.commit();
                return true;

            case R.id.item_orders:
                MyOrders_Fragment fragment12= new MyOrders_Fragment();
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.frame_layout,fragment12,"My Order");
                fragmentTransaction1.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}