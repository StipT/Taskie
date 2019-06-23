package com.tstipanic.taskie.ui.auth_screen.login

import android.content.Intent
import android.os.Bundle
import com.tstipanic.taskie.R
import com.tstipanic.taskie.common.displayToast
import com.tstipanic.taskie.ui.auth_screen.register.RegisterActivity
import com.tstipanic.taskie.ui.base.BaseActivity
import com.tstipanic.taskie.ui.task_list_screen.activity.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject

class LoginActivity : BaseActivity(), LoginContract.View {

    private val presenter by inject<LoginContract.Presenter>()

    override fun getLayoutResourceId(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.setView(this)
    }

    override fun setUpUi() {
        login.setOnClickListener { signInClicked() }
        goToLogin.setOnClickListener { goToRegistration() }
    }

    override fun signInClicked() = presenter.onSingInClicked(password.text.toString(), email.text.toString())


    override fun goToRegistration() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

    override fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun showToast(text: String) = displayToast(text)

}