import SwiftUI

struct StepQuizSortingIcon: View {
    var disabled: Bool

    var onClick: () -> Void

    var body: some View {
        Button(action: onClick, label: {
            Image(systemName: "chevron.backward")
                .imageScale(.large)

                .frame(widthHeight: 24)
                .foregroundColor(Color(
                    disabled ? ColorPalette.onSurfaceAlpha12 : ColorPalette.onSurfaceAlpha38
                ))
        })
        .disabled(disabled)
    }
}

struct StepQuizSortingIcon_Previews: PreviewProvider {
    static var previews: some View {
        Group {
            StepQuizSortingIcon(disabled: true, onClick: {})
            StepQuizSortingIcon(disabled: false, onClick: {})
        }
        .previewLayout(.sizeThatFits)
        .padding()
    }
}
