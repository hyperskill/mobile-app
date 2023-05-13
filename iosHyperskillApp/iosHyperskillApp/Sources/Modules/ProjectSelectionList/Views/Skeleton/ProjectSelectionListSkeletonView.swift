import SwiftUI

extension ProjectSelectionListSkeletonView {
    struct Appearance {
        let listViewAppearance = ProjectSelectionListView.Appearance()
    }
}

struct ProjectSelectionListSkeletonView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        ScrollView([], showsIndicators: false) {
            VStack(spacing: appearance.listViewAppearance.spacing) {
                ProjectSelectionListHeaderSkeletonView()
                    .padding(.top, appearance.listViewAppearance.spacing)

                ProjectSelectionListGridSectionSkeletonView()
            }
            .padding([.horizontal, .bottom])
        }
        .frame(maxWidth: .infinity)
    }
}

struct ProjectSelectionSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectSelectionListSkeletonView()
    }
}
