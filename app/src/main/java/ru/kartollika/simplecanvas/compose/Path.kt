package ru.kartollika.simplecanvas.compose

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Path

@Immutable
data class Path(
  val path: Path,
  val drawIndex: Int = 0,
)