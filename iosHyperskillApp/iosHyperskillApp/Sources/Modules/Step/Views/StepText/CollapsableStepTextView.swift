import SwiftUI

extension CollapsableStepTextView {
    struct Appearance {
        let spacing = LayoutInsets.smallInset

        var stepTextViewAppearance = StepTextUIKitView.Appearance()
    }
}

struct CollapsableStepTextView: View {
    private(set) var appearance = Appearance()

    var text: String

    @State var isCollapsed = false

    var body: some View {
        VStack(alignment: .center, spacing: appearance.spacing) {
            Button(
                action: {
                    withAnimation {
                        isCollapsed.toggle()
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
                            .rotationEffect(.radians(isCollapsed ? .zero : (.pi / 2)))
                    }
                    .font(.headline)
                }
            )

            if !isCollapsed {
                StepTextView(
                    text: text,
                    appearance: appearance.stepTextViewAppearance,
                    onContentLoaded: nil
                )
            }
        }
    }
}

struct CollapsableStepTextView_Previews: PreviewProvider {
    static var previews: some View {
        CollapsableStepTextView(
            text: """
<p>Despite the fact that the syntax for different databases may differ, most of them have common standards.</p>
"""
        )
        .padding()
        .frame(height: 200)
    }
}
