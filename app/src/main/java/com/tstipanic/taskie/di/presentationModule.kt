package com.tstipanic.taskie.di

import com.tstipanic.taskie.presentation.*
import com.tstipanic.taskie.ui.auth_screen.login.LoginContract
import com.tstipanic.taskie.ui.auth_screen.register.RegisterContract
import com.tstipanic.taskie.ui.details_screen.fragments.task_details_fragment.TaskDetailsContract
import com.tstipanic.taskie.ui.details_screen.fragments.update_dialog_fragment.UpdateTaskContract
import com.tstipanic.taskie.ui.task_list_screen.fragments.TasksContract
import com.tstipanic.taskie.ui.task_list_screen.fragments.add_task_dialog.AddTaskContract
import org.koin.dsl.module

val presentationModule = module {

    factory<TasksContract.Presenter> { TasksPresenter(get(), get()) }

    factory<AddTaskContract.Presenter> { AddTaskPresenter(get(), get()) }

    factory<LoginContract.Presenter> { LoginPresenter(get()) }

    factory<RegisterContract.Presenter> { RegisterPresenter(get()) }

    factory<TaskDetailsContract.Presenter> { TaskDetailsPresenter(get()) }

    factory<UpdateTaskContract.Presenter> { UpdateTaskPresenter(get(), get()) }
}
