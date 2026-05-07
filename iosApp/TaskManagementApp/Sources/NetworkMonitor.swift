import Foundation
import sharedKit

class NetworkMonitor: ObservableObject {
    @Published var isConnected: Bool = true
    @Published var wasDisconnected: Bool = false
    
    private let observer = IOSNetworkObserver()
    
    init() {
        observer.observeNetwork { [weak self] connected in
            guard let self = self else { return }
            let isNowConnected = connected.boolValue
            
            DispatchQueue.main.async {
                if !isNowConnected {
                    self.wasDisconnected = true
                }
                
                self.isConnected = isNowConnected
            }
        }
    }
    
    deinit {
        observer.stopObserving()
    }
}
