package hackathon.co.kr.neopen

import android.app.Application
import hackathon.co.kr.neopen.sdk.pen.PenCtrl

class NeopenApplication : Application() {
    var iPenCtrl: PenCtrl? = null

    override fun onCreate() {
        super.onCreate()

        iPenCtrl = PenCtrl.getInstance().apply {
            setContext(applicationContext)
            registerBroadcastBTDuplicate()
        }
    }

    override fun onTerminate() {
        iPenCtrl?.unregisterBroadcastBTDuplicate()
        super.onTerminate()
    }
}