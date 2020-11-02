package com.celerocommerce.handylist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<StateEvent, ViewState> : ViewModel() {

    protected val _stateEvent = MutableLiveData<StateEvent>()

    protected val _viewState = MutableLiveData<ViewState>()

    val viewState: LiveData<ViewState>
        get() = _viewState

    val dataState: LiveData<DataState<ViewState>> = Transformations
        .switchMap(_stateEvent) { stateEvent ->
            stateEvent?.let {
                handleStateEvent(stateEvent)
            }
        }

    fun setStateEvent(event: StateEvent) {
        _stateEvent.value = event
    }

    fun setViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    fun getCurrentViewStateOrNew(): ViewState {
        val value = viewState.value ?: initViewState()
        return value
    }

    protected abstract fun handleStateEvent(stateEvent: StateEvent): LiveData<DataState<ViewState>>

    protected abstract fun initViewState(): ViewState
}