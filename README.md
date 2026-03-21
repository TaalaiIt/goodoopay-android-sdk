# GoodooPay SDK

Android-пакет для интеграции платежного SDK GoodooPay в мобильные приложения.

## Описание

GoodooPay SDK предоставляет готовый платежный UI и API для запуска оплаты через банковские приложения и QR-коды.

### Возможности

- Оплата через банковские приложения (MBank, DemirBank, Optima и др.)
- Генерация и отображение QR-кодов
- Автоматическая проверка статуса платежа
- Готовый UI с состояниями выбора метода, QR и обработки
- Обработка ошибок и валидация параметров

## Подключение в нативном Android-приложении

Добавьте репозитории:

```kotlin
repositories {
    google()
    mavenCentral()
    maven("https://storage.googleapis.com/download.flutter.io")
}
```

Добавьте зависимость:

```kotlin
dependencies {
   implementation("io.flutter:arm64_v8a_release:1.0.0-cf56914b326edb0ccb123ffdc60f00060bd513fa")
    implementation("io.flutter:flutter_embedding_release:1.0.0-cf56914b326edb0ccb123ffdc60f00060bd513fa")
    implementation("io.github.goodoollc:goodoo_pay_flutter:1.0.4")
    implementation("io.github.goodoollc:goodoo_pay_sdk:1.0.4")
}
```

## Вызов оплаты из Kotlin

```kotlin
val params = PaymentParams(
    apiKey = "",
    ls = "000000", // ACCOUNT_FROM_SLK
    bank = "О! банк",
    transactionId = null,
    amount = 10.0,
    description = "",
    availableMethods = listOf("bank", "qr")
)

GoodooPay.startPayment(this, params) { result ->
    when {
        result.isSuccess -> {
            // success
        }
        result.isFailed -> {
            // error
        }
        result.isCancelled -> {
            // cancelled
        }
    }
}
```

## Требования

- Android Studio Hedgehog+ / Gradle 8+
- minSdk 21+
- compileSdk 34
- Kotlin 1.9+

## Запуск примера

Пример находится в `example/`.

1. Соберите debug APK:

```bash
./gradlew :example:app:assembleDebug
```

2. Установите APK на устройство/эмулятор:

```bash
./gradlew :example:app:installDebug
```

3. Запустите приложение `GoodooPayNativeExample`.

