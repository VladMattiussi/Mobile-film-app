package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        final Activity activity = getActivity();
        final ImageView image = view.findViewById(R.id.imageView);
        final TextView title = view.findViewById(R.id.detail_title);
        final EditText review = view.findViewById(R.id.review);
        final EditText score = view.findViewById(R.id.score1);
        final Button back = view.findViewById(R.id.back1);
        Button add = view.findViewById(R.id.confirm);
        Bundle bundle = getArguments();
            final Film film = getFilmClicked(bundle.getInt("film_index"));
            title.setText(film.getTitle());
            image.setImageBitmap(film.getImage());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int myScore = 0;
                try {
                    myScore = Integer.parseInt(score.getText().toString());
                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }
                if (myScore < 1 || myScore >= 11) {
                    Toast.makeText(getActivity(), "Please insert score value between 1 and 10", Toast.LENGTH_LONG).show();
                } else {
                    final Film film1 = new Film(film.getImage(), film.getTitle(), film.getGenre(), myScore, review.getText().toString());

                    Bitmap img = film1.getImage();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 90, bos);
                    byte[] bArray = bos.toByteArray();
                    final String encodedImage = Base64.encodeToString(bArray, Base64.DEFAULT);

                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    // Enter the correct url for your api service site
                    String url = "http://192.168.1.103/mobile/recensioni.php?";
                    StringRequest StringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getActivity(), "data uploaded", Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "upload error", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("name", film1.getTitle());
                            params.put("review", film1.getSynopsis());
                            params.put("score", String.valueOf(film1.getScore()));
                            params.put("genre", film1.getGenre());
                            params.put("image", encodedImage);

                            return params;
                        }
                    };
                    requestQueue.add(StringRequest);
//set the result of the intent and terminate the activity
                    activity.setResult(1);
                    activity.finish();
                }
            }
        });

        return view;
    }

    Film getFilmClicked(int film_index){
        return FilmSingleton.getInstance().getFilm().get(film_index);
    }

}
