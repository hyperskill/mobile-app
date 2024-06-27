import SwiftUI

struct StepTheoryActionButton: View {
    let title: String

    let style: Style

    var isLoading = false

    var onClick: (() -> Void)?

    var body: some View {
        let button = Button(title, action: { onClick?() })
            .overlay(
                ProgressView()
                    .opacity(isLoading ? 1 : 0)
                    .padding(.leading)
                ,
                alignment: .init(horizontal: .leading, vertical: .center)
            )
            .disabled(isLoading)
            .animation(.default, value: isLoading)

        switch style {
        case .greenOutline:
            button.buttonStyle(OutlineButtonStyle(style: .green))
        case .greenFilled:
            button.buttonStyle(RoundedRectangleButtonStyle(style: .green))
        case .violetOutline:
            button.buttonStyle(OutlineButtonStyle(style: .violet))
        case .violetFilled:
            button.buttonStyle(RoundedRectangleButtonStyle(style: .violet))
        }
    }

    enum Style {
        case greenOutline
        case greenFilled
        case violetOutline
        case violetFilled
    }
}

#if DEBUG
#Preview("Light") {
    VStack {
        StepTheoryActionButton(title: "Start practicing", style: .greenOutline)
        StepTheoryActionButton(title: "Start practicing", style: .greenFilled)
        StepTheoryActionButton(title: "Start practicing", style: .violetFilled)
        StepTheoryActionButton(title: "Start practicing", style: .violetFilled, isLoading: true)
        StepTheoryActionButton(title: "Comments (0)", style: .violetOutline)
    }
    .padding()
}

#Preview("Dark") {
    VStack {
        StepTheoryActionButton(title: "Start practicing", style: .greenOutline)
        StepTheoryActionButton(title: "Start practicing", style: .greenFilled)
        StepTheoryActionButton(title: "Comments (0)", style: .violetOutline)
    }
    .preferredColorScheme(.dark)
    .padding()
}
#endif
