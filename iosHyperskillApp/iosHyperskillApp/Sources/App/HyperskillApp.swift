import SwiftUI

@main
struct HyperskillApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

	var body: some Scene {
        WindowGroup<AppView> {
            AppAssembly().makeModule()
        }
	}
}
