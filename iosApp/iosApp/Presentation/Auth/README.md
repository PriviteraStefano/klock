# iOS Authentication UI

This folder contains the complete iOS authentication UI implementation using SwiftUI with Kotlin Multiplatform ViewModels.

## Architecture Overview

```
┌─────────────────────────────────────────┐
│         iOSApp.swift (Main)             │
│     - Initializes Koin DI               │
│     - Gets AuthViewModel from Koin      │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│     AuthFlowView (Navigation)           │
│     - Routes based on auth state        │
│     - Handles login/register/profile    │
└──────────────┬──────────────────────────┘
               │
      ┌────────┴────────┐
      ▼                 ▼
 LoginView        RegisterView
      │                 │
      └────────┬────────┘
               ▼
┌─────────────────────────────────────────┐
│  AuthViewModelWrapper (ObservableObject)│
│  - Wraps Kotlin AuthViewModel           │
│  - Converts StateFlow to @Published     │
│  - Provides Swift-friendly API          │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│    AuthViewModel (Kotlin/Common)        │
│    - Manages auth state & intents       │
│    - Uses AuthUseCase from Koin         │
│    - Exposes StateFlow<AuthState>       │
└─────────────────────────────────────────┘
```

## File Structure

### Core Files

1. **AuthViewModelWrapper.swift**
   - Bridges Kotlin `AuthViewModel` to SwiftUI
   - Converts Kotlin `StateFlow<AuthState>` to SwiftUI `@Published`
   - Implements `ObservableObject` for SwiftUI integration
   - Handles state mapping and lifecycle

2. **LoginView.swift**
   - Email and password input fields
   - Form validation
   - Sign-in button with loading state
   - Password visibility toggle
   - Navigation to registration screen
   - Error handling with dismissible alerts

3. **RegisterView.swift**
   - First name and last name fields
   - Email validation
   - Password confirmation
   - Terms of service agreement checkbox
   - Registration with loading states
   - Form validation before submission

4. **AuthFlowView.swift**
   - Main navigation coordinator
   - Routes between Login, Register, and Authenticated states
   - Shows profile after successful login
   - Displays user information and logout button

## How It Works

### 1. Initialization (iOSApp.swift)

```swift
@main
struct CounterApp: App {
    private let authViewModel: AuthViewModel
    
    init() {
        KoinKt.doInitKoin()  // Initialize Kotlin Koin DI
        self.authViewModel = KoinKt.getAuthViewModel()  // Get ViewModel from Koin
    }
    
    var body: some Scene {
        WindowGroup {
            AuthFlowView(authViewModel: authViewModel)
        }
    }
}
```

### 2. ViewModel Wrapping

The `AuthViewModelWrapper` observes the Kotlin `StateFlow<AuthState>` and converts it to SwiftUI's `@Published` pattern:

```swift
@MainActor
class AuthViewModelWrapper: ObservableObject {
    @Published var state: AuthStateWrapper = .idle
    private let viewModel: AuthViewModel
    
    private func observeAuthState() {
        // Poll the Kotlin StateFlow and update @Published state
        stateObservation = Timer.publish(every: 0.1, on: .main, in: .common)
            .autoconnect()
            .compactMap { [weak self] _ -> AuthStateWrapper? in
                self?.viewModel.state  // Read from Kotlin StateFlow
            }
            .sink { [weak self] newState in
                self?.state = newState  // Update @Published
            }
    }
}
```

### 3. User Interaction Flow

```
User Types Email & Password
        ↓
User Taps "Sign In"
        ↓
LoginView calls viewModel.login(email, password)
        ↓
AuthViewModelWrapper.login() calls Kotlin processIntent()
        ↓
Kotlin AuthViewModel.handleLogin() executes coroutine
        ↓
AuthViewModel updates internal _state StateFlow
        ↓
AuthViewModelWrapper observes the change
        ↓
AuthViewModelWrapper updates @Published state
        ↓
SwiftUI re-renders with new state
```

## Usage

### Basic Implementation

