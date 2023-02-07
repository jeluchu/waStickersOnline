package com.jeluchu.wastickersonline.core.platform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jeluchu.jchucomponents.core.exception.Failure

abstract class BaseViewModel : ViewModel() {

    var failure: MutableLiveData<Failure> = MutableLiveData()
    var showSpinner: MutableLiveData<Boolean> = MutableLiveData()

    protected fun handleShowSpinner(show: Boolean) {
        this.showSpinner.value = show
    }

    fun handleFailure(failure: Failure) = failure.toString()

}