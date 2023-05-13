import SwiftUI

extension ProjectSelectionListGridCellProjectGraduateView {
    struct Appearance {
        let spacing: CGFloat = 4

        let imageSize = CGSize(width: 16, height: 16)

        let tintColor = Color.secondaryText
    }
}

struct ProjectSelectionListGridCellProjectGraduateView: View {
    private(set) var appearance = Appearance()

    var body: some View {
        HStack(spacing: appearance.spacing) {
            Image(Images.ProjectSelectionList.projectGraduate)
                .resizable()
                .renderingMode(.template)
                .aspectRatio(contentMode: .fit)
                .foregroundColor(appearance.tintColor)
                .frame(size: appearance.imageSize)

            Text(Strings.ProjectSelectionList.List.projectGraduateTitle)
                .font(.caption)
                .foregroundColor(.secondaryText)
        }
    }
}

struct ProjectSelectionListCellProjectGraduateView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectSelectionListGridCellProjectGraduateView()

        ProjectSelectionListGridCellProjectGraduateView()
            .preferredColorScheme(.dark)
    }
}
