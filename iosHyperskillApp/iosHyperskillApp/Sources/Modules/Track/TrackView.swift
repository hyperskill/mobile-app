import SwiftUI

struct TrackView: View {
    var body: some View {
        NavigationView {
            Text("")
                .navigationTitle(Strings.Track.title)
        }
    }
}

struct TrackView_Previews: PreviewProvider {
    static var previews: some View {
        TrackAssembly().makeModule()
    }
}
