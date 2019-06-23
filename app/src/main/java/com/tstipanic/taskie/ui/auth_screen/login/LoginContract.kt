package com.tstipanic.taskie.ui.auth_screen.login

interface LoginContract {

    interface View {

        fun setUpUi()

        fun goToRegistration()

        fun goToMain()

        fun showToast(text: String)

        fun signInClicked()
    }


    interface Presenter {

        fun setView(view: View)

        fun onSingInClicked(password: String, email: String)
    }
}