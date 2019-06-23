package com.tstipanic.taskie.presentation

import com.tstipanic.taskie.common.RESPONSE_OK
import com.tstipanic.taskie.model.interactor.Interactor
import com.tstipanic.taskie.model.request.UserDataRequest
import com.tstipanic.taskie.model.response.LoginResponse
import com.tstipanic.taskie.prefs.provideSharedPrefs
import com.tstipanic.taskie.ui.auth_screen.login.LoginContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(
    private val interactor: Interactor
) : LoginContract.Presenter {

    private lateinit var view: LoginContract.View

    override fun setView(view: LoginContract.View) {
        this.view = view
    }

    override fun onSingInClicked(password: String, email: String) {
        interactor.login(request = UserDataRequest(password = password, email = email), loginCallback = loginCallback())
    }


    private fun loginCallback(): Callback<LoginResponse> = object : Callback<LoginResponse> {
        override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
            t?.printStackTrace()
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

    private fun handleOkResponse(loginReponse: LoginResponse?) {
        view.showToast("Successfully logged in!")
        loginReponse?.token?.let { provideSharedPrefs().storeUserToken(it) }
        view.goToMain()
    }

    private fun handleSomethingWentWrong() = view.showToast("Something went wrong!")
}