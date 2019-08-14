import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

fun KClass<*>.findMutableProperties(): String =
    members
        .filter { it.name.startsWith("component").not() }
        .fold("") { errorMessage: String, property: KCallable<*> ->
            when (property) {
                is KMutableProperty<*> -> {
                    val tmpErrorMessage = "$errorMessage\n${property.name} is not allowed to be 'var'"
                    isSubtypeMutable(property, tmpErrorMessage)
                }

                else -> isSubtypeMutable(property, errorMessage)
            }
        }

fun isSubtypeMutable(property: KCallable<*>, errorMessage: String) =
    when {
        /*property.isSubtypeOf(MutableList::class) ->
            "$errorMessage\n${property.name} is not allowed to be mutable"
*/
        property.isSubtypeOf(Function::class) ->
            "$errorMessage\n${property.name} is not allowed to be a function"

        else -> errorMessage
    }
