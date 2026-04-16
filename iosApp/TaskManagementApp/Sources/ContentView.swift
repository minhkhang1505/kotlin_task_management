import SwiftUI
import sharedKit

struct ContentView: View {
    @State private var homePath: [Route] = []
    
    var body: some View {
        TabView {
            Tab("Home", systemImage: "house.fill") {
                NavigationStack(path: $homePath) {
                    HomeScreen()
                        .navigationDestination(for: Route.self) { route in
                            switch route {
                            case .taskDetail(let id):
                                TaskDetailScreen(id: id)
                            // We can add other routes here when needed
                            default:
                                Text("Destination not implemented")
                            }
                        }
                }
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
