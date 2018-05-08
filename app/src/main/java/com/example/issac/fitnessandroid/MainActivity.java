package com.example.issac.fitnessandroid;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;


    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;


    String currentUserID;
    private CircleImageView Profileimage;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // Profileimage = (CircleImageView) findViewById(R.id.action_pic);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Imagenes Perfil");
        final Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
        final TextView toolbar2 = (TextView) toolbar1.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar1);
        toolbar2.setText(toolbar1.getTitle());

        getSupportActionBar().setDisplayShowTitleEnabled(false);



        Profileimage = (CircleImageView) findViewById(R.id.imagenPerfil);

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("imagenPerfil").exists()) {
                        //Toast.makeText(MainActivity.this, "Si hay imagen", Toast.LENGTH_SHORT).show();
                        String imagen = dataSnapshot.child("imagenPerfil").getValue().toString();
                        Picasso.with(MainActivity.this).load(imagen).placeholder(R.drawable.profile).into(Profileimage);
                        //Picasso.with(MainActivity.this).load(image).placeholder(R.drawable.profile_icon).into(Profileimage);

                    }else{
                        Toast.makeText(MainActivity.this, "No hay imagen", Toast.LENGTH_SHORT).show();
                    }
                    if (dataSnapshot.child("fullname").getValue().toString()!=null){
                        toolbar2.setText(" "+ dataSnapshot.child("fullname").getValue().toString());
                    }else{
                        Toast.makeText(MainActivity.this, "No hay nombre", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Nueva rutina", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                SendUserToNewRutina();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_tutoriales:
                SendUserToTutorials();
                Toast.makeText(this, "Tutoriales", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.buscar_usuario:
                SendUserToSearch();
                Toast.makeText(this, "Búsqueda", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_account:
                SendUserToDetallesActivity();
                Toast.makeText(this, "Cuenta", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_logout:
                mAuth.signOut();
                SendUserToLoginActivity();
                Toast.makeText(this, "Salir", Toast.LENGTH_SHORT).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void SendUserToSearch() {
        Intent searchIntent = new Intent(MainActivity.this, SearchUsersActivity.class);
        startActivity(searchIntent);
    }

    private void SendUserToTutorials() {
        Intent tutorialsIntent = new Intent(MainActivity.this, VideoListActivity.class);
        startActivity(tutorialsIntent);
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    private void SendUserToAjustesActivity() {
        Intent ajustesIntent = new Intent(MainActivity.this, AjustesActivity.class);
        //ajustesIntent.addFlags(Intent.FL);
        startActivity(ajustesIntent);
        finish();
    }
    private void SendUserToDetallesActivity() {
        Intent ajustesIntent = new Intent(MainActivity.this, DetallesActivity.class);
        ajustesIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(ajustesIntent);
    }
    private void SendUserToNewRutina() {
        Intent ajustesIntent = new Intent(MainActivity.this, ShareActivity.class);
        ajustesIntent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
        startActivity(ajustesIntent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    Tab1Rutinas tab1= new Tab1Rutinas();
                    return tab1;
                case 1:
                    Tab2Dietas tab2= new Tab2Dietas();
                    return tab2;
                case 2:
                    Tab3Ejemplos tab3= new Tab3Ejemplos();
                    return tab3;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position){
            switch (position){
                case 0:
                    return "Seccion 1";
                case 1:
                    return "Seccion 2";
                case 2:
                    return "Seccion 3";
            }
            return null;
        }
    }
}
