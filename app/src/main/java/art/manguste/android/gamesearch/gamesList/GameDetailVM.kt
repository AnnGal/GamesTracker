package art.manguste.android.gamesearch.gamesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import art.manguste.android.gamesearch.api.GamesApi
import art.manguste.android.gamesearch.core.GameDetail
import art.manguste.android.gamesearch.core.LoadStatus
import kotlinx.coroutines.launch

class GameDetailVM(application: Application) : AndroidViewModel(application)  {

    private val _gameDetail = MutableLiveData<GameDetail>()
    val gameDetail: LiveData<GameDetail> get() = _gameDetail

    private val _status = MutableLiveData<LoadStatus>()
    val status: LiveData<LoadStatus>
        get() = _status

    init {
        _status.value = LoadStatus.NONE
    }

    fun getGameInfo(gameAlias: String){
        viewModelScope.launch {
            try {
                _status.value = LoadStatus.LOADING

                val resultRequest = GamesApi.retrofitService.getSpecificGame(gameAlias = gameAlias)

                _gameDetail.value = resultRequest
                //Log.d("GamesList fragment", "games = ${gamesList.value?.size}")

                _status.value = LoadStatus.DONE
            } catch (e: Exception) {
                _status.value = LoadStatus.ERROR
                _gameDetail.value = null
            }
        }
    }
}



