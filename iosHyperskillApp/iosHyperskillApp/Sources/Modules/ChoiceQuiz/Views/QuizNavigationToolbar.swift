import Foundation
import SwiftUI

private var associatedObjectHandle: UInt8 = 0

enum QuizNavigationToolbar {
    @ToolbarContentBuilder
    static func build(_ presentationMode: Binding<PresentationMode>) -> some ToolbarContent {
        ToolbarItem(placement: .principal) {
            Text("Problem’s title")
                .font(.headline)
        }

        ToolbarItem(placement: .navigationBarTrailing) {
            Button(action: {}, label: {
                Image("choice-quiz-info-icon")
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 24, height: 24)
                    .foregroundColor(.secondaryText)
            })
        }
    }
}
