import SwiftUI

struct StepActionButton: View {
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

struct StepActionButton_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            VStack {
                StepActionButton(title: "Start practicing", style: .greenOutline)
                StepActionButton(title: "Start practicing", style: .greenFilled)
                StepActionButton(title: "Start practicing", style: .violetFilled)
                StepActionButton(title: "Start practicing", style: .violetFilled, isLoading: true)
                StepActionButton(title: "Comments (0)", style: .violetOutline)
            }

            VStack {
                StepActionButton(title: "Start practicing", style: .greenOutline)
                StepActionButton(title: "Start practicing", style: .greenFilled)
                StepActionButton(title: "Comments (0)", style: .violetOutline)
            }
            .preferredColorScheme(.dark)
        }
        .padding()
    }
}
