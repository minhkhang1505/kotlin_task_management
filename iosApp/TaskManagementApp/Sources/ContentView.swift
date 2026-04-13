import SwiftUI
import sharedKit

struct ContentView: View {

    let greeting = Greeting().greet()

    var body: some View {
        VStack(spacing: 20) {
            Text("🚀 KMP iOS App")
                .font(.title)

            Text(greeting)
        }
    }
}
