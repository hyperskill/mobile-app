import SwiftUI

struct StepActionButton: View {
    let title: String

    let style: Style

    private let action: () -> Void

    init(title: String, style: Style, action: @escaping () -> Void) {
        self.title = title
        self.style = style
        self.action = action
    }

    var body: some View {
        let button = Button(title, action: action)

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
            StepActionButton(title: "Start practicing", style: .greenOutline, action: {})
            StepActionButton(title: "Start practicing", style: .greenFilled, action: {})
            StepActionButton(title: "Comments (0)", style: .violetOutline, action: {})
        }
        .padding()
    }
}
