import SwiftUI

struct StudyPlanSectionErrorView: View {
    let cornerRadius: CGFloat = LayoutInsets.smallInset

    let action: () -> Void

    var body: some View {
        PlaceholderView(
            configuration: .reloadContent(action: action)
        )
        .cornerRadius(cornerRadius)
    }
}

struct StudyPlanSectionErrorView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionErrorView(action: {})
            .previewLayout(.sizeThatFits)
            .padding()
            .background(Color.red)
    }
}
