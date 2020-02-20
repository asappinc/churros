package com.asapp.churros.rx

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

fun <T, S> Observable<T>.regulatedBuffer(
        initialBatchSize: Int,
        initialTimeSpan: Long,
        timeUnit: TimeUnit,
        scheduler: Scheduler,
        getNewBatchSize: (S) -> Int,
        getNewTimeSpan: (S) -> Long,
        callBack: (List<T>) -> Single<S>
): Observable<S> = lift {
    RegulatedBufferObserver(
        it, initialBatchSize, initialTimeSpan, timeUnit, scheduler,
        getNewBatchSize, getNewTimeSpan, callBack
    )
}

class RegulatedBufferObserver<T, S>(
        private val child: Observer<in S>,
        initialBatchSize: Int,
        initialTimeSpan: Long,
        private val timeUnit: TimeUnit,
        private val scheduler: Scheduler,
        private val getNewBatchSize: (S) -> Int,
        private val getNewTimeSpan: (S) -> Long,
        private val callBack: (List<T>) -> Single<S>
) : Observer<T>, Disposable {
    private val internalSubject = PublishSubject.create<T>()
    private var batchSize = initialBatchSize
    private var timeSpan = initialTimeSpan

    private var internalDisposable: Disposable? = null
    private var mainDisposable: Disposable? = null

    override fun onComplete() {
        if (mainDisposable?.isDisposed == true) {
            return
        }

        child.onComplete()
    }

    override fun onSubscribe(disposable: Disposable) {
        mainDisposable = disposable
        child.onSubscribe(this)
    }

    override fun onNext(t: T) {
        if (mainDisposable?.isDisposed == true) {
            return
        }

        if (internalDisposable == null) {
            internalDisposable = internalSubject.buffer(timeSpan, timeUnit, scheduler, batchSize)
                    .flatMapSingle { callBack(it) }
                    .subscribe ({
                        val newBatchSize = getNewBatchSize(it)
                        val newTimeSpan = getNewTimeSpan(it)
                        if (batchSize != newBatchSize || timeSpan != newTimeSpan) {
                            batchSize = newBatchSize
                            timeSpan = newTimeSpan
                            internalDisposable!!.dispose()
                            internalDisposable = null
                        }

                        child.onNext(it)
                    }) {
                        child.onError(it)
                    }
        }

        internalSubject.onNext(t)
    }

    override fun onError(throwable: Throwable) {
        if (mainDisposable?.isDisposed == true) {
            return
        }

        child.onError(throwable)
    }

    override fun isDisposed(): Boolean = mainDisposable?.isDisposed ?: false

    override fun dispose() {
        internalDisposable?.dispose()
    }

}