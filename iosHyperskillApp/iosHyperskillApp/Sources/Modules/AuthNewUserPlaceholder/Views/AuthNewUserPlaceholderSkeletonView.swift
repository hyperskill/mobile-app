import SwiftUI

struct AuthNewUserPlaceholderSkeletonView: View {
    var body: some View {
        ScrollView {
            VStack {
                AuthNewUserPlaceholderHeaderView()

                VStack(spacing: LayoutInsets.smallInset) {
                    ForEach(1..<10) { _ in
                        SkeletonRoundedView()
                            .frame(height: 78)
                    }
                }
            }
            .padding()
        }
    }
}

struct AuthNewUserPlaceholderSkeletonView_Previews: PreviewProvider {
    static var previews: some View {
        AuthNewUserPlaceholderSkeletonView()
    }
}
