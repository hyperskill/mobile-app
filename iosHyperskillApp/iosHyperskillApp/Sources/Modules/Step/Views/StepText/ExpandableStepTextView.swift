import SwiftUI

extension ExpandableStepTextView {
    struct Appearance {
        let spacing = LayoutInsets.defaultInset

        var stepTextViewAppearance = StepTextUIKitView.Appearance()
    }
}

struct ExpandableStepTextView: View {
    private(set) var appearance = Appearance()

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
                        Text(Strings.StepQuiz.stepTextHeaderTitle)
                            .foregroundColor(.primaryText)
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
                StepTextView(
                    text: text,
                    appearance: appearance.stepTextViewAppearance,
                    onContentLoaded: nil
                )
            }
        }
    }
}

struct ExpandableStepTextView_Previews: PreviewProvider {
    static var previews: some View {
        ExpandableStepTextView(
            text: """
<p>Despite the fact that the syntax for different databases may differ, most of them have common standards.</p>
""",
            onExpandButtonTap: {}
        )
        .padding()
        .frame(height: 200)
    }
}
