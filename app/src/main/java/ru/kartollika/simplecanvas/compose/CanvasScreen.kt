package ru.kartollika.simplecanvas.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.kartollika.simplecanvas.R
import ru.kartollika.simplecanvas.rememberCanvasDrawState
import kotlin.math.roundToInt

@Composable
fun CanvasScreen(
  modifier: Modifier = Modifier,
) {
  val canvasBackground = ImageBitmap.imageResource(R.drawable.canvas)
  val canvasDrawUiState = rememberCanvasDrawState()

  var zoom by remember { mutableFloatStateOf(1f) }
  var rotation by remember { mutableFloatStateOf(0f) }
  var pan by remember { mutableStateOf(Offset.Zero) }

  Canvas(
    modifier = modifier
      .graphicsLayer {
        scaleX = zoom
        scaleY = zoom
        translationX = pan.x
        translationY = pan.y
        rotationZ = rotation
      }
      .clip(RoundedCornerShape(32.dp))
      .drawBehind {
        drawImage(
          canvasBackground,
          dstSize = IntSize(
            size.width.roundToInt(),
            size.height.roundToInt()
          )
        )
      }
      .pointerInput(Unit) {
        detectDragOrTransformGestures(
          onDragStart = { offset ->
            canvasDrawUiState.startDrawing(offset)
          },
          onDragEnd = {
            canvasDrawUiState.endDrawing()
          },
          onDragCancel = {
            canvasDrawUiState.reset()
          },
          onDrag = { offset ->
            canvasDrawUiState.draw(offset)
          },
          onTransform = {
              _: Offset,
              gesturePan: Offset,
              gestureZoom: Float,
              gestureRotation,
            ->
            zoom = (zoom * gestureZoom).coerceIn(1f, 5f)
            rotation += gestureRotation
            pan += gesturePan
          },
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