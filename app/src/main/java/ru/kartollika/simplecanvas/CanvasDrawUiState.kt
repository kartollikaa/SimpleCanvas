package ru.kartollika.simplecanvas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import ru.kartollika.simplecanvas.CanvasMode.Draw
import ru.kartollika.simplecanvas.compose.Path
import androidx.compose.ui.graphics.Path as ComposePath

@Composable
fun rememberCanvasDrawState(
): CanvasDrawUiState {
  return remember {
    CanvasDrawUiState()
  }
}

enum class CanvasMode {
  Draw,
  Transform,
}

@Stable
class CanvasDrawUiState {
  var currentPath by mutableStateOf<Path?>(null)
  var mode by mutableStateOf<CanvasMode>(Draw)

  private var lastOffset: Offset = Offset.Unspecified

  fun startDrawing(offset: Offset) {
    val path = ComposePath()
    lastOffset = offset
    path.moveTo(offset.x, offset.y)
    currentPath = Path(path)
  }

  fun reset() {
    currentPath = null
  }

  fun draw(offset: Offset) {
    if (lastOffset != Offset.Unspecified) {
      val newOffset = Offset(
        lastOffset.x + offset.x,
        lastOffset.y + offset.y
      )

      val currentPath = currentPath ?: return
      lastOffset = newOffset
      this.currentPath = currentPath.copy(
        path = currentPath.path.apply {
          lineTo(newOffset.x, newOffset.y)
        },
        drawIndex = currentPath.drawIndex + 1
      )
    }
  }
}