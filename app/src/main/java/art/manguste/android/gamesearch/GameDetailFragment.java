package art.manguste.android.gamesearch;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;

import art.manguste.android.gamesearch.core.Game;
import art.manguste.android.gamesearch.threads.GamesApiLoader;
import art.manguste.android.gamesearch.core.SearchType;
import art.manguste.android.gamesearch.threads.DBUtils;

import static art.manguste.android.gamesearch.api.URLMaker.formURL;

public class GameDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<Game>>{

    private static final String EXTRA_GAME_CODE = "game_site_code";
    private static final String EXTRA_GAME_NAME = "game_name";
    private String gameCode = null;
    private String gameName = null;
    private static final int LOADER_GAME_ID = 3;
    private int mImageSize = 0;

    //UI
    ProgressBar mProgressBar;
    ImageView mCoverImageView;
    TextView mTitle;
    TextView mRelease;
    TextView mDescription;
    TextView mGenre;
    TextView mDeveloper;
    TextView mPlatform;
    TextView mPublisher;
    TextView mWebsite;
    ImageButton mFavoriteButton;
    ImageButton mShareButton;
    TextView mDisclaimer;

    Game mGame;
    CollapsingToolbarLayout mToolbarLayout;

    public GameDetailFragment() {
        // Required empty public constructor
    }

    public static GameDetailFragment createInstance(String gameCode, String gameName) {
        GameDetailFragment fragment = new GameDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_GAME_CODE, gameCode);
        bundle.putString(EXTRA_GAME_NAME, gameName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            gameCode = getArguments().getString(EXTRA_GAME_CODE);
            gameName = getArguments().getString(EXTRA_GAME_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_detail, container, false);

        mImageSize = getResources().getDimensionPixelSize(R.dimen.cover_size) * 2;

        // UI objects
        mProgressBar = view.findViewById(R.id.pb_loading);
        mCoverImageView = view.findViewById(R.id.iv_cover);
        mTitle = view.findViewById(R.id.tv_title);
        mRelease = view.findViewById(R.id.tv_release);
        mDescription = view.findViewById(R.id.tv_description);
        mGenre = view.findViewById(R.id.tv_genre);
        mDeveloper = view.findViewById(R.id.tv_developer);
        mPlatform = view.findViewById(R.id.tv_platform);
        mPublisher = view.findViewById(R.id.tv_publisher);
        mWebsite = view.findViewById(R.id.tv_game_website);
        mFavoriteButton = view.findViewById(R.id.ib_favorite);
        mShareButton = view.findViewById(R.id.ib_share);
        mDisclaimer = view.findViewById(R.id.tv_disclaimer);

        //save game as favorite game for tracking
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBUtils.changeFavoriteStatus(getContext(), mGame);
                boolean isAddToFavorite = true;
                if (!mGame.isFavorite()){
                    // add to Favorite
                    mFavoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_star_filled));
                } else {
                    // remove from Favorite
                    mFavoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_star_empty));
                    isAddToFavorite = false;
                }
                mGame.setFavorite(isAddToFavorite);
            }
        });

        //save game as favorite game for tracking
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mGame.getName()+" - "+ mGame.getApiLink());
                startActivity(intent);
            }
        });

        // toolbar and return button
        AppCompatActivity mActivity = ((AppCompatActivity) getActivity());
        mActivity.setSupportActionBar((Toolbar)view.findViewById(R.id.toolbar));
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbarLayout = view.findViewById(R.id.toolbar_collapsing);
        mToolbarLayout.setTitle(gameName);
        mToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorTab));
        mToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        // start Loader
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LOADER_GAME_ID, null, this);

        return view;
    }

    // Loader begin
    @NonNull
    @Override
    public Loader<ArrayList<Game>> onCreateLoader(int id, @Nullable Bundle args) {
        mProgressBar.setVisibility(View.VISIBLE);
        String urlString = formURL(SearchType.GAME, gameCode);
        return new GamesApiLoader(getContext(), urlString, SearchType.GAME);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Game>> loader, ArrayList<Game> data) {
        mProgressBar.setVisibility(View.GONE);

        //TODO if none games found - set text about it

        // change data in view
        if (data != null && !data.isEmpty()) {
            setGameInfo(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Game>> loader) {
        //
    }
    // Loader end


    private void setGameInfo(ArrayList<Game> data) {
        Game game = data.get(0);

        if (game != null){
            mGame = game;

            Glide.with(getContext())
                    .load(game.getImgHttp())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.empty_photo)
                    .override(mImageSize, mImageSize)
                    .into(mCoverImageView);

            mTitle.setText(game.getName());
            mRelease.setText(game.getReleaseStr());
            mDescription.setText(Html.fromHtml(game.getDescription()));
            mGenre.setText(game.getGenresList());
            mDeveloper.setText(game.getDevelopersList());
            mPlatform.setText(game.getPlatformsList());
            mPublisher.setText(game.getPublishersList());

            if (game.isFavorite()) {
                mFavoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_star_filled));
            }

            mWebsite.setText(Html.fromHtml("<u>"+game.getWebsite()+"</u>"));
            mWebsite.setTextColor(Color.BLUE);

            mWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = String.valueOf(mWebsite.getText());
                    if (url.length()>0){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                }
            });

            mDisclaimer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = mGame.getApiLink();
                    if (url.length()>0){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                }
            });
        }

    }


}