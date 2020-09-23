package art.manguste.android.gamesearch.core;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import art.manguste.android.gamesearch.GameDetailActivity;
import art.manguste.android.gamesearch.R;
import art.manguste.android.gamesearch.db.GameDBHelper;

public class GameCardAdapter extends RecyclerView.Adapter<GameCardAdapter.GameViewHolder>
        implements ListItemClickListener {

    private ArrayList<Game> games = new ArrayList<>();
    private Context mContext;
    private int mImageSize;

    public GameCardAdapter(Context context, int imageSize) {
        super();
        mContext = context;
        mImageSize = imageSize;
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
    public void onFavoriteBtnClick(Game game) {
        // TODO check if not favorite game
        // add to DB asynch
        GameDBHelper.saveGameAsFavorite(mContext, game);
        //Toast.makeText(mContext, "add to favorite", Toast.LENGTH_SHORT).show();
    }

    class GameViewHolder extends RecyclerView.ViewHolder{
        private Game game;

        private TextView mTitleTextView;
        private TextView mDescriptionTextView;
        private TextView mReleaseTextView;
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
            mRateTextView = itemView.findViewById(R.id.rate);
            mFavoriteImageButton = itemView.findViewById(R.id.favorite);
            mGameIconImageView = itemView.findViewById(R.id.game_icon);

            mFavoriteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListItemClickListener != null)
                        mListItemClickListener.onFavoriteBtnClick(game);
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

        void bind(Game game) {
            this.game = game;
            if (game != null) {
                mTitleTextView.setText(this.game.getName());
                mDescriptionTextView.setText("Genres: "+game.getGenresList()/*+"\n"
                        +"Platforms: "+game.getPlatformsList()*/);
                mReleaseTextView.setText(game.getReleaseStr());
                mRateTextView.setText(game.getRating());

                if (game.getFavorite()) {
                    mFavoriteImageButton.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_star_filled));
                }

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


