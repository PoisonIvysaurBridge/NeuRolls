package com.werelit.neurolls.neurolls;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link Fragment} that displays a list of number vocabulary words.
 */
public class NumbersFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    /** The list containing Entertainment objects */               private List<Entertainment> entertainments = new ArrayList<>();
    /** The recycler view containing the Entertainment items */    private RecyclerView mRecyclerView;
    /** The adapter used for the recycler view */               private EntertainmentAdapter mAdapter;
    /** The layout manager for the recycler view */             private RecyclerView.LayoutManager mLayoutManager;
    /** The layout for the snackbar with undo delete */         private ConstraintLayout constraintLayout;
    /** TextView that is displayed when the list is empty */    private TextView mEmptyStateTextView;


    /** Handles playback of all the sound files */
    private MediaPlayer mMediaPlayer;

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.entertainment_roll, container, false);


        // use a constraint layout for the delete snackbar with UNDO
        constraintLayout = rootView.findViewById(R.id.constraint_layout);

        // set visibility of the empty view to be GONE initially
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        mEmptyStateTextView.setVisibility(View.GONE);

        // setup the recycler view adapter, layout, etc.
        prepareRecyclerView(rootView);

        // add Entertainment items into the Entertainments list
        prepareEntertainments();

        // prepare the buttons in the UI
        prepareButtons(rootView);


        return rootView;
    }

    /**
     * This method setups the recycler view
     * - setting the adapter
     * - setting the layout of the recycler view
     * - adding an item touch listener, etc.
     */
    public void prepareRecyclerView(View rootView){
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // This draws a line separator for each row, but card views are used so no need for this
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        // specify an adapter (see also next example)
        mAdapter = new EntertainmentAdapter(entertainments);
        mRecyclerView.setAdapter(mAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
    }

    /**
     * This method adds the dummy Entertainment data into the Entertainments list.
     */
    private void prepareEntertainments() {
        entertainments.add(new Entertainment("Pericos", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Entertainment("La Casita @ 6th Andrew", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Entertainment("La Casita @ 2nd Razon", "Canteen @ Razon building DLSU", 3));
        entertainments.add(new Entertainment("first resto", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Entertainment("second resto", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Entertainment("third resto", "Canteen @ Razon building DLSU", 3));
        entertainments.add(new Entertainment("fourth resto", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Entertainment("5th rest", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Entertainment("6th resto", "Canteen @ Razon building DLSU", 3));
        entertainments.add(new Entertainment("seventh resto", "Canteen @ LS building DLSU", 5));
        entertainments.add(new Entertainment("eighth resto", "Canteen @ Andrew building DLSU", 9));
        entertainments.add(new Entertainment("9th", "Canteen @ Razon building DLSU", 3));
    }

    /**
     * This method setups the buttons to be displayed in the Entertainment activity UI
     */
    public void prepareButtons(View rootView){

        // ADD Button to go to add a new Entertainment activity
        Button add = (Button) rootView.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                //startActivityForResult(intent, ADD_RESTO_REQUEST);
            }
        });

        // SURPRISE button to pick a random Entertainment
        Button surprise = (Button) rootView.findViewById(R.id.surprise);
        surprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // EQUAL RANDOMNESS
                Collections.shuffle(entertainments);
                mAdapter.notifyDataSetChanged(); //enable this to view the shuffling animation

                if(entertainments.size() != 0){
                    Snackbar snackbar = Snackbar.make(view, "Go for... " + entertainments.get(entertainments.size()/2).getmEntertainmentName() + "!!!", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Action", null).show();
                    TextView snackbarActionTextView =  snackbar.getView().findViewById( android.support.design.R.id.snackbar_text );
                    snackbarActionTextView.setTextSize( 30 );
                    snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);
                }
            }
        });

        // CLEAR Button to go to add a new Entertainment activity
        Button clear = (Button) rootView.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entertainments.clear();
                //mAdapter.notifyDataSetChanged();
                mRecyclerView.setVisibility(View.GONE);
                mEmptyStateTextView.setText("No Entertainments. :(");
                mEmptyStateTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof EntertainmentAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            String name = entertainments.get(viewHolder.getAdapterPosition()).getmEntertainmentName();

            // backup of removed item for undo purpose
            final Entertainment deletedItem = entertainments.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
            if(entertainments.size() == 0 ){
                mRecyclerView.setVisibility(View.GONE);
                mEmptyStateTextView.setText("No Entertainments. :(");
                mEmptyStateTextView.setVisibility(View.VISIBLE);
            }

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                    mEmptyStateTextView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);

            snackbar.show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // When the activity is stopped, release the media player resources because we won't
        // be playing any more sounds.
        releaseMediaPlayer();
    }

    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer(){
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;
        }
    }
}
