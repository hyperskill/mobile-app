import SwiftUI

struct BackgroundView: View {
    var color = Color.background

    var body: some View {
        color.ignoresSafeArea()
    }
}

struct BackgroundView_Previews: PreviewProvider {
    static var previews: some View {
        BackgroundView()
    }
}
