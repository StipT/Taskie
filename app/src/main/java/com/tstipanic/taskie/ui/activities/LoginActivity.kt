package com.tstipanic.taskie.ui.activities

import android.content.Intent
import com.tstipanic.taskie.R
import com.tstipanic.taskie.common.RESPONSE_OK
import com.tstipanic.taskie.common.displayToast
import com.tstipanic.taskie.model.request.UserDataRequest
import com.tstipanic.taskie.model.response.LoginResponse
import com.tstipanic.taskie.networking.BackendFactory
import com.tstipanic.taskie.prefs.provideSharedPrefs
import com.tstipanic.taskie.ui.activities.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    private val interactor = BackendFactory.getTaskieInteractor()
    private val prefs = provideSharedPrefs()

    override fun getLayoutResourceId(): Int = R.layout.activity_login

    override fun setUpUi() {
        login.setOnClickListener { signInClicked() }
        goToLogin.setOnClickListener { goToRegistrationClicked() }
    }

    private fun signInClicked() {
        interactor.login(
            request = UserDataRequest(password = password.text.toString(), email = email.text.toString()),
            loginCallback = loginCallback()
        )
    }

    private fun loginCallback(): Callback<LoginResponse> = object : Callback<LoginResponse> {
        override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
            //TODO : handle default error 400 , 404, 500
        }

        override fun onResponse(call: Call<LoginResponse>?, response: Response<LoginResponse>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponse(response.body())
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun goToRegistrationClicked() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleOkResponse(loginReponse: LoginResponse?) {
        this.displayToast("Successfully logged in!")
        loginReponse?.token?.let { prefs.storeUserToken(it) }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleSomethingWentWrong() = this.displayToast("Something went wrong!")
}