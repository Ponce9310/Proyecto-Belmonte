# 🚐 Proyecto Belmonte

**Aplicación Android para la gestión de traslados.**

## 📌 Descripción

Proyecto desarrollado en Kotlin para Android, orientado a facilitar la gestión de usuarios y traslados mediante una interfaz que permite navegar por las distintas funcionalidades de la aplicación.

El objetivo es ofrecer una experiencia sencilla e intuitiva para consultar y administrar la información relacionada con los traslados.

## ✨ Funcionalidades

- 👤 Gestión de usuarios.
- 🚐 Administración de traslados.
- 🧭 Navegación entre las pantallas de la aplicación.
- 💾 Persistencia de datos mediante Room.
- 🏗️ Organización del código mediante capas y componentes de la arquitectura Android.
- 🧪 Pruebas unitarias y pruebas instrumentadas.

> Las funcionalidades disponibles dependen de la implementación actual del proyecto.

## 🛠️ Tecnologías utilizadas

- **Lenguaje:** Kotlin.
- **Plataforma:** Android.
- **Sistema de construcción:** Gradle.
- **Base de datos local:** Room.
- **Arquitectura:** ViewModel y patrón Repository.
- **Control de versiones:** Git y GitHub.

## 📂 Estructura del proyecto

```text
Belmonte/
├── app/
│   ├── src/
│   │   ├── main/
│   │   ├── test/
│   │   └── androidTest/
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── README.md
```

- `main/`: código fuente y recursos de la aplicación.
- `test/`: pruebas unitarias.
- `androidTest/`: pruebas instrumentadas para Android.
- `gradle/`: archivos de configuración del sistema de construcción.

## ▶️ Cómo ejecutar el proyecto

1. Instala Android Studio.
2. Abre el proyecto desde la carpeta `Belmonte`.
3. Espera a que Gradle sincronice las dependencias.
4. Conecta un dispositivo Android o inicia un emulador.
5. Ejecuta la aplicación mediante el botón **Run ▶** de Android Studio.

También puedes compilar el proyecto desde CMD, ubicado en la carpeta raíz:

```cmd
gradlew.bat assembleDebug
```

## 🧪 Pruebas

Para ejecutar las pruebas unitarias desde CMD:

```cmd
gradlew.bat test
```

Las pruebas instrumentadas requieren un dispositivo Android o emulador configurado.

## 🌿 Control de versiones

El proyecto se administra mediante Git y está alojado en GitHub. El historial incluye **7 commits**, organizados para reflejar las distintas etapas de desarrollo.

## 🎓 Propósito académico

Este proyecto permite aplicar conocimientos de desarrollo Android con Kotlin, persistencia de datos, arquitectura de software, navegación entre pantallas, pruebas y control de versiones.

## 👨‍💻 Autor

**Dennis Ponce**

---

*Proyecto Belmonte — Aplicación Android para la gestión de traslados.*
