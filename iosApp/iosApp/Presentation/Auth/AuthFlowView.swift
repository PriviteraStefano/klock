import SwiftUI
import common

/// Main authentication flow view that handles navigation between login, register, and authenticated states
struct AuthFlowView: View {
    @StateObject private var viewModel: AuthFlowView
    
    init(authViewModel: AuthViewModel) {
        _viewModel = StateObject(wrappedValue: AuthViewModelWrapper(viewModel: authViewModel))
    }
    
    var body: some View {
        NavigationStack {
            Group {
                switch viewModel.state {
                case .authenticated(let user):
                    AuthenticatedView(user: user, viewModel: viewModel)
                case .idle, .unauthenticated, .error, .loggingOut:
                    LoginView(authViewModel: viewModel.viewModel)
                case .loading, .registering:
                    // Show loading while transitioning
                    VStack(spacing: 16) {
                        ProgressView()
                            .scaleEffect(1.5)
                        Text("Please wait...")
                            .foregroundColor(.secondary)
                    }
                }
            }
        }
    }
}

/// View shown when user is authenticated
struct AuthenticatedView: View {
    let user: UserWrapper
    @ObservedObject var viewModel: AuthViewModelWrapper
    
    var body: some View {
        ZStack {
            LinearGradient(
                gradient: Gradient(colors: [
                    Color.blue.opacity(0.1),
                    Color.blue.opacity(0.05)
                ]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .ignoresSafeArea()
            
            VStack(spacing: 32) {
                // Welcome Section
                VStack(spacing: 16) {
                    Image(systemName: "checkmark.circle.fill")
                        .font(.system(size: 64))
                        .foregroundColor(.green)
                    
                    VStack(spacing: 8) {
                        Text("Welcome, \(user.fullName)!")
                            .font(.system(size: 28, weight: .bold))
                            .foregroundColor(.primary)
                        
                        Text(user.email)
                            .font(.system(size: 16, weight: .regular))
                            .foregroundColor(.secondary)
                    }
                }
                
                // User Info Cards
                VStack(spacing: 12) {
                    UserInfoCard(icon: "person.fill", label: "Name", value: user.fullName)
                    UserInfoCard(icon: "envelope.fill", label: "Email", value: user.email)
                    UserInfoCard(icon: "id.card.fill", label: "User ID", value: user.id)
                }
                
                Spacer()
                
                // Logout Button
                Button(action: { viewModel.logout() }) {
                    if viewModel.isLoading {
                        ProgressView()
                            .progressViewStyle(.circular)
                            .tint(.white)
                    } else {
                        HStack(spacing: 8) {
                            Image(systemName: "power")
                            Text("Sign Out")
                                .font(.system(size: 16, weight: .semibold))
                        }
                    }
                }
                .frame(maxWidth: .infinity)
                .frame(height: 54)
                .background(Color.red)
                .foregroundColor(.white)
                .cornerRadius(12)
                .disabled(viewModel.isLoading)
            }
            .padding(20)
        }
        .navigationTitle("Profile")
        .navigationBarTitleDisplayMode(.inline)
    }
}

/// Reusable card component for displaying user information
struct UserInfoCard: View {
    let icon: String
    let label: String
    let value: String
    
    var body: some View {
        HStack(spacing: 12) {
            Image(systemName: icon)
                .font(.system(size: 18))
                .foregroundColor(.blue)
                .frame(width: 40)
            
            VStack(alignment: .leading, spacing: 4) {
                Text(label)
                    .font(.system(size: 12, weight: .semibold))
                    .foregroundColor(.secondary)
                
                Text(value)
                    .font(.system(size: 16, weight: .regular))
                    .foregroundColor(.primary)
            }
            
            Spacer()
        }
        .padding(12)
        .background(Color(.systemBackground))
        .cornerRadius(12)
        .overlay(
            RoundedRectangle(cornerRadius: 12)
                .stroke(Color.blue.opacity(0.2), lineWidth: 1)
        )
    }
}

#Preview {
    NavigationStack {
        AuthFlowView(authViewModel: AuthViewModel(authUseCase: DummyAuthUseCase()))
    }
}
