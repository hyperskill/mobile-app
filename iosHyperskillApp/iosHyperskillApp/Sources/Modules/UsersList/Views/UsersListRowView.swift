import SwiftUI
import shared

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
