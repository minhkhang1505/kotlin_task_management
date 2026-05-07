import SwiftUI
import sharedKit

struct ContentView: View {
    @State private var homePath: [Route] = []
    @StateObject private var networkMonitor = NetworkMonitor()
    
    // Snackbar state for restoring connection
    @State private var showRestoredSnackbar = false
    
    var body: some View {
        ZStack(alignment: .bottom) {
            TabView {
                Tab("Home", systemImage: "house.fill") {
                    NavigationStack(path: $homePath) {
                        HomeScreen()
                            .navigationDestination(for: Route.self) { route in
                                switch route {
                                case .taskDetail(let id):
                                    TaskDetailScreen(id: id)
                                case .repeatScreen(let id):
                                    RepeatScreen(id: id)
                                default:
                                    Text("Destination not implemented")
                                }
                            }
                    }
                }
                
                Tab("Settings", systemImage: "gearshape.fill") {
                    SettingsScene()
                }
                
                Tab("Search", systemImage: "magnifyingglass", role: .search) {
                    SearchScreen()
                }
            }
            .tabViewStyle(.sidebarAdaptable)
            
            // Network Snackbars
            VStack {
                if !networkMonitor.isConnected {
                    Text("Đã mất kết nối mạng")
                        .font(.subheadline)
                        .foregroundColor(.white)
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(Color.red)
                        .cornerRadius(8)
                        .padding(.horizontal, 16)
                        .padding(.bottom, 60)
                        .transition(.move(edge: .bottom).combined(with: .opacity))
                        .animation(.easeInOut, value: networkMonitor.isConnected)
                } else if networkMonitor.wasDisconnected && showRestoredSnackbar {
                    Text("Đã khôi phục kết nối mạng")
                        .font(.subheadline)
                        .foregroundColor(.white)
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(Color.green)
                        .cornerRadius(8)
                        .padding(.horizontal, 16)
                        .padding(.bottom, 60)
                        .transition(.move(edge: .bottom).combined(with: .opacity))
                        .onAppear {
                            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                                withAnimation {
                                    showRestoredSnackbar = false
                                }
                            }
                        }
                }
            }
        }
        .onChange(of: networkMonitor.isConnected) { connected in
            if connected && networkMonitor.wasDisconnected {
                withAnimation {
                    showRestoredSnackbar = true
                }
            }
        }
    }
}
