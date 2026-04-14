import SwiftUI
import sharedKit

struct ContentView: View {
    var body: some View {
        TabView {
            Tab("Home", systemImage: "house.fill") {
                HomeScreen()
            }
            
            Tab("Settings", systemImage: "gearshape.fill") {
                SettingsScene()
            }
            
            // Tách nút Search ra khỏi cụm chính bằng role: .search
            Tab("Search", systemImage: "magnifyingglass", role: .search) {
                SearchScreen()
            }
        }
        // Kích hoạt giao diện thanh tab lơ lửng, hỗ trợ hiệu ứng kính mờ
        .tabViewStyle(.sidebarAdaptable)
    }
}
