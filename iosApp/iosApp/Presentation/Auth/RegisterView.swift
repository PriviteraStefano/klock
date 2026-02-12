import SwiftUI
import common

struct RegisterView: View {
    @StateObject private var viewModel: AuthViewModelWrapper
    @Environment(\.dismiss) var dismiss
    
    @State private var email = ""
    @State private var password = ""
    @State private var confirmPassword = ""
    @State private var firstName = ""
    @State private var lastName = ""
    @State private var showPassword = false
    @State private var agreedToTerms = false
    
    @FocusState private var focusedField: RegisterField?
    
    enum RegisterField {
        case firstName
        case lastName
        case email
        case password
        case confirmPassword
    }
    
    init(authViewModel: AuthViewModel) {
        _viewModel = StateObject(wrappedValue: AuthViewModelWrapper(viewModel: authViewModel))
    }
    
    var isFormValid: Bool {
        !email.isEmpty &&
        !password.isEmpty &&
        password == confirmPassword &&
        !firstName.isEmpty &&
        !lastName.isEmpty &&
        agreedToTerms
    }
    
    var body: some View {
        ZStack {
            // Background
            LinearGradient(
                gradient: Gradient(colors: [
                    Color.green.opacity(0.1),
                    Color.green.opacity(0.05)
                ]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .ignoresSafeArea()
            
            VStack(spacing: 24) {
                // Header
                VStack(spacing: 8) {
                    Text("Create Account")
                        .font(.system(size: 32, weight: .bold))
                        .foregroundColor(.primary)
                    
                    Text("Join us today")
                        .font(.system(size: 16, weight: .regular))
                        .foregroundColor(.secondary)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                
                ScrollView {
                    VStack(spacing: 16) {
                        // First Name Field
                        VStack(alignment: .leading, spacing: 6) {
                            Label("First Name", systemImage: "person")
                                .font(.system(size: 14, weight: .semibold))
                                .foregroundColor(.secondary)
                            
                            TextField("John", text: $firstName)
                                .textFieldStyle(.roundedBorder)
                                .textContentType(.givenName)
                                .focused($focusedField, equals: .firstName)
                                .padding(.vertical, 4)
                        }
                        
                        // Last Name Field
                        VStack(alignment: .leading, spacing: 6) {
                            Label("Last Name", systemImage: "person.fill")
                                .font(.system(size: 14, weight: .semibold))
                                .foregroundColor(.secondary)
                            
                            TextField("Doe", text: $lastName)
                                .textFieldStyle(.roundedBorder)
                                .textContentType(.familyName)
                                .focused($focusedField, equals: .lastName)
                                .padding(.vertical, 4)
                        }
                        
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
                                    TextField("Enter password", text: $password)
                                        .textFieldStyle(.roundedBorder)
                                        .focused($focusedField, equals: .password)
                                } else {
                                    SecureField("Enter password", text: $password)
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
                        
                        // Confirm Password Field
                        VStack(alignment: .leading, spacing: 6) {
                            Label("Confirm Password", systemImage: "lock.fill")
                                .font(.system(size: 14, weight: .semibold))
                                .foregroundColor(.secondary)
                            
                            SecureField("Confirm password", text: $confirmPassword)
                                .textFieldStyle(.roundedBorder)
                                .focused($focusedField, equals: .confirmPassword)
                                .padding(.vertical, 4)
                            
                            if !confirmPassword.isEmpty && password != confirmPassword {
                                Label("Passwords don't match", systemImage: "xmark.circle.fill")
                                    .font(.system(size: 12, weight: .regular))
                                    .foregroundColor(.red)
                            }
                        }
                        
                        // Terms Agreement
                        VStack(spacing: 12) {
                            Toggle(isOn: $agreedToTerms) {
                                HStack(spacing: 6) {
                                    Text("I agree to the")
                                    NavigationLink("Terms of Service", destination: Text("Terms Screen"))
                                        .foregroundColor(.blue)
                                    Text("and")
                                    NavigationLink("Privacy Policy", destination: Text("Privacy Screen"))
                                        .foregroundColor(.blue)
                                }
                                .font(.system(size: 13, weight: .regular))
                            }
                        }
                        .padding(12)
                        .background(Color(.systemGray6))
                        .cornerRadius(8)
                    }
                }
                
                // Sign Up Button
                Button(action: {
                    viewModel.register(
                        email: email,
                        password: password,
                        firstName: firstName,
                        lastName: lastName
                    )
                }) {
                    if viewModel.isLoading {
                        ProgressView()
                            .progressViewStyle(.circular)
                            .tint(.white)
                    } else {
                        Text("Create Account")
                            .font(.system(size: 16, weight: .semibold))
                    }
                }
                .frame(maxWidth: .infinity)
                .frame(height: 54)
                .background(Color.green)
                .foregroundColor(.white)
                .cornerRadius(12)
                .disabled(!isFormValid || viewModel.isLoading)
                .opacity(!isFormValid || viewModel.isLoading ? 0.6 : 1.0)
                
                // Sign In Link
                HStack(spacing: 4) {
                    Text("Already have an account?")
                        .font(.system(size: 14, weight: .regular))
                        .foregroundColor(.secondary)
                    
                    Button(action: { dismiss() }) {
                        Text("Sign in")
                            .font(.system(size: 14, weight: .semibold))
                            .foregroundColor(.blue)
                    }
                }
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
                            Text("Registration Failed")
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
        .navigationTitle("Sign Up")
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    NavigationStack {
        RegisterView(authViewModel: AuthViewModel(authUseCase: DummyAuthUseCase()))
    }
}
