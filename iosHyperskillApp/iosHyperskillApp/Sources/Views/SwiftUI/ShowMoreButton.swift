import SwiftUI

struct ShowMoreButton: View {
    let action: () -> Void

    var body: some View {
        Button(Strings.StepQuiz.Hints.showMore) {
            action()
        }
        .font(.subheadline)
        .foregroundColor(Color(ColorPalette.primary))
    }
}

struct ShowMoreButton_Previews: PreviewProvider {
    static var previews: some View {
        ShowMoreButton(action: {})
    }
}
