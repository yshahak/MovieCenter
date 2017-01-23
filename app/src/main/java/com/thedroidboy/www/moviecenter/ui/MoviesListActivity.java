package com.thedroidboy.www.moviecenter.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.thedroidboy.www.moviecenter.R;
import com.thedroidboy.www.moviecenter.control.MoviesController;
import com.thedroidboy.www.tmdblayer.ServerHelper;
import com.thedroidboy.www.tmdblayer.provider.MovieItem;
import com.thedroidboy.www.tmdblayer.provider.MovieProvider;

import static com.thedroidboy.www.tmdblayer.MovieStoreManager.CODE_POPULAR_LIST;
import static com.thedroidboy.www.tmdblayer.MovieStoreManager.CODE_QUERY;
import static com.thedroidboy.www.tmdblayer.MovieStoreManager.EXTRA_QUERY;
import static com.thedroidboy.www.tmdblayer.provider.MoviesContract.MoviesEntry.COL_MOVIE_POSTER_INDEX;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item overview. On tablets, the activity presents the list of items and
 * item overview side-by-side using two vertical panes.
 */
public class MoviesListActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String EXTRA_CODE = "extraCode";
    private int errorCont;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecyclerView recyclerView;
    private View progrssBar;
    private SearchView mSearchView;
    private int currentCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        progrssBar = findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView)findViewById(R.id.item_list);
        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }
        recyclerView.setLayoutManager(new GridLayoutManager(this, mTwoPane ? 3 : 2));
        if (savedInstanceState == null || savedInstanceState.getInt(EXTRA_CODE, CODE_POPULAR_LIST) == CODE_POPULAR_LIST) {
            getSupportLoaderManager().initLoader(CODE_POPULAR_LIST, null, MoviesListActivity.this);
        }
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(this);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_CODE, currentCode);
    }

    @Override
    public void onBackPressed() {
        if (currentCode  != CODE_POPULAR_LIST) {
            currentCode = CODE_POPULAR_LIST;
            getSupportLoaderManager().restartLoader(CODE_POPULAR_LIST, null, MoviesListActivity.this);
        } else {
            super.onBackPressed();
        }
    }

    public void setupRecyclerView(Cursor moviesCursor) {
        progrssBar.setVisibility(View.GONE);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(moviesCursor));
        if (mTwoPane) {
            moviesCursor.moveToFirst();
            MovieItem movieItem = MovieProvider.convertRecordToMovieItem(moviesCursor);
            Bundle arguments = new Bundle();
            arguments.putParcelable(ItemDetailFragment.ARG_MOVIE, movieItem);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (ServerHelper.isNetworkAvailable(this)) {
            progrssBar.setVisibility(View.VISIBLE);
            MoviesController.getInstance().queryServer(this, query);
        } else {
            Toast.makeText(this, getString(R.string.internet_error), Toast.LENGTH_SHORT).show();
        }
        mSearchView.setQuery("", false);
        mSearchView.setIconified(true);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int queryCode, Bundle resultData) {
        currentCode = queryCode;
        switch (queryCode){
            case CODE_POPULAR_LIST:
                return MoviesController.getInstance().getPopularMovieList(this);
            case CODE_QUERY:
                String query = resultData.getString(EXTRA_QUERY);
                return MoviesController.getInstance().getQueryResults(this, query);

        }
        return MoviesController.getInstance().getPopularMovieList(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0){
            getSupportLoaderManager().restartLoader(CODE_POPULAR_LIST, null, MoviesListActivity.this);
        } else {
            setupRecyclerView(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }




    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final Cursor moviesCursor;

        SimpleItemRecyclerViewAdapter(Cursor moviesCursor) {
            this.moviesCursor = moviesCursor;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            moviesCursor.moveToPosition(position);
            Picasso.with(holder.itemView.getContext())
                    .load(moviesCursor.getString(COL_MOVIE_POSTER_INDEX))
                    .fit()
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.poster, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            errorCont++;
                            if (errorCont >= 3){ //sometimes the server block poster display
                                errorCont = 0;
                                Toast.makeText(MoviesListActivity.this, "The is a server connection error", Toast.LENGTH_LONG).show();
                                Log.d("TAG", "Server error");

//                                getSupportLoaderManager().restartLoader(CODE_POPULAR_LIST, null, MoviesListActivity.this);
                            }
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return moviesCursor.getCount();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final ImageView poster;

            ViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                poster = (ImageView)view.findViewById(R.id.poster);
            }

            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                moviesCursor.moveToPosition(position);
                MovieItem movieItem = MovieProvider.convertRecordToMovieItem(moviesCursor);
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(ItemDetailFragment.ARG_MOVIE, movieItem);
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                } else {
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_MOVIE, movieItem);

                    context.startActivity(intent);
                }
            }
        }
    }
}
