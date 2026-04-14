import SwiftUI

struct AvatarView: View {
    let imageURL: URL?
    let initials: String

    var body: some View {
        ZStack {
            Circle()
                .fill(Color.gray.opacity(0.16))
                .frame(width: 84, height: 84)

            if let imageURL {
                AsyncImage(url: imageURL) { phase in
                    switch phase {
                    case .success(let image):
                        image
                            .resizable()
                            .scaledToFill()
                            .frame(width: 84, height: 84)
                            .clipShape(Circle())
                    default:
                        initialsText
                    }
                }
            } else {
                initialsText
            }
        }
    }

    private var initialsText: some View {
        Text(initials.prefix(2).uppercased())
            .font(.system(size: 28, weight: .semibold))
            .foregroundStyle(Color.primary)
    }
}
