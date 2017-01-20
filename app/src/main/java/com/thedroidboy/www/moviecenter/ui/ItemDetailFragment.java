package com.thedroidboy.www.moviecenter.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thedroidboy.www.moviecenter.R;
import com.thedroidboy.www.tmdblayer.provider.MovieItem;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link MoviesListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {

    public static final String ARG_MOVIE = "movie_item";

    /**
     * The movieTitle this fragment is presenting.
     */
    private MovieItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE)) {
            mItem = getArguments().getParcelable(ARG_MOVIE);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.movieTitle);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_details, container, false);
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.movie_desc)).setText(mItem.overview);
            ((TextView) rootView.findViewById(R.id.movie_release)).setText(mItem.release);
            ((TextView) rootView.findViewById(R.id.movie_rate)).setText(mItem.rate + "/10");

            Picasso.with(getContext())
                    .load(mItem.moviePoster)
                    .fit()
                    .into((ImageView)rootView.findViewById(R.id.poster));
        }

        return rootView;
    }
}
