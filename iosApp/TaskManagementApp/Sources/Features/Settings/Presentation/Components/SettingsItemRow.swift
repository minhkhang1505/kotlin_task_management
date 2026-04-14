import SwiftUI

struct SettingsItemRow: View {
    let option: SettingsOption
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            HStack(spacing: 12) {
                Image(systemName: option.systemImageName)
                    .frame(width: 24)
                    .foregroundStyle(option.isDestructive ? Color.red : Color.accentColor)

                Text(option.title)
                    .foregroundStyle(option.isDestructive ? Color.red : Color.primary)

                Spacer()

                Image(systemName: "chevron.right")
                    .font(.caption)
                    .foregroundStyle(Color.secondary)
            }
            .padding(.horizontal, 14)
            .padding(.vertical, 12)
            .frame(maxWidth: .infinity)
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(Color(.secondarySystemBackground))
            )
        }
        .buttonStyle(.plain)
    }
}
