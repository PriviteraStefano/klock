import SwiftUI
import ClientShared

struct AuthView: View {
    @State var authViewModel = AuthViewModel()
    @State private var email = ""
    @State private var password = ""

    var body: some View {
        ZStack {
            switch authViewModel.state {
            case .idle, .unauthenticated:
                VStack(spacing: 16) {
                    TextField("Email", text: $email)
                        .textFieldStyle(.roundedBorder)

                    SecureField("Password", text: $password)
                        .textFieldStyle(.roundedBorder)

                    Button("Login") {
                        authViewModel.processIntent(
                            intent: AuthIntent.Login(email: email, password: password)
                        )
                    }
                    .buttonStyle(.borderedProminent)
                }
                .padding()

            case .loading:
                ProgressView()

            case let .authenticated(user):
                VStack(spacing: 16) {
                    Text("Welcome, \(user.fullName)")

                    Button("Logout") {
                        authViewModel.processIntent(intent: AuthIntent.Logout())
                    }
                    .buttonStyle(.borderedProminent)
                }

            case let .error(error):
                VStack(spacing: 16) {
                    Text(error.message)
                        .foregroundColor(.red)

                    Button("Dismiss") {
                        authViewModel.processIntent(intent: AuthIntent.DismissError())
                    }
                }

            default:
                EmptyView()
            }
        }
    }
}
