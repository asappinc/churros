package com.asapp.churros.rx

import io.reactivex.Observable


fun <T> Observable<T>.startWith(item: T, condition: Boolean): Observable<T> = if (condition) {
    startWith(item)
} else {
    this
}