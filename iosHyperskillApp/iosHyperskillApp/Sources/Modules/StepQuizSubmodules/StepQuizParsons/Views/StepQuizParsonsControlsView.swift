import SwiftUI

extension StepQuizParsonsControlsView {
    struct Appearance {
        let buttonStyle: OutlineButtonStyle = {
            var style = OutlineButtonStyle(style: .violet)
            style.maxWidth = nil
            style.opacityDisabled = 0.5
            return style
        }()
    }
}

struct StepQuizParsonsControlsView: View {
    private(set) var appearance = Appearance()

    let addTabControlConfiguration: ControlConfiguration
    let removeTabControlConfiguration: ControlConfiguration
    let downControlConfiguration: ControlConfiguration
    let upControlConfiguration: ControlConfiguration

    var body: some View {
        HStack(spacing: LayoutInsets.smallInset) {
            Button(
                action: addTabControlConfiguration.action,
                label: {
                    HStack(spacing: LayoutInsets.smallInset) {
                        buildSystemImage(systemName: "arrow.right.to.line")

                        Text(Strings.StepQuizParsons.tab)
                            .font(.body)
                    }
                }
            )
            .buttonStyle(appearance.buttonStyle)
            .disabled(addTabControlConfiguration.isDisabled)

            Button(
                action: removeTabControlConfiguration.action,
                label: { buildSystemImage(systemName: "arrow.left.to.line") }
            )
            .buttonStyle(appearance.buttonStyle)
            .disabled(removeTabControlConfiguration.isDisabled)

            Spacer()

            Button(
                action: downControlConfiguration.action,
                label: { buildSystemImage(systemName: "chevron.down") }
            )
            .buttonStyle(appearance.buttonStyle)
            .disabled(downControlConfiguration.isDisabled)

            Button(
                action: upControlConfiguration.action,
                label: { buildSystemImage(systemName: "chevron.up") }
            )
            .buttonStyle(appearance.buttonStyle)
            .disabled(upControlConfiguration.isDisabled)
        }
    }

    @ViewBuilder
    private func buildSystemImage(systemName: String) -> Image {
        Image(systemName: systemName)
            .renderingMode(.template)
    }

    struct ControlConfiguration {
        let action: () -> Void
        let isDisabled: Bool
    }
}

struct StepQuizParsonsControlsView_Previews: PreviewProvider {
    static var previews: some View {
        let enabledConfiguration = StepQuizParsonsControlsView.ControlConfiguration(
            action: {},
            isDisabled: false
        )
        let disabledConfiguration = StepQuizParsonsControlsView.ControlConfiguration(
            action: {},
            isDisabled: true
        )

        VStack {
            StepQuizParsonsControlsView(
                addTabControlConfiguration: enabledConfiguration,
                removeTabControlConfiguration: enabledConfiguration,
                downControlConfiguration: enabledConfiguration,
                upControlConfiguration: enabledConfiguration
            )

            StepQuizParsonsControlsView(
                addTabControlConfiguration: disabledConfiguration,
                removeTabControlConfiguration: disabledConfiguration,
                downControlConfiguration: disabledConfiguration,
                upControlConfiguration: disabledConfiguration
            )
        }
        .padding()
    }
}
