import SwiftUI

struct HomeSubheadlineView: View {
    var body: some View {
        Text(Strings.Home.keepPracticing)
            .font(.subheadline)
            .foregroundColor(.secondaryText)
    }
}

struct HomeSubheadlineView_Previews: PreviewProvider {
    static var previews: some View {
        HomeSubheadlineView()
    }
}
