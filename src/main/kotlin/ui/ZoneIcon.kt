package ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import data.Zone


@Composable
fun ZoneIcon(
    zone: Zone,
    modifier: Modifier = Modifier,
    iconSize: DpSize = DpSize(24.dp, 24.dp),
) {
    val contentColor = LocalContentColor.current
    val baseAlpha = ContentAlpha.disabled
    val density = LocalDensity.current
    Box(
        modifier = modifier.size(iconSize).drawBehind {
            scale(scaleY = 9f / 16, scaleX = 1f) {
                clipPath(
                    path = Path().apply {
                        addRoundRect(RoundRect(
                            left = 0f,
                            top = 0f,
                            right = size.width,
                            bottom = size.height,
                            radiusX = with(density) { 2.dp.toPx() },
                            radiusY = with(density) { 2.dp.toPx() * (16f/9) }
                        ))
                    }
                ) {
                    drawRect(
                        color = contentColor.copy(alpha = baseAlpha),
                    )
                    when (zone) {
                        is Zone.Horizontal -> drawRect(
                            topLeft = Offset(x = size.width * zone.startPercent, y = 0f),
                            size = this.size.copy(width = this.size.width * zone.percentSize),
                            color = contentColor
                        )
                        is Zone.Vertical -> drawRect(
                            topLeft = Offset(x = 0f, y = size.height * zone.startPercent),
                            size = this.size.copy(height = this.size.height * zone.percentSize),
                            color = contentColor
                        )
                    }
                }
            }

        }
    )
}
