# RotApp - Arquitectura y Estructura del Proyecto Android

## Resumen General
- **Patrón arquitectural**: MVVM (Model-View-ViewModel) + Repository Pattern
- **Framework UI**: Jetpack Compose
- **Gestión de estado**: `StateFlow`, `MutableStateFlow`
- **Navegación**: Jetpack Compose Navigation con enum `Screen`
- **Base de datos**: Room (SQLite)
- **Dependencias compartidas**: `AppContainer` (DI manual) con repositorios

---

## Estructura de Carpetas

```
app/src/main/java/com/rotapp/
├── MainActivity.kt              # Entry point, configura NavHost
├── RotAppApplication.kt         # Application class, inicializa AppContainer
├── AppViewModelProvider.kt      # Factory para ViewModels con dependencias
├── di/
│   └── AppContainer.kt          # DI container (userRepository, companyRepository)
├── data/
│   ├── model/
│   │   ├── User.kt              # data class User (id, fullName, email, roleId, isActive)
│   │   ├── Company.kt           # data class Company
│   │   └── CompanyMembership.kt # Relación User-Company
│   ├── user/
│   │   ├── UserEntity.kt        # Entity Room para usuarios
│   │   ├── UserDao.kt           # DAO Room con queries
│   │   ├── UserRepository.kt    # Interface del repositorio
│   │   └── UserRepositoryImpl.kt # Implementación con Room
│   ├── company/
│   │   ├── CompanyEntity.kt
│   │   ├── CompanyDao.kt
│   │   ├── UserCompanyEntity.kt # Tabla intermedia many-to-many
│   │   ├── UserCompanyDao.kt
│   │   ├── CompanyRepository.kt
│   │   └── CompanyRepositoryImpl.kt
│   ├── local/
│   │   └── RotAppDatabase.kt    # Database Room principal
│   └── UserMappers.kt           # Mappers Entity ↔ Model
└── ui/
    ├── theme/
    │   ├── Color.kt
    │   ├── Theme.kt
    │   └── Type.kt
    ├── welcome/
    │   ├── welcome.kt           # Composable WelcomeScreen
    │   ├── RoleSelectionScreen.kt
    │   ├── RoleSelectionViewModel.kt
    │   └── RoleSelectionUiState.kt
    ├── login/
    │   ├── loginScreen.kt       # Composable LoginScreen
    │   ├── LoginViewModel.kt
    │   └── LoginUiState.kt
    ├── register/
    │   ├── registerScreen.kt
    │   ├── RegisterViewModel.kt
    │   └── RegisterUiState.kt
    ├── company/
    │   ├── CreateCompanyScreen.kt
    │   ├── CreateCompanyViewModel.kt
    │   └── CreateCompanyUiState.kt
    └── about/
        └── AboutScreen.kt       # Vista estática
```

---

## Flujo de Navegación (MainActivity)

**Rutas disponibles** (enum `Screen`):
- `Welcome`
- `Login`
- `Register`
- `RoleSelection`
- `CreateCompany`
- `About`

**Flujo típico de onboarding**:
1. `WelcomeScreen` → usuario toca "Iniciar sesión" o "Regístrate"
2. `LoginScreen` / `RegisterScreen` → autenticación exitosa
3. `RoleSelectionScreen` → usuario elige admin/staff
4. Si `admin`: `CreateCompanyScreen` → configuración de empresa
5. Navegación regresa a `Welcome` (popUpTo con `launchSingleTop`)

**Navegación implementada con**:
```kotlin
NavHost(navController, startDestination = Screen.Welcome.name) {
    composable(Screen.Welcome.name) {
        WelcomeScreen(
            onLoginClick = { navController.navigate(Screen.Login.name) },
            // ...
        )
    }
    // ...
}
```

---

## Vistas (Composables) y Componentes

### 1. WelcomeScreen
**Propósito**: Pantalla inicial con branding y CTA principales.

**Estructura**:
- `Box` con imagen de fondo + gradiente
- Logo centrado superior
- Card inferior con botones:
  - "Iniciar sesión" (Button prominent)
  - "Regístrate"
  - "Acerca de RotApp"

**Parámetros**:
- `onLoginClick: () -> Unit`
- `onRegisterClick: () -> Unit`
- `onAboutClick: () -> Unit`

**Sin ViewModel** (vista estática con callbacks).

---

### 2. LoginScreen
**Propósito**: Formulario de autenticación (email + contraseña).

