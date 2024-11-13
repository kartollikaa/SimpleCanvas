package ru.kartollika.simplecanvas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import ru.kartollika.simplecanvas.compose.Path
import androidx.compose.ui.graphics.Path as ComposePath

@Composable
fun rememberCanvasDrawState(
): CanvasDrawUiState {
  return remember {
    CanvasDrawUiState()
  }
}

@Stable
class CanvasDrawUiState {
  var paths: ImmutableList<Path> by mutableStateOf(persistentListOf())
  var currentPath by mutableStateOf<Path?>(null)

  private var lastOffset: Offset = Offset.Unspecified

  fun startDrawing(offset: Offset) {
    val path = ComposePath()
    lastOffset = offset
    path.moveTo(offset.x, offset.y)
    currentPath = Path(path)
  }

  fun draw(offset: Offset) {
    if (lastOffset != Offset.Unspecified) {
      val newOffset = Offset(
        lastOffset.x + offset.x,
        lastOffset.y + offset.y
      )

      lastOffset = newOffset

      val currentPath = currentPath ?: return
      currentPath.path.lineTo(newOffset.x, newOffset.y)
      this.currentPath = currentPath.copy(
        drawIndex = currentPath.drawIndex + 1
      )
    }
  }

  fun endDrawing() {
    val path = currentPath ?: return
    paths = paths
      .toMutableList()
      .apply {
        add(path)
      }
      .toImmutableList()
  }

  fun reset() {
    currentPath = null
  }
}