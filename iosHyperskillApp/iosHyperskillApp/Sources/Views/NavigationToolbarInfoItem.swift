import SwiftUI

struct NavigationToolbarInfoItem: ToolbarContent {
    var onClick: (() -> Void)?

    var body: some ToolbarContent {
        ToolbarItem(placement: .navigationBarTrailing) {
            Button(
                action: { onClick?() },
                label: {
                    Image(Images.StepQuiz.info)
                        .font(.body)
                        .imageScale(.large)
                }
            )
        }
    }
}
