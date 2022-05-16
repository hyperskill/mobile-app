import SwiftUI

struct NavigationToolbarInfoButton: ToolbarContent {
    var body: some ToolbarContent {
        ToolbarItem(placement: .navigationBarTrailing) {
            Button(action: {}, label: {
                Image("choice-quiz-info-icon")
                    .renderingMode(.template)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
                    .frame(width: 24, height: 24)
                    .foregroundColor(.secondaryText)
            })
        }
    }
}
