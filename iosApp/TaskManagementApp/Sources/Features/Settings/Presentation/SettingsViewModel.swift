import Foundation

@MainActor
final class SettingsViewModel: ObservableObject {
    @Published private(set) var state = SettingsViewState()

    func onScreenShow() {
        // In production, load profile and app version from injected use cases.
        state.appVersion = "1.0.0"
    }

    func onEvent(_ event: SettingsEvent) {
        switch event {
        case .navigateTheme, .navigateLanguage, .navigateFontStyle, .deleteAccount, .openAbout:
            break
        case .showLogoutDialog:
            state.isLogoutDialogVisible = true
        case .dismissLogoutDialog:
            state.isLogoutDialogVisible = false
        case .confirmLogout:
            state.isLogoutDialogVisible = false
            // TODO: Call sign out use case from sharedKit.
        }
    }
}
