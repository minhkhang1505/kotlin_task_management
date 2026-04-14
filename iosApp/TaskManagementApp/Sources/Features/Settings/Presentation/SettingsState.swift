import Foundation

struct SettingsViewState: Equatable {
    var profile: UserProfile = UserProfile(email: "user@example.com", avatarURL: nil)
    var isLogoutDialogVisible: Bool = false
    var appVersion: String = "1.0.0"
}

enum SettingsEvent {
    case navigateTheme
    case navigateLanguage
    case navigateFontStyle
    case showLogoutDialog
    case dismissLogoutDialog
    case confirmLogout
    case deleteAccount
    case openAbout
}
