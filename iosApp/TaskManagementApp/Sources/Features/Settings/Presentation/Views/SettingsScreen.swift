import SwiftUI

struct SettingsScreen: View {
    let state: SettingsViewState
    let onEvent: (SettingsEvent) -> Void
    let onScreenShow: () -> Void
    
    @State private var path: [Route] = []

    var body: some View {
        NavigationStack(path: $path) {
            ZStack {
                Color(.systemBackground)
                    .ignoresSafeArea()

                VStack(spacing: 16) {
                    Text("Settings")
                        .font(.title2)
                        .fontWeight(.bold)
                        .frame(maxWidth: .infinity, alignment: .leading)

                    AvatarView(
                        imageURL: state.profile.avatarURL,
                        initials: state.profile.email ?? "NA"
                    )

                    Text(state.profile.email ?? "No email")
                        .font(.subheadline)
                        .foregroundStyle(.secondary)

                    VStack(spacing: 8) {
                        SettingsItemRow(option: .theme) { path.append(.theme) }
                        SettingsItemRow(option: .language) { path.append(.language) }
                        SettingsItemRow(option: .fontStyle) { path.append(.font) }
                        SettingsItemRow(option: .deleteAccount) { onEvent(.deleteAccount) }
                        SettingsItemRow(option: .aboutApp) { onEvent(.openAbout) }
                        SettingsItemRow(option: .logout) { onEvent(.showLogoutDialog) }
                    }

                    Spacer()

                    HStack(spacing: 4) {
                        Text("Version")
                            .fontWeight(.semibold)
                        Text(state.appVersion)
                    }
                    .font(.footnote)
                    .foregroundStyle(.secondary)
                }
                .padding(16)
            }
            .confirmationDialog(
                "Are you sure you want to logout?",
                isPresented: Binding(
                    get: { state.isLogoutDialogVisible },
                    set: { isVisible in
                        if !isVisible {
                            onEvent(.dismissLogoutDialog)
                        }
                    }
                ),
                titleVisibility: .visible
            ) {
                Button("Cancel", role: .cancel) {
                    onEvent(.dismissLogoutDialog)
                }
                Button("Logout", role: .destructive) {
                    onEvent(.confirmLogout)
                }
            }
            .onAppear(perform: onScreenShow)
            .navigationDestination(for: Route.self) { route in
                switch route {
                case .theme:
                    ThemeScreen()
                case .language:
                    LanguageScreen()
                case .font:
                    FontStyleScreen()
                default:
                    EmptyView()
                }
            }
        }
    }
}

struct SettingsScene: View {
    @StateObject private var viewModel = SettingsViewModel()

    var body: some View {
        SettingsScreen(
            state: viewModel.state,
            onEvent: viewModel.onEvent,
            onScreenShow: viewModel.onScreenShow
        )
    }
}


#Preview {
    SettingsScreen(
        // Truyền các mock data tương ứng với model của bạn vào đây
        state: SettingsViewState(
             // Ví dụ giả định cấu trúc model của bạn:
             // profile: ProfileInfo(avatarURL: nil, email: "preview@example.com"),
             // appVersion: "1.0.0"
        ),
        onEvent: { event in
            print("Preview event: \(event)")
        },
        onScreenShow: {
            print("Screen shown")
        }
    )
}
