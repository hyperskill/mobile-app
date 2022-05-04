import SwiftUI

struct CorrectAlertView: View {
    var body: some View {
        HStack {
            Image("check_icon")
            Text("You’re absolutely correct!")
                .foregroundColor(Color(ColorPalette.secondary))
                .font(.body)
            Spacer()
        }
        .padding()
        .background(Color(ColorPalette.green200Alpha12))
        .cornerRadius(8)
    }
}

struct CorrectAlertView_Previews: PreviewProvider {
    static var previews: some View {
        CorrectAlertView()
    }
}
