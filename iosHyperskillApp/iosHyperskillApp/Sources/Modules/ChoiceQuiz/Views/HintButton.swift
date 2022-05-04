import SwiftUI

struct HintButton: View {
    var body: some View {
        Button(action: {}, label: {
            HStack {
                Image("lightning_icon")
                    .padding(.vertical, 9)
                    .padding(.leading, 12)
                Text("See hint")
                    .padding(.vertical, 8)
                    .padding(.trailing, 12)
            }
        })
        .buttonStyle(OutlineButtonStyle(font: .subheadline))
        .frame(width: 109, height: 34)
    }
}

struct HintButton_Previews: PreviewProvider {
    static var previews: some View {
        HintButton()
    }
}
