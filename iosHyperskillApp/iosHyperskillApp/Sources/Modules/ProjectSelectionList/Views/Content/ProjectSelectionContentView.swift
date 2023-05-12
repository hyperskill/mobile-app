import shared
import SwiftUI

extension ProjectSelectionContentView {
    struct Appearance {
        let spacing: CGFloat = 32
    }
}

struct ProjectSelectionContentView: View {
    private(set) var appearance = Appearance()

    let viewData: ProjectSelectionListFeatureViewStateContent

    var body: some View {
        ScrollView {
            VStack(spacing: appearance.spacing) {
                ProjectSelectionContentHeaderView(
                    avatarSource: viewData.trackIcon,
                    title: viewData.formattedTitle
                )
                .padding(.top, appearance.spacing)
            }
        }
    }
}

//struct ProjectSelectionContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        ProjectSelectionContentView()
//    }
//}
