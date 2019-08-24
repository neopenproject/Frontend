package hackathon.co.kr.ui.login.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import hackathon.co.kr.neopen.R
import hackathon.co.kr.neopen.databinding.ActivityAssignBinding
import hackathon.co.kr.ui.activity.MainActivity
import hackathon.co.kr.ui.login.viewModel.LoginViewModel
import hackathon.co.kr.util.setSpData

class AssignActivity : AppCompatActivity() {
    var layoutRes = R.layout.activity_assign
    lateinit var binding: ActivityAssignBinding
    val assignVM: LoginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    fun onDataBinding() {
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.activity = this
        binding.assignVM = assignVM
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onDataBinding()
        subscribeUI()
        window.statusBarColor = resources.getColor(R.color.color_3440ff)
    }

    fun subscribeUI() {
        assignVM.toastMessage.observe(this, Observer { message ->
            when (message) {
                "회원가입 진행" -> {

                }
                "회원가입 성공" -> {
                    setResult(Activity.RESULT_OK, null)
                    finish()
                }
                "로그인 성공" -> {

                }
            }
            Toast.makeText(this@AssignActivity, message, Toast.LENGTH_SHORT).show()
        })
    }
}