```swift
import SwiftUI
import common

struct MyAuthView: View {
    @StateObject var viewModel: AuthViewModelWrapper
    
    init(authViewModel: AuthViewModel) {
        _viewModel = StateObject(wrappedValue: AuthViewModelWrapper(viewModel: authViewModel))
    }
    
    var body: some View {
        switch viewModel.state {
        case .idle, .unauthenticated:
            LoginView(authViewModel: viewModel.viewModel)
        case .authenticated(let user):
            Text("Welcome, \(user.fullName)")
        case .error(let message):
            Text("Error: \(message)")
        case .loading:
            ProgressView()
        default:
            EmptyView()
        }
    }
}
```

## State Management

### Kotlin AuthState (Sealed Class)

```kotlin
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Registering : AuthState()
    data class Authenticated(val user: UserResponse) : AuthState()
    data class Error(val message: String, val code: String? = null) : AuthState()
    object Unauthenticated : AuthState()
    object LoggingOut : AuthState()
}
```

### Swift AuthStateWrapper (Enum)

```swift
enum AuthStateWrapper: Equatable {
    case idle
    case loading
    case registering
    case authenticated(user: UserWrapper)
    case error(String)
    case unauthenticated
    case loggingOut
}
```

## Intents (User Actions)

```kotlin
sealed class AuthIntent {
    data class Login(val email: String, val password: String) : AuthIntent()
    data class Register(
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String
    ) : AuthIntent()
    object Logout : AuthIntent()
    object DismissError : AuthIntent()
}
```

Called from Swift:
```swift
viewModel.login(email: "user@example.com", password: "password123")
viewModel.register(email: "...", password: "...", firstName: "...", lastName: "...")
viewModel.logout()
viewModel.dismissError()
```

## Key Features

✅ **Full Type Safety** - Kotlin types are preserved through the wrapper
✅ **Reactive Updates** - StateFlow automatically updates UI
✅ **Proper Lifecycle** - Observables are cleaned up properly
✅ **Error Handling** - Errors are displayed and dismissible
✅ **Loading States** - UI responds to loading indicators
✅ **Form Validation** - Client-side validation before submission
✅ **Responsive Design** - Works on all iOS devices
✅ **Dark Mode Support** - Uses system colors

## Integration with Koin

The Kotlin DI setup in `clientshared/src/commonMain/kotlin/org/stefanoprivitera/klock/clientshared/di/Koin.kt` automatically:

1. Scans `org.stefanoprivitera.klock` package
2. Finds `@Single` annotated classes like `AuthViewModelImpl`
3. Finds `@Module` classes for dependencies
4. Injects everything automatically

The `KoinHelper.kt` in `nativeMain` provides the Swift-friendly function:

```kotlin
fun getAuthViewModel(): AuthViewModel {
    return GlobalContext.get().get<AuthViewModel>()
}
```

## Debugging Tips

1. **StateFlow Not Updating?**
   - Check if the Kotlin ViewModel is being injected correctly
   - Verify the polling interval in `AuthViewModelWrapper.observeAuthState()`
   - Use Kotlin logs to debug: `println("State: ${viewModel.state}")`

2. **Type Mismatch Errors?**
   - Ensure `AuthState` mapping in `mapAuthState()` handles all sealed classes
   - Check generated Objective-C headers in build folder

3. **Koin Resolution Issues?**
   - Verify `@Single` and `@Module` annotations are present
   - Check package names match the `@ComponentScan`
   - Use `koinApplication.logger.info()` to debug

## Future Improvements

- [ ] Add KMP-NativeCoroutines for better coroutine handling
- [ ] Implement MVVM pattern with ViewModel for each screen
- [ ] Add animation transitions between states
- [ ] Implement biometric authentication
- [ ] Add password reset flow
- [ ] Implement OAuth/Social login

## Notes

- The polling approach (Timer) is used because SKIE is not yet available for your Kotlin version
- For production, consider upgrading Kotlin to use SKIE's AsyncSequence approach
- StateFlow reading is safe from the main thread
- All UI updates are dispatched to the main thread
