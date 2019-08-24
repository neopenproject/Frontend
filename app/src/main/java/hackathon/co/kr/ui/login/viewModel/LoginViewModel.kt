package hackathon.co.kr.ui.login.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import hackathon.co.kr.neopen.R

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    var titles = app.resources.getStringArray(R.array.login_titles)
    var inputs = arrayListOf(
            ObservableField(""), ObservableField(""), ObservableField(""), ObservableField(""),
            ObservableField(""), ObservableField(""), ObservableField(""), ObservableField("")
    )
    var toastMessage = ObservableField<String>()

    init {
        // 0 : id
        // 1 : pw
    }

    fun assignBtnClick() {
        val id = inputs[0].get()
        val pw = inputs[1].get()

        Log.d("assignBtnClick : ", "$id, $pw")

        // 회원가입 로직 넣으면 됩니다.
    }
}