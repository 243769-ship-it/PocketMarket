# 🛒 PocketMarket

PocketMarket es una plataforma móvil de comercio electrónico para Android. Está diseñada para conectar a vendedores locales con compradores mediante una interfaz moderna y fluida. Soporta múltiples roles de usuario y cuenta con persistencia de datos local para el funcionamiento sin conexión.

## ✨ Características Principales
* **Gestión de Roles:** Perfiles independientes para Administradores, Vendedores y Clientes.
* **Soporte Offline (Favoritos):** Los usuarios pueden guardar productos en su lista de favoritos utilizando una base de datos local (Room) sin depender de conexión a internet.
* **Seguridad Biométrica:** Integración nativa de huella digital/reconocimiento facial para la validación del carrito de compras (Checkout).
* **Retroalimentación Háptica:** Uso de `VibratorManager` para confirmar acciones de éxito o error en la interfaz.

## 🛠️ Stack Tecnológico y Arquitectura
El proyecto fue construido siguiendo estrictamente los principios de **Clean Architecture** y el patrón de diseño **MVVM** (Model-View-ViewModel), asegurando un código escalable, testeable y con separación de responsabilidades.

* **Lenguaje:** Kotlin
* **UI Toolkit:** Jetpack Compose (Material Design 3)
* **Inyección de Dependencias:** Dagger Hilt
* **Persistencia Local:** Room Database (SQLite)
* **Consumo de API:** Ktor Client
* **Programación Asíncrona:** Corrutinas y Kotlin Flow (StateFlow / SharedFlow)

## 📁 Estructura del Proyecto (Clean Architecture)
* `:core` - Lógica base, navegación, utilidades, inyección de dependencias base y abstracciones de hardware (Biometría, Vibración).
* `:features` - Módulos independientes de la aplicación (`auth`, `admin`, `seller`, `products`, `cart`).
    * `domain/` - Casos de uso (Use Cases) y definición de repositorios (Interfaces).
    * `data/` - Implementación de repositorios, DAOs (Room) y llamadas de red.
    * `presentation/` - ViewModels, estados de UI (UiState sellados) y pantallas en Jetpack Compose.