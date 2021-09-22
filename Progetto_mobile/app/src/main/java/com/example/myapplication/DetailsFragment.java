package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class DetailsFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_card, container, false);

        Button back = view.findViewById(R.id.back);
        Button Option = view.findViewById(R.id.add);
        Film film = null;

        final TextView title = view.findViewById(R.id.detail_title);
        final TextView genre = view.findViewById(R.id.detail_genre);
        final TextView score = view.findViewById(R.id.detail_score);
        final TextView yourScore = view.findViewById(R.id.score_number);
        final TextView synopsis = view.findViewById(R.id.synopsis);
        final TextView review = view.findViewById(R.id.detail_synopsis);
        final ImageView image = view.findViewById(R.id.imageView);
        final Bundle bundle = getArguments();
        int position = bundle.getInt("film_index");
        int IsReview = bundle.getInt("review");
        if (bundle != null) {
            film = getFilmClicked(position,IsReview);
            image.setImageBitmap(film.getImage());
            title.setText(film.getTitle());
            genre.setText(film.getGenre());
            score.setText(String.valueOf(film.getScore()));
            synopsis.setText(film.getSynopsis());
        }

        if(IsReview == 1){
            Option.setText("Delete");
            review.setText("Your Review");
            yourScore.setText("Your Score:");

            final Film finalFilm = film;
            Option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("Delete this review?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                    // Enter the correct url for your api service site
                                    String url = "http://192.168.1.103/mobile/deleteRecensioni.php?";
                                    StringRequest StringRequest = new StringRequest(Request.Method.POST, url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    FilmSingletonReview.getInstance().getFilm().remove(finalFilm);
                                                    Toast.makeText(getActivity(), "data deleted", Toast.LENGTH_SHORT).show();
                                                    FragmentManager fm = getActivity().getSupportFragmentManager();
                                                    fm.popBackStack();
                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getActivity(), "delete error", Toast.LENGTH_SHORT).show();
                                        }
                                    }){
                                        @Override
                                        protected Map<String,String> getParams(){
                                            Map<String,String> params = new HashMap<String, String>();
                                            params.put("name", finalFilm.getTitle());
                                            params.put("review",finalFilm.getSynopsis());
                                            params.put("score",String.valueOf(finalFilm.getScore()));
                                            return params; } };
                                    requestQueue.add(StringRequest);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }
            });
        }

        else{
            Option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddActivity.class);
                    intent.putExtra("film_index",bundle.getInt("film_index"));
                    getActivity().startActivityForResult(intent, 1);
                }
            });
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });

        return view;
    }

    Film getFilmClicked(int film_index, int IsReview){
        if(IsReview == 1) {
            return FilmSingletonReview.getInstance().getFilm().get(film_index);
        }
        else {
            return FilmSingleton.getInstance().getFilm().get(film_index);
        }
    }

}
