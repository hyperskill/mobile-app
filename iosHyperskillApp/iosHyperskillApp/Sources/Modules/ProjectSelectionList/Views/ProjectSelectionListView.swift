import shared
import SwiftUI

struct ProjectSelectionListView: View {
    @StateObject var viewModel: ProjectSelectionListViewModel

    var body: some View {
        Text("Hello, World!")
    }
}

struct ProjectSelectionListView_Previews: PreviewProvider {
    static var previews: some View {
        ProjectSelectionListAssembly(trackID: 1)
            .makeModule()
    }
}
