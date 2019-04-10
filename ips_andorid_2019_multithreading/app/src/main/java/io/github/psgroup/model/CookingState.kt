package io.github.psgroup.model

sealed class CookingState {
    object Completed : CookingState()
    object NotStarted : CookingState()
    class InProgress(val max: Int, val current: Int) : CookingState()
    class Error(val error: CookingError) : CookingState()
}
