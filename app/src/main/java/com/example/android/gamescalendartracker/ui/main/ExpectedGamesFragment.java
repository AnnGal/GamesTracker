package com.example.android.gamescalendartracker.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.gamescalendartracker.R;
import com.example.android.gamescalendartracker.ui.viewcard.GameCard;
import com.example.android.gamescalendartracker.ui.viewcard.GameCardAdapter;

import java.util.ArrayList;
import java.util.Date;

public class ExpectedGamesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExpectedGamesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.games_list, container, false);

        final ArrayList<GameCard> games = new ArrayList<>();
        games.add(new GameCard(0,"title",  "developer", "publisher", new Date()));
        games.add(new GameCard(0,"title2",  "developer2", "publisher2", new Date()));

        GameCardAdapter adapter = new GameCardAdapter(getActivity(), games);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);


        return rootView;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpectedGamesFragment newInstance(String param1, String param2) {
        ExpectedGamesFragment fragment = new ExpectedGamesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


}
