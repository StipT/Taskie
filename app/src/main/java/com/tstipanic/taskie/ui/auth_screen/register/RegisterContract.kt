package com.tstipanic.taskie.ui.auth_screen.register

interface RegisterContract {


    interface View {

        fun registerClicked()

        fun goToLogin()

        fun showToast(text: String)
    }


    interface Presenter {

        fun setView(view: View)

        fun onRegisterClicked(password: String, email: String, name: String)
    }
}