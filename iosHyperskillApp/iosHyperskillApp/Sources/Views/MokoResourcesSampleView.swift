import SwiftUI
import shared

struct MokoResourcesSampleView: View {
    var body: some View {
        VStack {
            Text(
                SharedResources.strings().shared_text_example.localized()
            )
            .foregroundColor(
                Color(SharedResources.colors().colorPrimaryShared.color.uiColor)
            )

            Text(
                SharedResourcesFormattedStrings.shared.getFormattedTextExample(placeholder: "Sandwich").localized()
            )
            .foregroundColor(
                Color(SharedResources.colors().color_overlay_red.dynamicUIColor)
            )
        }
    }
}

struct MokoResourcesSampleView_Previews: PreviewProvider {
    static var previews: some View {
        MokoResourcesSampleView()
    }
}
