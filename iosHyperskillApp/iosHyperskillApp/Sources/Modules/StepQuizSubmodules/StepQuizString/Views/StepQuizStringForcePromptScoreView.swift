import SwiftUI

extension StepQuizStringForcePromptScoreView {
    struct Appearance {
        let checkboxIndicatorWidthHeight: CGFloat = 18
    }
}

struct StepQuizStringForcePromptScoreView: View {
    private(set) var appearance = Appearance()

    let isSelected: Bool

    let onTap: (() -> Void)

    @State private var showPopover = false

    var body: some View {
        if #available(iOS 16.4, *) {
            HStack {
                Button(action: onTap) {
                    HStack(alignment: .center, spacing: LayoutInsets.smallInset) {
                        CheckboxButton(isSelected: isSelected, onClick: onTap)
                            .frame(widthHeight: appearance.checkboxIndicatorWidthHeight)
                        Text(Strings.StepQuizString.Prompt.forceScoreCheckboxTitle)
                            .font(.subheadline)
                            .foregroundColor(.primaryText)
                    }
                }

                Button(
                    action: { showPopover = true },
                    label: { Image(systemName: "questionmark.circle") }
                )
                .font(.body)
                .popover(isPresented: $showPopover) {
                    Text(Strings.StepQuizString.Prompt.forceScoreCheckboxSubtitle)
                        .font(.caption)
                        .presentationCompactAdaptation(.popover)
                        .padding(.horizontal)
                }
            }
            .frame(maxWidth: .infinity, alignment: .leading)
        } else {
            VStack(alignment: .leading, spacing: LayoutInsets.smallInset) {
                Button(action: onTap) {
                    HStack(alignment: .center, spacing: LayoutInsets.smallInset) {
                        CheckboxButton(isSelected: isSelected, onClick: onTap)
                            .frame(widthHeight: appearance.checkboxIndicatorWidthHeight)
                        Text(Strings.StepQuizString.Prompt.forceScoreCheckboxTitle)
                            .font(.subheadline)
                            .foregroundColor(.primaryText)
                    }
                }

                Text(Strings.StepQuizString.Prompt.forceScoreCheckboxSubtitle)
                    .font(.caption)
                    .foregroundColor(.tertiaryText)
            }
        }
    }
}

#if DEBUG
#Preview {
    VStack {
        StepQuizStringForcePromptScoreView(isSelected: true, onTap: {})
        StepQuizStringForcePromptScoreView(isSelected: false, onTap: {})
    }
    .padding()
}
#endif
