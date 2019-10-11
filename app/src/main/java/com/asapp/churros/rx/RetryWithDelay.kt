package com.asapp.churros.rx

import io.reactivex.Scheduler
import io.reactivex.Single
import java.util.concurrent.TimeUnit

/**
 * Subscribes to an observable and retries as many times as the number of elements in the [retryList].
 * There will be a delay of N "time units" as specified by list and [timeUnit]. The list
 * should return non-negative numbers.
 *
 * @param scheduler is provided mostly for testing purposes.
 * @param callback should be provided when there's a condition that should cause a hard break given
 *     a throwable from the original observable.
 * @throws Throwable if observable fails more times than elements in sequence.
 */
fun <T> Single<T>.retryWithDelay(
    retryList: List<Int>,
    timeUnit: TimeUnit,
    scheduler: Scheduler,
    callback: (Throwable) -> Boolean = { false }
): Single<T> {
    val rangeObservable = Iterable {
        val list = retryList + -1

        list.iterator()
    }

    return retryWhen { attempts ->
        attempts
            .zipWith(rangeObservable) { throwable: Throwable, nextDelay: Int ->
                if (callback(throwable) || nextDelay < 0) {
                    throw Throwable("Too many retries", throwable)
                } else {
                    nextDelay
                }
            }
            .flatMap<Long> {
                Single.timer(it.toLong(), timeUnit, scheduler).toFlowable()
            }
    }
}