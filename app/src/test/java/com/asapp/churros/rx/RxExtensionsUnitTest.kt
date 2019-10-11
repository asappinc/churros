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
}