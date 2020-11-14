package art.manguste.android.gamesearch.threads

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

// extra threads for small tasks // Executor test
class Executors private constructor(private val diskIO: Executor, private val networkIO: Executor, private val mainThread: Executor) {
    fun diskIO(): Executor {
        return diskIO
    }

    fun mainThread(): Executor {
        return mainThread
    }

    fun networkIO(): Executor {
        return networkIO
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object {
        private val LOCK = Any()
        private var sInstance: Executors? = null
        val instance: Executors?
            get() {
                if (sInstance == null) {
                    synchronized(LOCK) {
                        sInstance = Executors(java.util.concurrent.Executors.newSingleThreadExecutor(),
                                java.util.concurrent.Executors.newFixedThreadPool(3),
                                MainThreadExecutor())
                    }
                }
                return sInstance
            }
    }
}