**ViewModel**: `LoginViewModel`
- `val uiState: StateFlow<LoginUiState>`
- Métodos públicos:
  - `onEmailChange(String)`
  - `onPasswordChange(String)`
  - `onLoginClick()`
  - `onForgotPasswordClick()`
  - `dismissError()`
  - `consumeSuccess()`

**Estado** (`LoginUiState`):
```kotlin
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)
```

**Estructura UI**:
- Header con gradiente + imagen de fondo + logo
- Card flotante con:
  - `TextField` para email
  - `TextField` para password (visualPasswordTransformation)
  - `Button` "Login" (muestra `CircularProgressIndicator` si `isLoading`)
  - `ErrorCard` si `errorMessage != null`
  - TextButton "¿Olvidaste tu contraseña?"
- Callbacks:
  - `onBackClick: () -> Unit`
  - `onLoginSuccess: () -> Unit` (se dispara cuando `isSuccess == true` via `LaunchedEffect`)

**Lógica de validación** (en ViewModel):
- Email y password no vacíos
- Llama `userRepository.validateCredentials(email, password)`
- Actualiza `uiState` con `isSuccess` o `errorMessage`
- Observa `userRepository.activeUserStream` para pre-llenar email

---

### 3. RegisterScreen
**Propósito**: Formulario de registro (nombre completo, email, contraseña, confirmar contraseña).

**ViewModel**: `RegisterViewModel`
- Similar a LoginViewModel

**Estado** (`RegisterUiState`):
```kotlin
data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)
```

**Estructura UI**:
- Header igual a Login (gradiente + logo)
- Card con formulario:
  - 4 `TextField` (fullName, email, password, confirmPassword)
  - `Button` "Crear cuenta"
  - `ErrorCard` si hay error
  - `SuccessCard` si `isSuccess == true` con botón "Continuar"
- Callbacks:
  - `onBackClick: () -> Unit`
  - `onNavigateToRole: () -> Unit` (al confirmar éxito)

**Validaciones** (en ViewModel):
- Nombre no vacío
- Email válido (formato `@`)
- Contraseñas coinciden
- Contraseña mínimo 6 caracteres
- Llama `userRepository.registerUser(...)`

---

### 4. RoleSelectionScreen
**Propósito**: Selector de rol (administrador/colaborador).

**ViewModel**: `RoleSelectionViewModel`
- Observa `userRepository.activeUserStream` para pre-seleccionar rol
- `selectRole(roleId: String)` actualiza `selectedRoleId`
- `confirmSelection()` llama `userRepository.updateUserRole(...)` y marca `pendingNavigation`

**Estado** (`RoleSelectionUiState`):
```kotlin
data class RoleSelectionUiState(
    val options: List<RoleOption> = listOf(...),
    val selectedRoleId: String? = null,
    val isProcessing: Boolean = false,
    val errorMessage: String? = null,
    val pendingNavigation: String? = null
)
```

**Estructura UI**:
- TopAppBar con back + "Selecciona tu rol"
- `LazyColumn` con:
  - Texto intro ("¿Cómo quieres usar RotApp?")
  - Cards seleccionables para cada `RoleOption` (admin/staff)
  - Badge "Seleccionado" si activo
  - `ErrorCard` si hay error
- Botón flotante "Continuar" (enabled solo si hay selección y no `isProcessing`)
- Callback:
  - `onRoleConfirmed: (String) -> Unit` (dispara vía `LaunchedEffect(pendingNavigation)`)

**RoleOption** (data class):
```kotlin
data class RoleOption(
    val id: String,       // "admin", "staff"
    val title: String,
    val description: String
)
```

---

### 5. CreateCompanyScreen
**Propósito**: Formulario para configurar empresa (nombre, categoría, número de empleados).

**ViewModel**: `CreateCompanyViewModel`
- Observa `userRepository.activeUserStream` para usuario activo
- Observa `companyRepository.observeCompanies(userId)` para pre-llenar
- `createCompany()` valida y llama `companyRepository.createCompany(...)`

**Estado** (`CreateCompanyUiState`):
```kotlin
data class CreateCompanyUiState(
    val name: String = "",
    val category: CompanyCategory = CompanyCategory.ALL,
    val employeesCount: Int = 10,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val generatedCompanyId: Long? = null
)
```

**CompanyCategory** (enum):
```kotlin
enum class CompanyCategory {
    ALL, HEALTH, RETAIL, SERVICES, MANUFACTURING;
    val displayName: String get() = ...
}
```

