package org.hyperskill.app.android.core.extensions

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.zipWith
import java.util.concurrent.TimeUnit
import kotlin.math.pow
import org.hyperskill.app.android.core.extensions.rx.ObservableReduceMap

enum class RxEmpty { INSTANCE }

infix fun Completable.then(completable: Completable): Completable =
    this.andThen(completable)

infix fun <T> Completable.then(observable: Observable<T>): Observable<T> =
    this.andThen(observable)

infix fun <T> Completable.then(single: Single<T>): Single<T> =
    this.andThen(single)

infix fun <T> Observable<T>.merge(observable: Observable<T>): Observable<T> =
    Observable.merge(this, observable)

infix fun <T> Observable<T>.concat(observable: Observable<T>): Observable<T> =
    Observable.concat(this, observable)

operator fun <T> Observable<T>.plus(observable: Observable<T>): Observable<T> =
    merge(observable)

infix fun <X, Y> Observable<X>.zip(observable: Observable<Y>): Observable<Pair<X, Y>> =
    this.zipWith(observable)

class RetryWithDelay(private val retryDelayMillis: Int) :
    io.reactivex.functions.Function<Flowable<out Throwable>, Flowable<*>> {

    override fun apply(attempts: Flowable<out Throwable>): Flowable<*> =
        attempts.flatMap { Flowable.timer(retryDelayMillis.toLong(), TimeUnit.MILLISECONDS) }
}

class RetryExponential(private val maxAttempts: Int) :
    io.reactivex.functions.Function<Flowable<out Throwable>, Flowable<*>> {

    override fun apply(attempts: Flowable<out Throwable>): Flowable<*> =
        attempts.zipWith(
            Flowable.range(1, maxAttempts),
            BiFunction { t1: Throwable, t2: Int -> handleRetryAttempt(t1, t2) }
        ).flatMap { x -> x.toFlowable() }

    private fun handleRetryAttempt(throwable: Throwable, attempt: Int): Single<Long> =
        when (attempt) {
            1 -> Single.just(1L)
            maxAttempts -> Single.error(throwable)
            else -> {
                val expDelay = 2.toDouble().pow((attempt - 2).toDouble()).toLong()
                Single.timer(expDelay, TimeUnit.SECONDS)
            }
        }
}

/**
 * Performs reduce operation with [seed] over values in [sources] and emits every obtained value
 */
fun <T : Any, R : Any> reduce(
    sources: List<Single<T>>,
    seed: R,
    transform: (R, T) -> Single<R>
): Observable<R> =
    if (sources.isNotEmpty()) {
        sources
            .first()
            .flatMapObservable { item ->
                transform(seed, item)
                    .flatMapObservable { value ->
                        Observable.just(value) + reduce(
                            sources.subList(1, sources.size),
                            value,
                            transform
                        )
                    }
            }
    } else {
        Observable.empty()
    }

/**
 * Performs reduce operation with [seed] over values in current observable and emits every obtained value
 */
fun <T : Any, R : Any> Observable<T>.reduceMap(seed: R, transform: (R, T) -> R): Observable<R> =
    ObservableReduceMap<T, R>(this, seed, transform)

fun <T : Any> ObservableEmitter<T>.onErrorSafe(throwable: Throwable) {
    if (!isDisposed) {
        onError(throwable)
    }
}