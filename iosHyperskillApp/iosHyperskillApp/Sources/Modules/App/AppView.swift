import shared
import SwiftUI

struct AppView: View {
    @ObservedObject private var viewModel: AppViewModel

    @Environment(\.colorScheme) private var colorScheme

    init(viewModel: AppViewModel) {
        self.viewModel = viewModel
        self.viewModel.onViewAction = self.handleViewAction(_:)
    }

    var body: some View {
        AuthSocialAssembly()
            .makeModule()
            .onAppear {
                updateProgressHUDStyle(colorScheme: colorScheme)
            }
            .onChange(of: colorScheme) { newColorScheme in
                updateProgressHUDStyle(colorScheme: newColorScheme)
            }
    }

    // MARK: Private API

    private func updateProgressHUDStyle(colorScheme: ColorScheme) {
        ProgressHUD.updateStyle(isDark: colorScheme == .dark)
    }

    private func handleViewAction(_ viewAction: AppFeatureActionViewAction) {
        print("AppView :: \(#function) viewAction = \(viewAction)")
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        AppAssembly().makeModule()
    }
}
