import SwiftUI

struct StepActionButton: View {
    let title: String

    let style: Style

    var onClick: (() -> Void)?

    var body: some View {
        let button = Button(title, action: { self.onClick?() })

        switch style {
        case .greenOutline:
            button
                .buttonStyle(
                    OutlineButtonStyle(
                        foregroundColor: Color(ColorPalette.secondary),
                        borderColor: Color(ColorPalette.secondaryAlpha35)
                    )
                )
        case .greenFilled:
            button
                .buttonStyle(
                    RoundedRectangleButtonStyle(
                        foregroundColor: Color(ColorPalette.onSecondary),
                        backgroundColor: Color(ColorPalette.secondary)
                    )
                )
        case .violetOutline:
            button.buttonStyle(OutlineButtonStyle())
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
