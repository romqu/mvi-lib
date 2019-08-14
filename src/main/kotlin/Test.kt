import io.reactivex.Observable

fun main() {

    val clazz = TestActionTransformer::class

    val testMviController = TestMviController(TestMviState(listOf("")), listOf(clazz))

    Observable.just(TestMviIntent.TestClickIntent)
        .map { it as TestMviIntent<TestMviAction> }
        .compose(testMviController.intent)
        .subscribe(::println)


}

data class M(private val a: String)