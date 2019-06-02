package com.tstipanic.taskie.ui.activities

import android.content.Intent
import com.tstipanic.taskie.R
import com.tstipanic.taskie.common.RESPONSE_OK
import com.tstipanic.taskie.common.displayToast
import com.tstipanic.taskie.model.request.UserDataRequest
import com.tstipanic.taskie.model.response.RegisterResponse
import com.tstipanic.taskie.networking.BackendFactory
import com.tstipanic.taskie.ui.activities.base.BaseActivity
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : BaseActivity() {

    private val interactor = BackendFactory.getTaskieInteractor()

    override fun getLayoutResourceId(): Int = R.layout.activity_register

    override fun setUpUi() {
        register.setOnClickListener { signInClicked() }
        goToLogin.setOnClickListener { goToLoginClicked() }
    }

    private fun signInClicked() {
        interactor.register(
            UserDataRequest(
                email = email.text.toString(),
                password = password.text.toString(),
                name = name.text.toString()
            ), registerCallback()
        )
    }

    private fun registerCallback(): Callback<RegisterResponse> = object : Callback<RegisterResponse> {
        override fun onFailure(call: Call<RegisterResponse>?, t: Throwable?) {
            //TODO : handle default error 400 , 404, 500
        }

        override fun onResponse(call: Call<RegisterResponse>?, response: Response<RegisterResponse>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponse()
                    else -> handleSomethingWentWrong()
                }
            }
        }
    }

    private fun goToLoginClicked() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleOkResponse() {
        this.displayToast("Successfully registered")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleSomethingWentWrong() = this.displayToast("Something went wrong!")
}