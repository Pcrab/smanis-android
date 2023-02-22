package xyz.pcrab.smanis.utils.state

import android.graphics.Rect
import androidx.window.layout.FoldingFeature
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed interface DevicePosture {
    object NormalPosture : DevicePosture

    data class BookPosture(
        val hingePosture: Rect,
    ) : DevicePosture

    data class Separating(
        val hingePosture: Rect,
        var orientation: FoldingFeature.Orientation,
    ) : DevicePosture
}

@OptIn(ExperimentalContracts::class)
fun isBookPosture(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return (foldFeature?.state == FoldingFeature.State.HALF_OPENED) && (foldFeature.orientation == FoldingFeature.Orientation.VERTICAL)
}

@OptIn(ExperimentalContracts::class)
fun isSeparating(foldFeature: FoldingFeature?): Boolean {
    contract { returns(true) implies (foldFeature != null) }
    return (foldFeature?.state == FoldingFeature.State.FLAT) && foldFeature.isSeparating
}

enum class SmanisNavigationType {
    BOTTOM_NAVIGATION, NAVIGATION_RAIL, PERMANENT_NAVIGATION_DRAWER
}

enum class SmanisContentType {
    COMPACT, EXTENDED
}