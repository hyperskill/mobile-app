import SwiftUI

struct ContentView: View {
    var body: some View {
        UsersListAssembly().makeModule()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
