package com.tstipanic.taskie.ui.auth_screen.register

import android.content.Intent
import android.os.Bundle
import com.tstipanic.taskie.R
import com.tstipanic.taskie.common.displayToast
import com.tstipanic.taskie.ui.auth_screen.login.LoginActivity
import com.tstipanic.taskie.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.android.ext.android.inject


class RegisterActivity : BaseActivity(), RegisterContract.View {

    private val presenter by inject<RegisterContract.Presenter>()

    override fun getLayoutResourceId(): Int = R.layout.activity_register


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.setView(this)
    }

    override fun setUpUi() {
        register.setOnClickListener { registerClicked() }
        goToLogin.setOnClickListener { goToLoginClicked() }
    }

    private fun goToLoginClicked() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun registerClicked() {
        presenter.onRegisterClicked(email.text.toString(), password.text.toString(), name.text.toString())
    }

    override fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun showToast(text: String) = displayToast(text)
}