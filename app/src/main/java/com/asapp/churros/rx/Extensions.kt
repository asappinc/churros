package com.asapp.churros.rx

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

fun <T> Completable.andThenRun(block: () -> Single<T>): Single<T> = andThen(Single.defer(block))

fun <T> Observable<T>.startWith(item: T, condition: Boolean): Observable<T> = if (condition) {
    startWith(item)
} else {
    this
}