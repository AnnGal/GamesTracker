package art.manguste.android.gamesearch.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import art.manguste.android.gamesearch.GameDetailActivity;
import art.manguste.android.gamesearch.R;
import art.manguste.android.gamesearch.db.GameDBHelper;

public class GameCardAdapter extends RecyclerView.Adapter<GameCardAdapter.GameViewHolder>
        implements ListItemClickListener {

    private static final String TAG = GameCardAdapter.class.getSimpleName();

    private ArrayList<Game> games = new ArrayList<>();
    private Context mContext;
    private int mImageSize;
    private ViewGroup mViewGroup;
    private SearchType mSearchType;

    public GameCardAdapter(Context context, int imageSize, ViewGroup container, SearchType searchType) {
        super();
        mContext = context;
        mImageSize = imageSize;
        mViewGroup = container;
        mSearchType = searchType;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaterialCardView cv = (MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_game, parent, false);
        return new GameViewHolder(cv, this);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        holder.bind(games.get(position));
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
        notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(Game game) {
        Intent intent = new Intent(mContext, GameDetailActivity.class);
        intent.putExtra(GameDetailActivity.EXTRA_GAME_CODE, game.getGameAlias());
        intent.putExtra(GameDetailActivity.EXTRA_GAME_NAME, game.getName());
        mContext.startActivity(intent);
    }

    @Override
    public void onFavoriteBtnClick(Game game, ImageButton mFavoriteImageButton, int position) {
        // add or remove from DB async
        boolean isAddToFavorite = true;
        if (!game.isFavorite()){
            // add to Favorite
            mFavoriteImageButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_star_filled));
        } else {
            // remove from Favorite
            mFavoriteImageButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_star_empty));
            isAddToFavorite = false;
        }

        game.setFavorite(isAddToFavorite);
        showSnackbar(game.getName(), isAddToFavorite);

        GameDBHelper.changeFavoriteStatus(mContext, game);

        // if it favorite list - then remove game from the list
        if (!isAddToFavorite && SearchType.FAVORITE.equals(mSearchType)){
            games.remove(position);
            this.notifyItemRemoved(position);
        }
    }

    private void showSnackbar(String name, boolean added){
        // Snack-bar interaction
        String snackMessage = "\"" + name + "\" ";
        snackMessage += added ? "added to favourites" : "removed from favourites";

        Snackbar snackbar = Snackbar
                .make(mViewGroup, snackMessage, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    class GameViewHolder extends RecyclerView.ViewHolder{
        private Game game;

        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private TextView mReleaseTextView;
        private TextView mLabelRateTextView;
        private TextView mRateTextView;
        private ImageButton mFavoriteImageButton;
        private ImageView mGameIconImageView;
        private ListItemClickListener mListItemClickListener;

        public GameViewHolder(@NonNull MaterialCardView itemView, ListItemClickListener listItemClickListener) {
            super(itemView);
            mListItemClickListener = listItemClickListener;

            mTitleTextView = itemView.findViewById(R.id.title);
            mDescriptionTextView = itemView.findViewById(R.id.description);
            mReleaseTextView = itemView.findViewById(R.id.release_date);
            mLabelRateTextView = itemView.findViewById(R.id.rate_label);
            mRateTextView = itemView.findViewById(R.id.rate);
            mFavoriteImageButton = itemView.findViewById(R.id.favorite);
            mGameIconImageView = itemView.findViewById(R.id.game_icon);

            mFavoriteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListItemClickListener != null)
                        mListItemClickListener.onFavoriteBtnClick(game, mFavoriteImageButton, getAdapterPosition());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListItemClickListener != null)
                        mListItemClickListener.onListItemClick(game);
                }
            });
        }

        @SuppressLint("SetTextI18n")
        void bind(Game game) {
            this.game = game;
            if (game != null) {
                mTitleTextView.setText(this.game.getName());
                mReleaseTextView.setText(game.getNotNullReleaseStr());

                // description
                String genres = game.getGenresList();
                if (genres != null && genres.length()>0){
                    mDescriptionTextView.setText(mContext.getResources().getText(R.string.label_genres)+" "+game.getGenresList());
                }

                // raring
                float rating = Float.parseFloat(game.getRating());
                if (Float.compare(rating, 0f) == 0){
                    mRateTextView.setVisibility(View.GONE);
                    mLabelRateTextView.setVisibility(View.GONE);
                } else {
                    mRateTextView.setVisibility(View.VISIBLE);
                    mLabelRateTextView.setVisibility(View.VISIBLE);
                    mRateTextView.setText(game.getRating());
                }

                // if game in favorite list - fire up a star
                if (game.isFavorite()) {
                    Log.d(TAG, "Favorite: " + game.getGameAlias());
                    mFavoriteImageButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_star_filled));
                } else mFavoriteImageButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_star_empty));

                // download image from web
                Glide.with(mContext)
                        .load(game.getImgHttp())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.drawable.empty_photo)
                        .override(mImageSize, mImageSize)
                        .into(mGameIconImageView);
            }
        }

    }

}


