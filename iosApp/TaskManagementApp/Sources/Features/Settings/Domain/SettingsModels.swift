import Foundation

struct UserProfile: Equatable {
    let email: String?
    let avatarURL: URL?
}

enum SettingsOption: CaseIterable, Identifiable {
    case theme
    case language
    case fontStyle
    case deleteAccount
    case aboutApp
    case logout

    var id: Self { self }

    var title: String {
        switch self {
        case .theme: return "Theme"
        case .language: return "Language"
        case .fontStyle: return "Font Style"
        case .deleteAccount: return "Delete Account"
        case .aboutApp: return "About App"
        case .logout: return "Logout"
        }
    }

    var systemImageName: String {
        switch self {
        case .theme: return "paintbrush"
        case .language: return "globe"
        case .fontStyle: return "textformat"
        case .deleteAccount: return "trash"
        case .aboutApp: return "info.circle"
        case .logout: return "rectangle.portrait.and.arrow.right"
        }
    }

    var isDestructive: Bool {
        self == .deleteAccount || self == .logout
    }
}
