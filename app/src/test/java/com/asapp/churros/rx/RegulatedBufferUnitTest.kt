package com.asapp.churros.rx

import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.BehaviorSubject
import org.junit.Test
import java.util.concurrent.TimeUnit

class RegulatedBufferUnitTest {
    @Test
    fun testBatchSize() {
        val subject = BehaviorSubject.create<Char>()
        val scheduler = TestScheduler()
        val timeSpan = 5L
        val updatedBatchSize = 1
        val testObserver = subject
            .regulatedBuffer(3, timeSpan, TimeUnit.SECONDS, scheduler,
                { it.second },
                { it.third }) {
                Single.just(Triple(it, updatedBatchSize, timeSpan))
            }
            .test()

        subject.onNext('a')
        testObserver.assertValues()
        subject.onNext('b')
        testObserver.assertValues()
        subject.onNext('c')
        testObserver.assertValues(Triple(listOf('a', 'b', 'c'), updatedBatchSize, timeSpan))
        subject.onNext('d')
        testObserver.assertValues(Triple(listOf('a', 'b', 'c'), updatedBatchSize, timeSpan),
            Triple(listOf('d'), updatedBatchSize, timeSpan))
        subject.onNext('e')
        testObserver.assertValues(Triple(listOf('a', 'b', 'c'), updatedBatchSize, timeSpan),
            Triple(listOf('d'), updatedBatchSize, timeSpan),
            Triple(listOf('e'), updatedBatchSize, timeSpan))
    }

    @Test
    fun testTimeSpan() {
        val subject = BehaviorSubject.create<Char>()
        val scheduler = TestScheduler()
        val batchSize = 300
        val initialTimeSpan = 5L
        val updatedTimeSpan = 2L
        val timeUnit = TimeUnit.SECONDS
        val testObserver = subject
            .regulatedBuffer(batchSize, initialTimeSpan, timeUnit, scheduler,
                { it.second },
                { it.third }) {
                Single.just(Triple(it, batchSize, updatedTimeSpan))
            }
            .test()

        subject.onNext('a')
        testObserver.assertValues()
        subject.onNext('b')
        testObserver.assertValues()
        subject.onNext('c')
        scheduler.advanceTimeBy(initialTimeSpan, timeUnit)
        testObserver.assertValues(Triple(listOf('a', 'b', 'c'), batchSize, updatedTimeSpan))
        subject.onNext('d')
        testObserver.assertValues(Triple(listOf('a', 'b', 'c'), batchSize, updatedTimeSpan))
        scheduler.advanceTimeBy(updatedTimeSpan - 1, timeUnit)
        testObserver.assertValues(Triple(listOf('a', 'b', 'c'), batchSize, updatedTimeSpan))
        scheduler.advanceTimeBy(1L, timeUnit)
        testObserver.assertValues(Triple(listOf('a', 'b', 'c'), batchSize, updatedTimeSpan),
            Triple(listOf('d'), batchSize, updatedTimeSpan))
    }
}