import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.starProjectedType

fun KCallable<*>.isSubtypeOf(clazz: KClass<*>): Boolean =
    returnType.isSubtypeOf(clazz.starProjectedType)