package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.recycler.FilmAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReviewFragment extends Fragment implements OnItemListener{

    private FilmAdapter adapter;
    private int SortOption = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {

            final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            final String url ="http://192.168.1.103/mobile/progettoRecensioni.php?";
            final String urlImage ="http://192.168.1.103/mobile/imageRecensioni.php?";
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(final JSONArray response) {
                            int a = response.length();
                            FilmSingletonReview.getInstance().clearFilm();
                            adapter.notifyDataSetChanged();
                            for(int n = 0; n<a; n++){
                                JSONObject json = null;
                                try {
                                    json = response.getJSONObject(n);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                final JSONObject finalJson = json;
                                ImageRequest imagine = null;
                                try {
                                    imagine = new ImageRequest(urlImage+"id="+json.getString("id"), new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(final Bitmap bitmap) {
                                            try {
                                                FilmSingletonReview.getInstance().addFilm(new Film(bitmap, finalJson.getString("name"), finalJson.getString("genre"), finalJson.getInt("score"), finalJson.getString("review")));
                                                Collections.sort(FilmSingletonReview.getInstance().getFilm(), new Comparator<Film>() {
                                                    @Override
                                                    public int compare(Film o1, Film o2) {
                                                        if(o1.getTitle().charAt(0) > o2.getTitle().charAt(0)){
                                                            return 1;
                                                        }
                                                        else{
                                                            return -1;
                                                        }
                                                    }
                                                });
                                                adapter.notifyDataSetChanged();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, 1024, 1024, null, null);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                requestQueue.add(imagine);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            Toast.makeText(getActivity(), "ERRORE", Toast.LENGTH_SHORT).show();
                        }
                    });
            requestQueue.add(jsonArrayRequest);
        }
    }


    private void setRecyclerView(Activity activity){
        RecyclerView recyclerView = activity.findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
        }
        else{
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(layoutManager);
        }
        adapter = new FilmAdapter(FilmSingletonReview.getInstance().getFilm(),this);
        recyclerView.setAdapter(adapter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        if (savedInstanceState != null){
            adapter = new FilmAdapter((List<Film>) savedInstanceState.getSerializable("adapterReview"),this);
            adapter.notifyDataSetChanged();
        }

        MaterialToolbar tool = view.findViewById(R.id.toolbar);
        tool.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Sort by")
                        .setSingleChoiceItems(R.array.SortList,0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SortOption = which;
                            }
                        })
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(SortOption == 0){
                                    Collections.sort(FilmSingletonReview.getInstance().getFilm(), new Comparator<Film>() {
                                        @Override
                                        public int compare(Film o1, Film o2) {
                                            if(o1.getTitle().charAt(0) > o2.getTitle().charAt(0)){
                                                return 1;
                                            }
                                            else{
                                                return -1;
                                            }
                                        }
                                    });
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    Collections.sort(FilmSingletonReview.getInstance().getFilm(), new Comparator<Film>() {
                                        @Override
                                        public int compare(Film o1, Film o2) {
                                            if(o1.getScore() < o2.getScore()){
                                                return 1;
                                            }
                                            else{
                                                return -1;
                                            }
                                        }
                                    });
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        Toast.makeText(getActivity(), "Home", Toast.LENGTH_SHORT).show();
                        getActivity().startActivityForResult(new Intent(getActivity(), MainActivity.class), 1);
                        break;
                    case R.id.page_2:
                        Toast.makeText(getActivity(), "Reviews", Toast.LENGTH_SHORT).show();
                        getActivity().startActivityForResult(new Intent(getActivity(), ReviewActivity.class), 1);
                        break;
                }
                return true;
            }
        });
        Menu menu = bottomNavigationView.getMenu();
        MenuItem item = menu.getItem(1);
        item.setChecked(true);

        return view;
    }


    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity activity = getActivity();
        if (activity != null) {
            setRecyclerView(activity);
        } else{
            Log.e("LISTFRAGMENT", "activity null");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("adapterReview", (Serializable) FilmSingletonReview.getInstance().getFilm());
    }

    @Override
    public void onItemClick(int position) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            DetailsFragment detailsFragment = (DetailsFragment)
                    activity.getSupportFragmentManager().findFragmentById(R.id.layout_fragmentCard);
            if (detailsFragment != null) {
                detailsFragment.getFilmClicked(position,1);
            } else {
                DetailsFragment newFragment = new DetailsFragment();
                Bundle args = new Bundle();
                args.putInt("film_index", position);
                args.putInt("review",1);
                newFragment.setArguments(args);
                replaceFragment(newFragment);
            }
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container1, fragment);
// Commit the transaction
        transaction.commit();
    }

}
