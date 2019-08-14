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
    object TestResult : MviResult
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

class TestMviView: MviView<TestMviController, TestMviIntent<TestMviAction>, TestMviResult>() {

    override var controller: TestMviController
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}


    override fun render(result: TestMviResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}