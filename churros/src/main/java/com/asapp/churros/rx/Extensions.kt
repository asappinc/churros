package com.asapp.churros.rx

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableContainer

fun <T> Completable.andThenRun(block: () -> Single<T>): Single<T> = andThen(Single.defer(block))

fun Disposable.disposeWith(disposableContainer: DisposableContainer) {
    disposableContainer.add(this)
}

@Deprecated("Use the simpler com.asapp.churros.runIf. Will be removed in the next major version.")
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

fun <In, Out> Single<List<In>>.flatMapList(mapper: (In) -> Single<Out>): Single<List<Out>> {
    return flatMap { list ->
        val map = list.map { mapper(it).toObservable() }

        Observable
            .zip(map) { it }
            .firstOrError()
    }
        .map { arr ->
            arr.map { it as Out }.toList()
        }
}