package hackathon.co.kr.ui.login.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import hackathon.co.kr.neopen.R
import hackathon.co.kr.neopen.databinding.ActivityAssignBinding
import hackathon.co.kr.ui.login.viewModel.LoginViewModel

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
    }
}