package com.tstipanic.taskie.presentation

import com.tstipanic.taskie.common.RESPONSE_OK
import com.tstipanic.taskie.model.interactor.Interactor
import com.tstipanic.taskie.model.request.UserDataRequest
import com.tstipanic.taskie.model.response.RegisterResponse
import com.tstipanic.taskie.ui.auth_screen.register.RegisterContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterPresenter(
    private val interactor: Interactor
) : RegisterContract.Presenter {

    private lateinit var view: RegisterContract.View

    override fun setView(view: RegisterContract.View) {
        this.view = view
    }

    override fun onRegisterClicked(password: String, email: String, name: String) {
        interactor.register(UserDataRequest(email = email, password = password, name = name), registerCallback())
    }

    private fun registerCallback(): Callback<RegisterResponse> = object : Callback<RegisterResponse> {
        override fun onFailure(call: Call<RegisterResponse>?, t: Throwable?) {
            t?.printStackTrace()
        }

        override fun onResponse(call: Call<RegisterResponse>?, response: Response<RegisterResponse>) {
            if (response.isSuccessful) {
                when (response.code()) {
                    RESPONSE_OK -> handleOkResponse()
                    else -> handleSomethingWentWrong()
                }
            }
        }

        private fun handleOkResponse() {
            view.showToast("Successfully registered")
            view.goToLogin()
        }

        private fun handleSomethingWentWrong() = view.showToast("Something went wrong!")
    }
}