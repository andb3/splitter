package fluent.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class FluentShapes(
    val element: Shape = RoundedCornerShape(4.dp),
    val surface: Shape = RoundedCornerShape(7.dp),
)