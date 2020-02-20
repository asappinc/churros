package com.asapp.churros.rx

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable


fun Completable.workOnMainThread(
    onComplete: () -> Unit, onError: (Throwable) -> Unit
): Disposable {
    return observeOn(AndroidSchedulers.mainThread())
        .subscribe(onComplete, onError)
}

fun <T> Maybe<T>.workOnMainThread(
    onNext: (T) -> Unit, onError: (Throwable) -> Unit
): Disposable {
    return observeOn(AndroidSchedulers.mainThread())
        .subscribe(onNext, onError)
}

fun <T> Observable<T>.workOnMainThread(
    onNext: (T) -> Unit, onError: (Throwable) -> Unit
): Disposable {
    return observeOn(AndroidSchedulers.mainThread())
        .subscribe(onNext, onError)
}