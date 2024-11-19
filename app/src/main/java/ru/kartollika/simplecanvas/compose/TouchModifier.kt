package ru.kartollika.simplecanvas.compose

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.positionChanged
import kotlin.math.PI
import kotlin.math.abs

suspend fun PointerInputScope.detectDragOrTransformGestures(
  panZoomLock: Boolean = false,
  onDragStart: (Offset) -> Unit = {},
  onDrag: (Offset) -> Unit = {},
  onDragEnd: (PointerInputChange) -> Unit = {},
  onDragCancel: () -> Unit = {},
  onTransform:
    (centroid: Offset, pan: Offset, zoom: Float, rotation: Float) -> Unit,
) {
  awaitEachGesture {
    var rotation = 0f
    var zoom = 1f
    var pan = Offset.Zero
    var pastTouchSlop = false
    val touchSlop = viewConfiguration.touchSlop
    var lockedToPanZoom = false

    var transformGestureStarted = false
    var dragGestureStarted = false

    // Wait for at least one pointer to press down, and set first contact position
    val down: PointerInputChange = awaitFirstDown(
      requireUnconsumed = false,
    )

    var pointer = down
    var pointerId = down.id
    var dragAmount = 0f
    val dragAmountThreshold = 50f

    do {
      val event = awaitPointerEvent()
      val downPointerCount = event.changes.map { it.pressed }.size
      // 2 pointers is required for transform gesture
      val gesturePointersRequirementMet = downPointerCount > 1

      // If any position change is consumed from another PointerInputChange
      // or pointer count requirement is not fulfilled
      val canceled = event.changes.any { it.isConsumed }

      if (!canceled && gesturePointersRequirementMet && dragAmountThreshold > dragAmount) {
        transformGestureStarted = true
        onDragCancel()

        val pointerInputChange =
          event.changes.firstOrNull { it.id == pointerId }
            ?: event.changes.first()

        // Next time will check same pointer with this id
        pointerId = pointerInputChange.id
        pointer = pointerInputChange

        val zoomChange = event.calculateZoom()
        val rotationChange = event.calculateRotation()
        val panChange = event.calculatePan()

        if (!pastTouchSlop) {
          zoom *= zoomChange
          rotation += rotationChange
          pan += panChange

          val centroidSize = event.calculateCentroidSize(useCurrent = false)
          val zoomMotion = abs(1 - zoom) * centroidSize
          val rotationMotion = abs(rotation * PI.toFloat() * centroidSize / 180f)
          val panMotion = pan.getDistance()

          if (zoomMotion > touchSlop ||
            rotationMotion > touchSlop ||
            panMotion > touchSlop
          ) {
            pastTouchSlop = true
            lockedToPanZoom = panZoomLock && rotationMotion < touchSlop
          }
        }

        if (pastTouchSlop) {
          val centroid = event.calculateCentroid(useCurrent = false)
          val effectiveRotation = if (lockedToPanZoom) 0f else rotationChange
          if (effectiveRotation != 0f ||
            zoomChange != 1f ||
            panChange != Offset.Zero
          ) {
            onTransform(
              centroid,
              panChange,
              zoomChange,
              effectiveRotation,
            )
          }

          event.changes.forEach {
            if (it.positionChanged()) {
              it.consume()
            }
          }
        }
      } else {
        val change = event.changes.first()
        if (!dragGestureStarted) {
          dragGestureStarted = true
          onDragStart(change.position)
        }
        if (transformGestureStarted) return@awaitEachGesture
        onDrag(change.positionChange())
        dragAmount += change.positionChange().getDistance()
      }
    } while (!canceled && event.changes.any { it.pressed })

    onDragEnd(pointer)
  }
}