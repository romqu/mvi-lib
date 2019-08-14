import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

abstract class MviController<S : MviState, I : MviIntent<A>, A : MviAction, R : MviResult>(
    private val initialState: S,
    private val actionTransformerKClassList: List<KClass<*>>
) {

    private val actionTransformerMap: HashMap<String, ObservableTransformer<A, R>>
            by lazy(LazyThreadSafetyMode.NONE) {
                actionTransformerKClassList.map { kClass ->
                    val transformer = kClass
                        .primaryConstructor!!
                        .call(StateManager(initialState)) as ObservableTransformer<A, R>

                    val actionName = kClass
                        .supertypes[0]
                        .arguments[1]
                        .type
                        .toString()

                    Pair(actionName, transformer)
                }.run {
                    HashMap<String, ObservableTransformer<A, R>>()
                        .apply {
                            putAll(this@run.asSequence())
                        }
                }
            }

    val intent = ObservableTransformer<I, R> { upstream ->
        upstream.mapToAction()
            .model()
    }

    private fun Observable<I>.mapToAction(): Observable<A> =
        map { it.toAction() }

    private fun Observable<A>.model(): Observable<R> =
        flatMap {
            compose(actionTransformerMap[it::class.qualifiedName])
        }

}