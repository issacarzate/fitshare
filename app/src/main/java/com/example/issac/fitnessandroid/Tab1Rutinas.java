package com.example.issac.fitnessandroid;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by issac on 03/02/18.
 */

public class Tab1Rutinas extends Fragment{
    private DatabaseReference Rutinasref;

    private ArrayList<String> strings;
    private RecyclerView postList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Rutinasref = FirebaseDatabase.getInstance().getReference().child("Rutinas");

        View rootView = inflater.inflate(R.layout.tab1, container, false);

        postList = (RecyclerView) rootView.findViewById(R.id.rvRutinas);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        postList.setLayoutManager(linearLayoutManager);

        DisplayAllUsersPosts();

        return rootView;
    }

    private void DisplayAllUsersPosts() {
        FirebaseRecyclerAdapter<Rutinas, RutinasViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Rutinas, RutinasViewHolder>(
                        Rutinas.class,
                        R.layout.rutinas_layout,
                        RutinasViewHolder.class,
                        Rutinasref
                ) {
                    @Override
                    protected void populateViewHolder(RutinasViewHolder viewHolder, Rutinas model, int position) {
                        final String PostKey = getRef(position).getKey();

                        viewHolder.setFullname(model.getFullname());
                        viewHolder.setTiempo(model.getTiempo());
                        viewHolder.setDate(model.getDate());
                        viewHolder.setDescripcion(model.getDescripcion());
                        viewHolder.setImagenPerfil(getContext(), model.getImagenPerfil());
                        viewHolder.setImagenRutina(getContext(), model.getImagenRutina());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(getActivity().getApplicationContext(), PostKey, Toast.LENGTH_SHORT).show();
                                Intent clickPostIntent = new Intent(getActivity().getApplicationContext(), ClickPostActivity.class);
                                clickPostIntent.putExtra("PostKey", PostKey);
                                startActivity(clickPostIntent);
                            }
                        });
                    }
                };
        postList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class RutinasViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public RutinasViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setFullname(String fullname){
            TextView username = (TextView) mView.findViewById(R.id.rutina_user_name);
            username.setText(fullname);
        }
        public void setImagenPerfil(Context ctx, String imagenPerfil){
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.rutina_profile_image);
            Picasso.with(ctx).load(imagenPerfil).into(image);
        }
        public void setTiempo(String tiempo){
            TextView postTime = (TextView) mView.findViewById(R.id.rutina_time);
            postTime.setText(tiempo);
        }
        public void setDate(String date){
            TextView postDate = (TextView) mView.findViewById(R.id.rutina_date);
            postDate.setText(date);
        }
        public void setDescripcion(String descripcion){
            TextView postDescripcion = (TextView) mView.findViewById(R.id.rutina_descripcion);
            postDescripcion.setText(descripcion);
        }
        public void setImagenRutina(Context ctx, String imagenRutina){
            ImageView RImage = (ImageView) mView.findViewById(R.id.rutina_imagen);
            Picasso.with(ctx).load(imagenRutina).into(RImage);
        }

    }

}