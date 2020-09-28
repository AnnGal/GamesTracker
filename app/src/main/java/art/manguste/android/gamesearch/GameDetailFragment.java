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
import art.manguste.android.gamesearch.api.GamesApiLoader;
import art.manguste.android.gamesearch.core.SearchType;
import art.manguste.android.gamesearch.db.GameDBHelper;

import static art.manguste.android.gamesearch.api.URLMaker.formURL;

public class GameDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<Game>>{

    private static final String EXTRA_GAME_CODE = "game_site_code";
    private static final String EXTRA_GAME_NAME = "game_name";
    private static final int LOADER_GAME_ID = 3;

    private String gameCode = null; // alias - needed for create an url
    private String gameName = null; // shows as page title
    private int mImageSize = 0; // for uploading image via Glide

    // UI
    ProgressBar mProgressBar;
    ImageView mCoverImageView;
    TextView mTitle;
    TextView mRelease;
    TextView mDescription;
    TextView mLabelGenre;
    TextView mGenre;
    TextView mLabelDeveloper;
    TextView mDeveloper;
    TextView mLabelPlatform;
    TextView mPlatform;
    TextView mLabelPublisher;
    TextView mPublisher;
    TextView mLabelWebsite;
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
        // pack params
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_GAME_CODE, gameCode);
        bundle.putString(EXTRA_GAME_NAME, gameName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // unpack params
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
        mLabelGenre = view.findViewById(R.id.tv_label_genre);
        mGenre = view.findViewById(R.id.tv_genre);
        mLabelDeveloper = view.findViewById(R.id.tv_label_developer);
        mDeveloper = view.findViewById(R.id.tv_developer);
        mLabelPlatform = view.findViewById(R.id.tv_label_platform);
        mPlatform = view.findViewById(R.id.tv_platform);
        mLabelPublisher = view.findViewById(R.id.tv_label_publisher);
        mPublisher = view.findViewById(R.id.tv_publisher);
        mLabelWebsite = view.findViewById(R.id.tv_label_game_website);
        mWebsite = view.findViewById(R.id.tv_game_website);
        mFavoriteButton = view.findViewById(R.id.ib_favorite);
        mShareButton = view.findViewById(R.id.ib_share);
        mDisclaimer = view.findViewById(R.id.tv_disclaimer);

        // save game as favorite game for tracking
        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameDBHelper.changeFavoriteStatus(getContext(), mGame);
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

        // share game link
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

    /**
     * set game detailed info on a fragment
     * @param data - ArrayList with only one Game
     */
    private void setGameInfo(ArrayList<Game> data) {
        Game game = data.get(0);

        if (game != null){
            mGame = game;
            // grab image from web
            Glide.with(getContext())
                    .load(game.getImgHttp())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.empty_photo)
                    .override(mImageSize, mImageSize)
                    .into(mCoverImageView);
            // text info
            mTitle.setText(game.getName());
            mRelease.setText(game.getNotNullReleaseStr());
            mDescription.setText(Html.fromHtml(game.getDescription()));

            // genres
            String genres = game.getGenresList();
            if (genres != null && genres.length()>0){
                mGenre.setText(genres);
            } else {
                mGenre.setVisibility(View.GONE);
                mLabelGenre.setVisibility(View.GONE);
            }

            // developers
            String dev = game.getDevelopersList();
            if (dev != null && dev.length()>0){
                mDeveloper.setText(dev);
            } else {
                mDeveloper.setVisibility(View.GONE);
                mLabelDeveloper.setVisibility(View.GONE);
            }

            // platform
            String platform = game.getPlatformsList();
            if (platform != null && platform.length()>0){
                mPlatform.setText(platform);
            } else {
                mPlatform.setVisibility(View.GONE);
                mLabelPlatform.setVisibility(View.GONE);
            }

            // publisher
            String publisher = game.getPublishersList();
            if (publisher != null && publisher.length()>0){
                mPublisher.setText(publisher);
            } else {
                mPublisher.setVisibility(View.GONE);
                mLabelPublisher.setVisibility(View.GONE);
            }

            // if it a favorite game - fire up a star
            if (game.isFavorite()) {
                mFavoriteButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_star_filled));
            }

            // link to game website (if game have any)
            String website = mGame.getWebsite();
            if (website != null && website.length() > 0){
                mWebsite.setText(Html.fromHtml("<u>"+website+"</u>"));
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
            } else {
                mWebsite.setVisibility(View.GONE);
                mLabelWebsite.setVisibility(View.GONE);
            }

            // link to the API website
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