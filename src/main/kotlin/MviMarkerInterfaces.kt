interface MviAction

interface MviIntent<A : MviAction> {

    fun toAction(): A
}

interface MviResult

interface MviState