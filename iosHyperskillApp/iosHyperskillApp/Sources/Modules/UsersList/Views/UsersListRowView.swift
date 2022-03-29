import shared
import SwiftUI

struct UsersListRowView: View {
    let user: User

    var body: some View {
        HStack {
            Text(user.login)
            Spacer()
        }
        .padding()
    }
}
