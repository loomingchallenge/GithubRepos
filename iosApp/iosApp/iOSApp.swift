import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

	init() {
        AppKt.doInitApp()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
