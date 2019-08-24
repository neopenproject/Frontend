package hackathon.co.kr.neopen

import android.app.Application
import hackathon.co.kr.neopen.sdk.pen.PenCtrl
import hackathon.co.kr.util.sharedPreferenceInit

class NeopenApplication : Application() {
    var iPenCtrl: PenCtrl? = null

    override fun onCreate() {
        super.onCreate()

        iPenCtrl = PenCtrl.getInstance().apply {
            setContext(applicationContext)
            registerBroadcastBTDuplicate()
        }

        sharedPreferenceInit(this)
    }

    override fun onTerminate() {
        iPenCtrl?.unregisterBroadcastBTDuplicate()
        super.onTerminate()
    }
}