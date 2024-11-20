Sample приложение с канвасом на Compose

Позволяет рисовать пути с помощью `Modifier.detectDragGestures()`

https://t.me/mobile_cappuccino/35

https://github.com/user-attachments/assets/3c809435-0f9d-45b2-b2cc-21e12a324913

Поддерживает трансформацию холста с помощью [собственнонаписанного обработчика жестов](https://github.com/kartollikaa/SimpleCanvas/blob/main/app/src/main/java/ru/kartollika/simplecanvas/compose/TouchModifier.kt) – комбинация `detectDragGestures()` и `detectTransformGestures()`

https://t.me/mobile_cappuccino/46

https://github.com/user-attachments/assets/5aa953cf-b169-43fc-bb08-4d783cf65c2e

Интересный момент: если рядом с изменяющимся путем не положить часто изменяющуюся переменную, то мы не увидим линия рисуется сразу за пальцем

https://github.com/user-attachments/assets/4df6750a-f813-4e41-8c3b-fe547e6cef0c