**Estructura UI**:
- TopAppBar con back + "Registrar empresa"
- `Column` scrollable con:
  - Header ("Configura tu empresa" + descripción)
  - `TextField` para nombre
  - `HorizontalPager` o `FlowRow` con chips para categorías
  - `Slider` (1-500) para empleados + Text mostrando valor
  - `FeedbackCard` (error o success)
- Botón flotante "Crear empresa" (muestra `CircularProgressIndicator` si `isLoading`)
- Callbacks:
  - `onBackClick: () -> Unit`
  - `onCompanyCreated: (Long) -> Unit` (dispara con `generatedCompanyId`)

---

### 6. AboutScreen
**Propósito**: Pantalla informativa sobre la app.

**Estructura UI**:
- TopAppBar con back + "Acerca de"
- `Column` centrada:
  - Logo `rotapp` (painterResource)
  - Título "RotApp"
  - Descripción textual
  - "Desarrollado por Dufray Manquillo"
  - "Versión 1.0.0"
- Callback:
  - `onBackClick: () -> Unit`

**Sin ViewModel** (vista estática).

---

## Capa de Datos (Repository Pattern)

### AppContainer (DI)
```kotlin
interface AppContainer {
    val userRepository: UserRepository
    val companyRepository: CompanyRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val database = Room.databaseBuilder(...)
    
    override val userRepository = UserRepositoryImpl(database.userDao())
    override val companyRepository = CompanyRepositoryImpl(
        companyDao = database.companyDao(),
        userCompanyDao = database.userCompanyDao()
    )
}
```

### UserRepository (interface)
```kotlin
interface UserRepository {
    val activeUserStream: Flow<User?>
    suspend fun getActiveUser(): User?
    suspend fun validateCredentials(email: String, password: String): Boolean
    suspend fun registerUser(fullName: String, email: String, password: String): User
    suspend fun updateUserRole(userId: Long, roleId: String)
}
```

**Implementación** (`UserRepositoryImpl`):
- Usa `UserDao` (Room) para operaciones CRUD
- Expone `activeUserStream` como `Flow<User?>` desde Room query
- Mapea `UserEntity` ↔ `User` con funciones extension

### CompanyRepository (interface)
```kotlin
interface CompanyRepository {
    fun observeCompanies(userId: Long): Flow<List<Company>>
    suspend fun createCompany(userId: Long, name: String, categoryId: String, employeesCount: Int, roleId: String): Company
}
```

**Implementación** (`CompanyRepositoryImpl`):
- Usa `CompanyDao` y `UserCompanyDao`
- Maneja relación many-to-many User-Company
- Mapea `CompanyEntity` ↔ `Company`

---

## Modelos

### User (domain model)
```kotlin
data class User(
    val id: Long,
    val fullName: String,
    val email: String,
    val roleId: String?,
    val isActive: Boolean
)
```

### Company (domain model)
```kotlin
data class Company(
    val id: Long,
    val name: String,
    val categoryId: String,
    val employeesCount: Int
)
```

### CompanyMembership
```kotlin
data class CompanyMembership(
    val company: Company,
    val role: String
)
```

---

## Entities (Room)

### UserEntity
```kotlin
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fullName: String,
    val email: String,
    val passwordHash: String,
    val roleId: String?,
    val isActive: Boolean
)
```

### CompanyEntity
```kotlin
@Entity(tableName = "companies")
data class CompanyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val categoryId: String,
    val employeesCount: Int
)
```

### UserCompanyEntity (join table)
```kotlin
@Entity(
    tableName = "user_company",
    primaryKeys = ["userId", "companyId"],
    foreignKeys = [...]
)
data class UserCompanyEntity(
    val userId: Long,
    val companyId: Long,
    val role: String
)
```

---

## Patrones de Diseño Clave

### 1. Estado unidireccional (MVVM + StateFlow)
- ViewModel expone `StateFlow<UiState>`
- Vista consume con `collectAsState()`
- Acciones (ej. `onLoginClick()`) modifican estado vía `MutableStateFlow.update { ... }`
- Vista reacciona automáticamente

### 2. Navegación declarativa
- Enum `Screen` con nombres de rutas
- `NavHost` + `composable(...)` para cada destino
- `navController.navigate(...)` con `popUpTo` para manejar back stack
- Callbacks desde pantallas hijas hacia `MainActivity`

