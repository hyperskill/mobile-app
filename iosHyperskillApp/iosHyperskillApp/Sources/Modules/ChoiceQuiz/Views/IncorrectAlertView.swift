import SwiftUI

struct IncorrectAlertView: View {
    var body: some View {
        HStack {
            Image("info_icon")
            Text("Not correct, but keep on trying \nand never give up!")
                .foregroundColor(Color(ColorPalette.primary))
                .font(.body)
            Spacer()
        }
        .padding(.vertical)
    }
}

struct IncorrectAlertView_Previews: PreviewProvider {
    static var previews: some View {
        IncorrectAlertView()
    }
}
