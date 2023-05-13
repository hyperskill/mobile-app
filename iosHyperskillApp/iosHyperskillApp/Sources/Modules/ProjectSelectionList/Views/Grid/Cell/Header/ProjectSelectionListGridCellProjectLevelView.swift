import SwiftUI

extension ProjectSelectionListGridCellProjectLevelView {
    struct Appearance {
        let spacing: CGFloat = 4

        let imageSize = CGSize(width: 16, height: 16)
    }
}

struct ProjectSelectionListGridCellProjectLevelView: View {
    private(set) var appearance = Appearance()

    let level: SharedProjectLevelWrapper

    var body: some View {
        HStack(spacing: appearance.spacing) {
            Image(level.iconImageName)
                .resizable()
                .renderingMode(.original)
                .aspectRatio(contentMode: .fit)
                .frame(size: appearance.imageSize)

            Text(level.title)
                .font(.caption)
                .foregroundColor(.secondaryText)
        }
    }
}

struct ProjectSelectionListCellProjectLevelView_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: LayoutInsets.defaultInset) {
            ProjectSelectionListGridCellProjectLevelView(level: .easy)
            ProjectSelectionListGridCellProjectLevelView(level: .medium)
            ProjectSelectionListGridCellProjectLevelView(level: .hard)
            ProjectSelectionListGridCellProjectLevelView(level: .nightmare)
        }
        .padding()
    }
}
