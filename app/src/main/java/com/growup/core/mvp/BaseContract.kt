package com.growup.core.mvp

interface BaseContract {
    interface View<T> {
        fun finishView()

        fun attachPresenter(presenter: T)
    }

    interface Presenter<V> {
        fun attachView(view: V)

        fun detachView()
    }
}