import SwiftUI

struct StepActionButton: View {
    let title: String

    let style: Style

    var onClick: (() -> Void)?

    var body: some View {
        let button = Button(title, action: { onClick?() })

        switch style {
        case .greenOutline:
            button.buttonStyle(OutlineButtonStyle(style: .green))
        case .greenFilled:
            button.buttonStyle(RoundedRectangleButtonStyle(style: .green))
        case .violetOutline:
            button.buttonStyle(OutlineButtonStyle(style: .violet))
        }
    }

    enum Style {
        case greenOutline
        case greenFilled
        case violetOutline
    }
}

struct StepActionButton_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            VStack {
                StepActionButton(title: "Start practicing", style: .greenOutline)
                StepActionButton(title: "Start practicing", style: .greenFilled)
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
