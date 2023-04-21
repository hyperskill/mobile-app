import SwiftUI

struct StudyPlanSectionContentErrorView: View {
    let cornerRadius: CGFloat = LayoutInsets.smallInset

    let action: () -> Void

    var body: some View {
        PlaceholderView(
            configuration: .reloadContent(action: action)
        )
        .cornerRadius(cornerRadius)
    }
}

struct StudyPlanSectionContentErrorView_Previews: PreviewProvider {
    static var previews: some View {
        StudyPlanSectionContentErrorView(action: {})
            .previewLayout(.sizeThatFits)
            .padding()
            .background(Color.red)
    }
}
