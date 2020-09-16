package art.manguste.android.gamesearch.ui.viewcard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import art.manguste.android.gamesearch.R;

public class GameCardRVAdapter extends RecyclerView.Adapter<GameCardRVAdapter.GameCardViewHolder> {

    private ArrayList<GameCard> games = new ArrayList<>();

    interface ListItemClickListener{
        void onListItemClick(int position, GameCard gameCard);
        void onViewClick(View v, int position, GameCard gameCard);
    }

    public GameCardRVAdapter(ArrayList<GameCard> gamesList) {
        super();
        games = gamesList;
    }

    @NonNull
    @Override
    public GameCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaterialCardView cv = (MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_game, parent, false);
        return new GameCardViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull GameCardViewHolder holder, int position) {
        holder.bind(games.get(position));
    }

    @Override
    public int getItemCount() {
        return games.size();
    }


    class GameCardViewHolder extends RecyclerView.ViewHolder{
        private MaterialCardView item;
        private GameCard gameCard;

        public GameCardViewHolder(@NonNull MaterialCardView itemView) {
            super(itemView);
            item = itemView;
        }

        void bind(GameCard gameCard) {
            this.gameCard = gameCard;
            if (gameCard != null) {
                ((TextView) item.findViewById(R.id.title)).setText(gameCard.getTitle());
            }
        }
    }
}
