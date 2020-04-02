package com.asapp.churros.rx

import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class RxUnitTest {
    @Test
    fun testAndThenRun() {
        var timesCalled = 0
        val testScheduler = TestScheduler()
        val testObserver = Completable.timer(5, TimeUnit.SECONDS, testScheduler)
            .andThenRun {
                Single.just(++timesCalled)
            }
            .test()

        assertEquals(0, timesCalled)
        testScheduler.advanceTimeBy(5, TimeUnit.SECONDS)
        testObserver.assertValue(1)
        assertEquals(1, timesCalled)
    }

    @Test
    fun testRunIf() {
        Single.just(1)
            .runIf(condition = true) {
                map { 2 * it }
            }
            .test()
            .assertValue(2)

        Single.just(1)
            .runIf(condition = false) {
                map { 2 * it }
            }
            .test()
            .assertValue(1)
    }

    @Test
    fun testFlatMapList() {
        fun fakeDbCall(n: Int) = Single.just(2 * n)
        fun fakeNetworkCall() = Single.just(listOf(1, 2, 3))

        val testObserver = fakeNetworkCall()
            .flatMapList { fakeDbCall(it) }
            .test()

        assertEquals(1, testObserver.valueCount())
        val values = testObserver.values().first()
        assertEquals(listOf(2, 4, 6), values)
    }
}