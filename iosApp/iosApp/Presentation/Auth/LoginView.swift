import SwiftUI
import common

struct LoginView: View {
    @StateObject private var viewModel: AuthViewModelWrapper
    @State private var email = ""
    @State private var password = ""
    @State private var showPassword = false
    
    @FocusState private var focusedField: LoginField?
    
    enum LoginField {
        case email
        case password
    }
    
    init(authViewModel: AuthViewModel) {
        _viewModel = StateObject(wrappedValue: AuthViewModelWrapper(viewModel: authViewModel))
    }
    
    var body: some View {
        ZStack {
            // Background
            LinearGradient(
                gradient: Gradient(colors: [
                    Color.blue.opacity(0.1),
                    Color.blue.opacity(0.05)
                ]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .ignoresSafeArea()
            
            VStack(spacing: 24) {
                // Header
                VStack(spacing: 8) {
                    Text("Welcome Back")
                        .font(.system(size: 32, weight: .bold))
                        .foregroundColor(.primary)
                    
                    Text("Sign in to your account")
                        .font(.system(size: 16, weight: .regular))
                        .foregroundColor(.secondary)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                
                // Form
                VStack(spacing: 16) {
                    // Email Field
                    VStack(alignment: .leading, spacing: 6) {
                        Label("Email Address", systemImage: "envelope")
                            .font(.system(size: 14, weight: .semibold))
                            .foregroundColor(.secondary)
                        
                        TextField("user@example.com", text: $email)
                            .textFieldStyle(.roundedBorder)
                            .keyboardType(.emailAddress)
                            .textContentType(.emailAddress)
                            .autocorrectionDisabled()
                            .textInputAutocapitalization(.never)
                            .focused($focusedField, equals: .email)
                            .padding(.vertical, 4)
                    }
                    
                    // Password Field
                    VStack(alignment: .leading, spacing: 6) {
                        Label("Password", systemImage: "lock")
                            .font(.system(size: 14, weight: .semibold))
                            .foregroundColor(.secondary)
                        
                        HStack {
                            if showPassword {
                                TextField("Enter your password", text: $password)
                                    .textFieldStyle(.roundedBorder)
                                    .focused($focusedField, equals: .password)
                            } else {
                                SecureField("Enter your password", text: $password)
                                    .textFieldStyle(.roundedBorder)
                                    .focused($focusedField, equals: .password)
                            }
                            
                            Button(action: { showPassword.toggle() }) {
                                Image(systemName: showPassword ? "eye.slash" : "eye")
                                    .foregroundColor(.secondary)
                                    .padding(.trailing, 12)
                            }
                        }
                        .padding(.vertical, 4)
                    }
                    
                    // Forgot Password Link
                    HStack {
                        Spacer()
                        NavigationLink(destination: Text("Forgot Password Screen")) {
                            Text("Forgot Password?")
                                .font(.system(size: 13, weight: .semibold))
                                .foregroundColor(.blue)
                        }
                    }
                    .padding(.top, 4)
                }
                
                // Login Button
                Button(action: {
                    viewModel.login(email: email, password: password)
                }) {
                    if viewModel.isLoading {
                        ProgressView()
                            .progressViewStyle(.circular)
                            .tint(.white)
                    } else {
                        Text("Sign In")
                            .font(.system(size: 16, weight: .semibold))
                    }
                }
                .frame(maxWidth: .infinity)
                .frame(height: 54)
                .background(Color.blue)
                .foregroundColor(.white)
                .cornerRadius(12)
                .disabled(viewModel.isLoading || email.isEmpty || password.isEmpty)
                .opacity(viewModel.isLoading || email.isEmpty || password.isEmpty ? 0.6 : 1.0)
                
                // Divider with text
                HStack {
                    VStack { Divider() }
                    Text("Don't have an account?")
                        .font(.system(size: 14, weight: .regular))
                        .foregroundColor(.secondary)
                    VStack { Divider() }
                }
                .padding(.vertical, 8)
                
                // Sign Up Link
                NavigationLink(destination: RegisterView(authViewModel: viewModel.viewModel)) {
                    HStack(spacing: 4) {
                        Text("Sign up")
                            .font(.system(size: 16, weight: .semibold))
                            .foregroundColor(.blue)
                    }
                    .frame(maxWidth: .infinity)
                    .frame(height: 54)
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(Color.blue.opacity(0.5), lineWidth: 1.5)
                    )
                }
                
                Spacer()
            }
            .padding(20)
            
            // Error Alert
            if let errorMessage = viewModel.errorMessage {
                VStack {
                    Spacer()
                    
                    VStack(spacing: 12) {
                        HStack {
                            Image(systemName: "exclamationmark.circle.fill")
                                .foregroundColor(.red)
                            Text("Login Failed")
                                .font(.system(size: 16, weight: .semibold))
                            Spacer()
                            Button(action: { viewModel.dismissError() }) {
                                Image(systemName: "xmark")
                                    .foregroundColor(.gray)
                            }
                        }
                        
                        Text(errorMessage)
                            .font(.system(size: 14, weight: .regular))
                            .foregroundColor(.secondary)
                            .frame(maxWidth: .infinity, alignment: .leading)
                    }
                    .padding(16)
                    .background(Color(.systemBackground))
                    .cornerRadius(12)
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke(Color.red.opacity(0.3), lineWidth: 1)
                    )
                    .padding(20)
                }
                .transition(.move(edge: .bottom).combined(with: .opacity))
            }
        }
        .navigationTitle("Login")
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    NavigationStack {
        LoginView(authViewModel: AuthViewModel(authUseCase: DummyAuthUseCase()))
    }
}

// Dummy implementation for preview
class DummyAuthUseCase: AuthUseCase {
    func login(email: String, password: String) -> KotlinResult<KotlinString> {
        fatalError("Not implemented for preview")
    }
    
    func register(email: String, password: String, firstName: String, lastName: String) -> KotlinResult<KotlinString> {
        fatalError("Not implemented for preview")
    }
    
    func logout(refreshToken: String) -> KotlinResult<KotlinUnit> {
        fatalError("Not implemented for preview")
    }
    
    func getCurrentUser() -> KotlinResult<UserResponse> {
        fatalError("Not implemented for preview")
    }
}