### 3. Validaciones en ViewModel
- Métodos `onXxxChange()` limpian mensajes de error
- Lógica centralizada (validación email, contraseñas coinciden, etc.)
- Estado reactivo: botones deshabilitados si `isLoading` o campos inválidos

### 4. Repository como fuente única de verdad
- ViewModels no acceden directamente a DAO/DB
- Repositorios exponen `Flow` para datos reactivos
- Operaciones suspend para escrituras
- Mappers separan entities de models de dominio

---

## Guía para Migrar a KMP (Kotlin Multiplatform)

### Qué mover a `commonMain`

1. **Modelos de dominio** → `data class User`, `Company`, enums
2. **Interfaces de repositorio** → `UserRepository`, `CompanyRepository` (con `Flow` y `suspend`)
3. **ViewModels** → toda la lógica en `StateFlow<UiState>`, migrando a `commonMain/presentation`
4. **Validaciones y casos de uso** → funciones puras (ej. validar email, contraseñas coinciden)

### Qué dejar platform-specific

1. **UIs** → Compose (androidMain) y SwiftUI (iosMain) consumiendo ViewModels compartidos
2. **Implementaciones de repositorio** → `expect/actual` para Room (Android) vs CoreData/UserDefaults (iOS)
3. **Navegación** → Compose Navigation (Android) vs NavigationStack (iOS)
4. **Assets** → drawable (Android) vs Assets.xcassets (iOS)

### Estructura propuesta KMP

```
shared/
├── src/
│   ├── commonMain/
│   │   └── kotlin/com/rotapp/
│   │       ├── domain/
│   │       │   ├── model/          # User, Company, enums
│   │       │   └── usecase/        # Validaciones, reglas
│   │       ├── data/
│   │       │   └── repository/     # Interfaces UserRepository, CompanyRepository
│   │       └── presentation/
│   │           └── viewmodel/      # LoginViewModel, RegisterViewModel (StateFlow)
│   ├── androidMain/
│   │   └── kotlin/com/rotapp/
│   │       └── data/
│   │           └── repository/     # UserRepositoryImpl (Room), actual implementations
│   └── iosMain/
│       └── kotlin/com/rotapp/
│           └── data/
│               └── repository/     # actual implementations (iOS storage)
```

### Equivalencias Compose ↔ SwiftUI (ya en Compose)

Como el proyecto ya usa Compose, solo se necesita:
- Mantener las UIs Compose en `androidMain`
- Crear vistas SwiftUI equivalentes en iOS app, consumiendo ViewModels compartidos
- Usar KMP-NativeCoroutines para exponer `StateFlow` a Swift como observables

---

## Checklist para Implementación KMP

- [ ] Crear módulo `shared` en proyecto KMP
- [ ] Mover modelos de dominio a `commonMain/kotlin/.../domain/model`
- [ ] Definir interfaces de repositorio en `commonMain/data/repository`
- [ ] Migrar ViewModels a `commonMain/presentation/viewmodel` (StateFlow)
- [ ] Implementar `expect/actual` para persistencia (Room vs iOS)
- [ ] Configurar navegación iOS en SwiftUI consumiendo shared ViewModels
- [ ] Agregar KMP-NativeCoroutines para interop StateFlow ↔ Swift
- [ ] Probar flujo completo en emulador Android
- [ ] Generar framework iOS y probar en simulador
- [ ] Sincronizar assets entre plataformas

---

## Recursos y Referencias

- **Proyecto Android actual**: `app/src/main/java/com/rotapp/`
- **Documentación KMP**: https://kotlinlang.org/docs/multiplatform.html
- **Jetpack Compose**: https://developer.android.com/jetpack/compose
- **Room Database**: https://developer.android.com/training/data-storage/room
- **KMP-NativeCoroutines**: https://github.com/rickclephas/KMP-NativeCoroutines

---

**Notas finales para migración a KMP**:
- Este proyecto Android ya tiene arquitectura limpia (MVVM + Repository)
- La lógica (ViewModels, validaciones, repositorios) puede moverse directamente a `commonMain`
- Las UIs Compose se quedan en Android; en iOS se recrean en SwiftUI consumiendo los mismos ViewModels
- Room se queda en `androidMain`; en `iosMain` usa CoreData/SQLDelight o almacenamiento nativo
- Los mockups visuales + este documento permiten a Copilot generar las pantallas iOS en SwiftUI
