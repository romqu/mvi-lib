import io.reactivex.Observable
import java.lang.IllegalStateException

typealias Reducer<S, T> = (currentState: S, T) -> S

class StateManager<S : MviState> constructor(
    private var state: S
) {

    init {
        state::class.findMutableProperties()
            .run {
                if (isNotEmpty()){
                    throw IllegalStateException(this)
                }
            }
    }

    fun <T : Any> reduce(value: T, reducer: Reducer<S, T>): T {

        setNewState(reducer(state, value))

        return value
    }

    fun <T : Any> map(mapper: (currentState: S) -> T): T {
        return mapper(state)
    }

    fun <T : Any> mapObservable(mapper: (currentState: S) -> Observable<T>): Observable<T> {
        return mapper(state)
    }

    private fun setNewState(newState: S) {

        synchronized(this) {

            if((state == newState).not()) state = newState
        }
    }
}