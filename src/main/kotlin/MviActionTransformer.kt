import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single

abstract class MviActionTransformer<S : MviState, A : MviAction, R : MviResult>(
    protected val stateManager: StateManager<S>
) : ObservableTransformer<A, R> {

    // OBSERVABLE -------------------------------------------------------------------------------------------

    protected inline fun <T : Any> Observable<T>.reduceState(
        crossinline reducer: Reducer<S, T>
    ): Observable<T> =
        map { value: T ->
            stateManager.reduce(value) { currentState, data ->
                reducer(currentState, data)
            }
        }

    protected inline fun <T : Any, R : Any> Observable<T>.mapWithState(
        crossinline mapper: (currentState: S, T) -> R
    ): Observable<R> =
        map { value: T ->
            stateManager.map { currentState: S ->
                mapper(currentState, value)
            }
        }

    protected inline fun <T : Any, R : Any> Observable<T>.flatMapWithState(
        crossinline mapper: (currentState: S, data: T) -> Observable<R>
    ): Observable<R> =
        flatMap { value: T ->
            stateManager.mapObservable { currentState ->
                mapper(currentState, value)
            }
        }

    // SINGLE -------------------------------------------------------------------------------------------

    protected inline fun <T : Any> Single<T>.reduceState(
        crossinline reducer: Reducer<S, T>
    ): Single<T> =
        map { value: T ->
            stateManager.reduce(value) { currentState, data ->
                reducer(currentState, data)
            }
        }

    protected inline fun <T : Any, R : Any> Single<T>.mapWithState(
        crossinline mapper: (currentState: S, T) -> R
    ): Single<R> =
        map { value: T ->
            stateManager.map { currentState: S ->
                mapper(currentState, value)
            }
        }
}