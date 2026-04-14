import SwiftUI
import sharedKit

struct ContentView: View {
    var body: some View {
        ZStack {
            Color(.systemBackground)

            NavigationView {
                SettingsScene()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .background(Color(.systemBackground))
            }
            .navigationViewStyle(.stack)
        }
    }
}
