package art.manguste.android.gamesearch;

import android.content.Context;
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

public class GameCardRVAdapter extends RecyclerView.Adapter<GameCardRVAdapter.GameViewHolder> {

    private ArrayList<Game> games = new ArrayList<>();
    private Context mContext;
    private int mImageSize;

    interface ListItemClickListener{
        void onListItemClick(int position, Game game);
        void onViewClick(View v, int position, Game game);
    }

    public GameCardRVAdapter(Context context, int imageSize) {
        super();
        mContext = context;
        mImageSize = imageSize;

    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaterialCardView cv = (MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_game, parent, false);
        return new GameViewHolder(cv);
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

    class GameViewHolder extends RecyclerView.ViewHolder{
        private MaterialCardView item;
        private Game game;
        TextView mTitleTextView;
        TextView mDescriptionTextView;
        TextView mReleaseTextView;
        TextView mRateTextView;
        ImageButton mFavoriteImageButton;
        ImageView mGameIconImageView;

        public GameViewHolder(@NonNull MaterialCardView itemView) {
            super(itemView);
            item = itemView;
            mTitleTextView = (TextView) itemView.findViewById(R.id.title);
            mDescriptionTextView = (TextView) itemView.findViewById(R.id.description);
            mReleaseTextView = (TextView) itemView.findViewById(R.id.release_date);
            mRateTextView = (TextView) itemView.findViewById(R.id.rate);
            mFavoriteImageButton = (ImageButton) itemView.findViewById(R.id.favorite);
            mGameIconImageView = (ImageView) itemView.findViewById(R.id.game_icon);
        }

        void bind(Game game) {
            this.game = game;
            if (game != null) {
                mTitleTextView.setText(this.game.getName());
                mDescriptionTextView.setText("Genres: "+game.getGenresList()/*+"\n"
                        +"Platforms: "+game.getPlatformsList()*/);
                mReleaseTextView.setText(game.getReleaseStr());
                mRateTextView.setText(game.getRating());
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
