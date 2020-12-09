package art.manguste.android.gamesearch.gamesList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import art.manguste.android.gamesearch.core.SearchType
import art.manguste.android.gamesearch.databinding.FragmentGameSearchBinding

import art.manguste.android.gamesearch.db.GameDatabase

/**
 * A simple [Fragment] subclass.
 * Use the [GamesListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GamesListFragment : Fragment() {

    private lateinit var viewModer: GamesViewModel

    private var _binding: FragmentGameSearchBinding? = null
    private val binding get() = _binding!!
    private var searchType: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentGameSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchType = requireArguments().getString(SEARCH_TYPE)
        Log.d(TAG, "SEARCH_TYPE = $searchType")
    }




    companion object {
        private val TAG = GamesListFragment::class.java.simpleName
        private const val SEARCH_TYPE = "search_type"

        /**
         * New instance via factory method
         * this fragment using the provided parameters.
         *
         * @param searchType - determines what kind of search and API request to use
         * @return new instance of fragment GamesListFragment.
         */
        fun newInstance(searchType: SearchType): GamesListFragment {
            val fragment = GamesListFragment()
            val args = Bundle()
            args.putString(SEARCH_TYPE, searchType.toString())
            fragment.arguments = args
            return fragment
        }
    }
}