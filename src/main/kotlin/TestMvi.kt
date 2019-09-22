import io.reactivex.Observable
import io.reactivex.ObservableSource
import kotlin.reflect.KClass

sealed class TestMviIntent<A : TestMviAction> : MviIntent<A> {

    object TestClickIntent : TestMviIntent<TestMviAction.TestReloadAction>() {

        override fun toAction(): TestMviAction.TestReloadAction =
            TestMviAction.TestReloadAction
    }
}

sealed class TestMviAction : MviAction {
    object TestReloadAction : TestMviAction()
}

sealed class TestMviResult : MviResult {
    object TestResult : TestMviResult()
}

data class TestMviState(
    val a: List<String>
) : MviState


class TestActionTransformer(
    stateManager: StateManager<TestMviState>
) : MviActionTransformer<TestMviState, TestMviAction.TestReloadAction, TestMviResult.TestResult>(stateManager) {

    override fun apply(upstream: Observable<TestMviAction.TestReloadAction>)
            : ObservableSource<TestMviResult.TestResult> =
        upstream.map { TestMviResult.TestResult }
}

class TestMviController(
    initialState: TestMviState,
    actionTransformerKClassList: List<KClass<*>>
) : MviController<TestMviState, TestMviIntent<TestMviAction>, TestMviAction, TestMviResult>(
    initialState, actionTransformerKClassList
)

class TestMviView: MviView<TestMviController, TestMviResult>() {

    override var controller: TestMviController = TestMviController(
        TestMviState(listOf("")),
        listOf(TestActionTransformer::class))

    init {
        Observable.just(TestMviIntent.TestClickIntent)
            .map { it as TestMviIntent<TestMviAction> }
            .onIntent()
    }



    override fun render(result: TestMviResult) =
        when(result){
            TestMviResult.TestResult -> println(result)
        }

    private fun Observable<TestMviIntent<TestMviAction>>.onIntent() =
        compose(controller.intent)
            .subscribe(::render)

}