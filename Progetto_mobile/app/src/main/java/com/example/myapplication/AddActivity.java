package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class AddActivity extends AppCompatActivity {
        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Bundle bundle = getIntent().getExtras();
        Bundle args = new Bundle();
        int position = bundle.getInt("film_index");
        args.putInt("film_index", position);
        Fragment AddFragment = new AddFragment();
        AddFragment.setArguments(args);
        replaceFragment(AddFragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity(1);
    }

    private void replaceFragment (Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container2, fragment);
// Commit the transaction
        transaction.commit();
    }
}

