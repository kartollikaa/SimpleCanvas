package ru.kartollika.simplecanvas.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import ru.kartollika.simplecanvas.R
import ru.kartollika.simplecanvas.rememberCanvasDrawState

@Composable
fun CanvasScreen(
  modifier: Modifier = Modifier,
) {
  val canvasBackground = ImageBitmap.imageResource(R.drawable.canvas)

  val canvasDrawUiState = rememberCanvasDrawState()
  Canvas(
    modifier = modifier
      .drawBehind {
        drawImage(canvasBackground)
      }
      .pointerInput(Unit) {
        detectDragGestures(
          onDragStart = { offset ->
            canvasDrawUiState.startDrawing(offset)
          },
          onDragEnd = {
            canvasDrawUiState.endDrawing()
          },
          onDragCancel = {
            canvasDrawUiState.reset()
          },
          onDrag = { _, offset ->
            canvasDrawUiState.draw(offset)
          }
        )
      },
  ) {
    canvasDrawUiState.paths.forEach { path ->
      drawPath(
        path = path.path,
        color = Color.Blue,
        style = Stroke(
          width = 10f,
          cap = StrokeCap.Round,
          join = StrokeJoin.Round
        ),
      )
    }


    canvasDrawUiState.currentPath?.let { pathWithProperties ->
      drawPath(
        path = pathWithProperties.path,
        color = Color.Blue,
        style = Stroke(
          width = 10f,
          cap = StrokeCap.Round,
          join = StrokeJoin.Round
        ),
      )
    }
  }
}