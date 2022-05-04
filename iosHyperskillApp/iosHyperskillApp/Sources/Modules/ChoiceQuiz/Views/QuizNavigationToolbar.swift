import Foundation
import SwiftUI

private var associatedObjectHandle: UInt8 = 0

enum QuizNavigationToolbar {
    @ToolbarContentBuilder
    static func build(_ presentationMode: Binding<PresentationMode>) -> some ToolbarContent {
        ToolbarItem(placement: .navigationBarLeading) {
            Button(
                action: {
                    presentationMode.wrappedValue.dismiss()
                },
                label: {
                    Image("back_icon")
                }
            )
        }

        ToolbarItem(placement: .principal) {
            Text("Problem’s title")
                .font(.headline)
        }

        ToolbarItem(placement: .navigationBarTrailing) {
            Button(action: {}, label: { Image("info_icon") })
        }
    }

    static func applyAppearance() {
        let coloredAppearance = UINavigationBarAppearance()
        coloredAppearance.configureWithOpaqueBackground()
        coloredAppearance.backgroundColor = ColorPalette.background

        UINavigationBar.appearance().standardAppearance = coloredAppearance
        UINavigationBar.appearance().compactAppearance = coloredAppearance
        UINavigationBar.appearance().scrollEdgeAppearance = coloredAppearance
        //todo нужно еще как-то поменять высоту, а то кучу места занимает
    }
}
