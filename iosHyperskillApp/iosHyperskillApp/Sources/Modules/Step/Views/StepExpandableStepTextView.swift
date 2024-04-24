import SwiftUI

extension StepExpandableStepTextView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset
    }
}

struct StepExpandableStepTextView: View {
    private(set) var appearance = Appearance()

    var title = Strings.StepQuiz.stepTextHeaderTitle
    var text: String

    @State var isExpanded = true

    let onExpandButtonTap: () -> Void

    var body: some View {
        VStack(alignment: .center, spacing: appearance.spacing) {
            Button(
                action: {
                    onExpandButtonTap()

                    withAnimation {
                        isExpanded.toggle()
                    }
                },
                label: {
                    HStack(alignment: .center) {
                        Text(title)
                            .foregroundColor(.primaryText)
                            .multilineTextAlignment(.leading)
                            .frame(maxWidth: .infinity, alignment: .leading)

                        Spacer()

                        Image(systemName: "chevron.right")
                            .imageScale(.small)
                            .aspectRatio(contentMode: .fit)
                            .rotationEffect(.radians(isExpanded ? (.pi / 2) : .zero))
                    }
                    .font(.headline)
                }
            )

            if isExpanded {
                LatexView(
                    text: text,
                    configuration: .stepText()
                )
                .transition(.scale.combined(with: .opacity))
            }
        }
    }
}

#if DEBUG
#Preview {
    StepExpandableStepTextView(
        text: """
<p>Despite the fact that the syntax for different databases may differ, most of them have common standards.</p>
""",
        onExpandButtonTap: {}
    )
    .padding()
    .frame(height: 200)
}

#Preview("Custom Title") {
    StepExpandableStepTextView(
        title: "Some custom title goes here and it very long and it very long and it very long",
        text: """
<p>Despite the fact that the syntax for different databases may differ, most of them have common standards.</p>
""",
        onExpandButtonTap: {}
    )
    .padding()
    .frame(height: 200)
}
#endif
