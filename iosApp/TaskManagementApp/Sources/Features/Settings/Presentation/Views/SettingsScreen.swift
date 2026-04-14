import SwiftUI

struct SettingsScreen: View {
    let state: SettingsViewState
    let onEvent: (SettingsEvent) -> Void
    let onScreenShow: () -> Void

    var body: some View {
        ZStack {
            Color(.systemBackground)
                .ignoresSafeArea()

            VStack(spacing: 16) {
                Text("Account")
                    .font(.title2)
                    .frame(maxWidth: .infinity, alignment: .leading)

                AvatarView(
                    imageURL: state.profile.avatarURL,
                    initials: state.profile.email ?? "NA"
                )

                Text(state.profile.email ?? "No email")
                    .font(.subheadline)
                    .foregroundStyle(.secondary)

                VStack(spacing: 8) {
                    SettingsItemRow(option: .theme) { onEvent(.navigateTheme) }
                    SettingsItemRow(option: .language) { onEvent(.navigateLanguage) }
                    SettingsItemRow(option: .fontStyle) { onEvent(.navigateFontStyle) }
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
