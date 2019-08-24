package hackathon.co.kr.ui.login.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import hackathon.co.kr.neopen.R
import hackathon.co.kr.neopen.databinding.ActivityLoginBinding
import hackathon.co.kr.ui.login.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    var layoutRes = R.layout.activity_login
    lateinit var binding: ActivityLoginBinding
    val loginVM: LoginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    fun onDataBinding() {
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.activity = this
        binding.loginVM = loginVM
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onDataBinding()
        subscribeUI()
    }

    fun subscribeUI() {
        loginVM.toastMessage.observe(this, Observer { message ->
            when (message) {
                "회원가입 진행" -> {
                    startActivity(Intent(this@LoginActivity, AssignActivity::class.java))
                }
            }
            Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
        })
    }

}