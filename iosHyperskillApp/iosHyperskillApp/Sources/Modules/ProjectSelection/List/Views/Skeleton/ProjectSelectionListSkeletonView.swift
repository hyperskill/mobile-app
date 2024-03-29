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

                ProjectSelectionListGridSectionSkeletonView()
            }
            .padding()
        }
        .frame(maxWidth: .infinity)
        .edgesIgnoringSafeArea(.bottom)
    }
}

struct ProjectSelectionListSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            ProjectSelectionListSkeletonView()
                .navigationTitle(Strings.ProjectSelectionList.title)
        }
    }
}
