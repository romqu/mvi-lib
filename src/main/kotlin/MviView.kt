import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType


abstract class MviView<C : MviController<*, *, *, *>, I : MviIntent<*>, R : MviResult> {

    abstract var controller: C

    abstract fun render(result: R)
}