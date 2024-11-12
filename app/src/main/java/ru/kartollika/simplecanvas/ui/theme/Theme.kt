package ru.kartollika.simplecanvas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = DarkAccent,
  surface = DarkSurface,
  onSurface = DarkOnSurface
)

private val LightColorScheme = lightColorScheme(
  primary = LightAccent,
  surface = LightSurface,
  onSurface = LightOnSurface
)

class RippleTheme(val rippleColor: Color) : RippleTheme {

  @Composable
  override fun defaultColor() = rippleColor

  @Composable
  override fun rippleAlpha(): RippleAlpha = RippleAlpha(
    draggedAlpha = 0.35f,
    focusedAlpha = 0.35f,
    hoveredAlpha = 0.35f,
    pressedAlpha = 0.35f
  )
}

@Composable
fun SimpleCanvasTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}