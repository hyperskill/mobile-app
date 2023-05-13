import shared
import SwiftUI

extension ProjectSelectionListCellProjectLevelView {
    struct Appearance {
        let spacing: CGFloat = 4

        let imageSize = CGSize(width: 16, height: 16)
    }
}

struct ProjectSelectionListCellProjectLevelView: View {
    private(set) var appearance = Appearance()

    let level: ProjectLevel

    var body: some View {
        if let title = level.title,
           let imageName = level.imageName {
            HStack(spacing: appearance.spacing) {
                Image(imageName)
                    .resizable()
                    .renderingMode(.original)
                    .aspectRatio(contentMode: .fit)
                    .frame(size: appearance.imageSize)

                Text(title)
                    .font(.caption)
                    .foregroundColor(.secondaryText)
            }
        } else {
            EmptyView()
        }
    }
}

struct ProjectSelectionListCellProjectLevelView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            ProjectSelectionListCellProjectLevelView(level: .easy)
            ProjectSelectionListCellProjectLevelView(level: .medium)
            ProjectSelectionListCellProjectLevelView(level: .hard)
            ProjectSelectionListCellProjectLevelView(level: .nightmare)
        }
        .padding()
    }
}
