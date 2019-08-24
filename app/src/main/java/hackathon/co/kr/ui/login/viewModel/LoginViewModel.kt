package hackathon.co.kr.ui.login.viewModel

import android.app.Application
import android.os.Handler
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import hackathon.co.kr.neopen.R
import hackathon.co.kr.util.network.NetworkUtil
import hackathon.co.kr.util.network.model.ResponseVO
import hackathon.co.kr.util.network.model.StringResponse
import hackathon.co.kr.util.setSpData
import hackathon.co.kr.util.token
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(app: Application) : AndroidViewModel(app) {
    var titles = app.resources.getStringArray(R.array.assign_titles)
    var inputs = arrayListOf(
            ObservableField(""), ObservableField(""), ObservableField(""), ObservableField(""),
            ObservableField(""), ObservableField(""), ObservableField(""), ObservableField("")
    )
    var toastMessage = MutableLiveData<String>()
    var isSplashing = ObservableField<Boolean>()

    init {
        // 0 : id
        // 1 : pw
        isSplashing.set(true)
        Handler().postDelayed({
            isSplashing.set(false)
        }, 3000)
    }

    fun assignBtnClick(isAssign: Boolean) {
        val id = inputs[0].get()
        val pw = inputs[1].get()

        if (isAssign) {
            Log.d("assignBtnClick : ", "$id, $pw")

            // 회원가입 로직 넣으면 됩니다.
            postSignup(id!!, pw!!, { response ->
                setSpData(token, response.result)
                toastMessage.value = "회원가입 성공"
            }, { t -> Log.d("assignBtnClick ", t.toString()) })
        } else {
            Log.d("assignBtnClick : ", "$id, $pw")
            toastMessage.value = "회원가입 진행"
        }
    }

    fun loginBtnClick() {
        val id = inputs[0].get()
        val pw = inputs[1].get()

        Log.d("loginBtnClick : ", "$id, $pw")
        // 로그인 화면에서 로그인 로직 넣으면 됩니다.
        postLogin(id!!, pw!!, { response ->
            setSpData(token, response.result)
            toastMessage.value = "로그인 성공"
        }, { t -> Log.d("loginBtnClick ", t.toString()) })
    }


    fun postSignup(email: String, pwd: String, onSuccess: (responseVo: StringResponse) -> Unit, onFailure: (t: Throwable) -> Unit) {
        var returnValue: StringResponse? = null
        val getT = NetworkUtil.getInstance().postAccountCus(email, pwd)
        getT.enqueue(object : Callback<StringResponse> {
            override fun onFailure(call: Call<StringResponse>, t: Throwable) {
                onFailure(t)
            }

            override fun onResponse(call: Call<StringResponse>, response: Response<StringResponse>) {
                returnValue = response.body()!!
                onSuccess(returnValue!!)
            }
        })
    }

    fun postLogin(email: String, pwd: String, onSuccess: (responseVo: StringResponse) -> Unit, onFailure: (t: Throwable) -> Unit) {
        var returnValue: StringResponse? = null
        val getT = NetworkUtil.getInstance().postAccountCusLogin(email, pwd)
        getT.enqueue(object : Callback<StringResponse> {
            override fun onFailure(call: Call<StringResponse>, t: Throwable) {
                onFailure(t)
            }

            override fun onResponse(call: Call<StringResponse>, response: Response<StringResponse>) {
                returnValue = response.body()!!
                onSuccess(returnValue!!)
            }
        })
    }
}