package com.asapp.churros.rx

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

fun <T> Completable.andThenRun(block: () -> Single<T>): Single<T> = andThen(Single.defer(block))

fun <T> Single<T>.runIf(condition: Boolean, block: Single<T>.() -> Single<T>): Single<T> {
    return if (condition) {
        block()
    } else {
        this
    }
}

fun <T> Observable<T>.startWith(item: T, condition: Boolean): Observable<T> = if (condition) {
    startWith(item)
} else {
    this
}