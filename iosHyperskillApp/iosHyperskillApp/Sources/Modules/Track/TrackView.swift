import SwiftUI

struct TrackView: View {
    var body: some View {
        NavigationView {
            Text("")
                .navigationTitle(Strings.Track.title)
        }
        .navigationViewStyle(StackNavigationViewStyle())
    }
}

struct TrackView_Previews: PreviewProvider {
    static var previews: some View {
        TrackAssembly().makeModule()
    }
}
