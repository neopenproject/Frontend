package hackathon.co.kr.ui.login.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hackathon.co.kr.neopen.R

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    var titles = app.resources.getStringArray(R.array.assign_titles)
    var inputs = arrayListOf(
            ObservableField(""), ObservableField(""), ObservableField(""), ObservableField(""),
            ObservableField(""), ObservableField(""), ObservableField(""), ObservableField("")
    )
    var toastMessage = MutableLiveData<String>()

    init {
        // 0 : id
        // 1 : pw
    }

    fun assignBtnClick(isAssign: Boolean) {
        val id = inputs[0].get()
        val pw = inputs[1].get()

        if (isAssign) {
            Log.d("assignBtnClick : ", "$id, $pw")

            // 회원가입 로직 넣으면 됩니다.
        }
        else {
            Log.d("assignBtnClick : ", "$id, $pw")
            toastMessage.value = "회원가입 진행"
        }
    }

    fun loginBtnClick() {
        val id = inputs[0].get()
        val pw = inputs[1].get()

        Log.d("loginBtnClick : ", "$id, $pw")
        // 로그인 화면에서 로그인 로직 넣으면 됩니다.
    }
}