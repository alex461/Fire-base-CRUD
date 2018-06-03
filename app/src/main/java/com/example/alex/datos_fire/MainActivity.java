package com.example.alex.datos_fire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String ARTIST_NODE = "Artists";
    private static final String TAG ="main Activity" ;
    private DatabaseReference databaseReference;
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private List<String> artistsname;
    private List<Artist>  artists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.lstArtist);
        artistsname = new ArrayList<>();
        artists = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,artistsname);
        listView.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(ARTIST_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                artistsname.clear();
                artists.clear();
                if (dataSnapshot.exists()){
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Artist artist = snapshot.getValue(Artist.class);
                        Log.w(TAG,"Artist Name: "+artist.getName());
                        artistsname.add(artist.getName());
                        artists.add(artist);
                    }


                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String id_artist = artists.get(position).getId();
                artists.remove(position);
                databaseReference.child(ARTIST_NODE).child(id_artist).removeValue();

                return true;
            }
        });
    }

    public void createArtist(View view){

        Artist artist = new Artist(databaseReference.push().getKey(),"garbage","rock");
        databaseReference.child(ARTIST_NODE).child(artist.getId()).setValue(artist);
    }
}
