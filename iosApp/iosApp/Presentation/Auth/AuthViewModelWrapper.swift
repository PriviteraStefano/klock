import SwiftUI
import Combine
import KlockFramework // Your KMP framework name

@MainActor
class AuthViewModelWrapper: ObservableObject {
    @Published var state: AuthState = .idle

    private let authViewModel: AuthViewModel
    private var cancellables = Set<AnyCancellable>()

    init(authViewModel: AuthViewModel) {
        self.authViewModel = authViewModel
        setupStateObserver()
    }

    private func setupStateObserver() {
        authViewModel.stateFlow.asPublisher()
            .receive(on: DispatchQueue.main)
            .assign(to: &$state)
    }

    func login(email: String, password: String) {
        let intent = AuthIntentLogin(email: email, password: password)
        authViewModel.processIntent(intent: intent)
    }

    func register(email: String, password: String, firstName: String, lastName: String) {
        let intent = AuthIntentRegister(
            email: email,
            password: password,
            firstName: firstName,
            lastName: lastName
        )
        authViewModel.processIntent(intent: intent)
    }

    func logout() {
        authViewModel.processIntent(intent: AuthIntentLogout())
    }

    func dismissError() {
        authViewModel.processIntent(intent: AuthIntentDismissError())
    }
}
