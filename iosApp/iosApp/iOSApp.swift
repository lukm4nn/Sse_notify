import SwiftUI

@main
struct iOSApp: App {

    @Environment(\.scenePhase) var scenePhase

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
        .onChange(of: scenePhase) { phase in
            if phase == .active {
                SseManager.shared.stop()
            }
        }
    }
}