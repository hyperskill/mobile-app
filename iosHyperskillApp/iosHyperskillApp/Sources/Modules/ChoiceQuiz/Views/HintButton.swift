import SwiftUI

struct HintButton: View {
    var body: some View {
        Button(action: {}, label: {
            HStack {
                Image("lightning_icon")
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                        .frame(width: 16, height: 16)
                    .padding(.vertical, 9)
                    .padding(.leading, 12)
                Text("See hint")
                    .padding(.vertical, 8)
                    .padding(.trailing, 12)
            }
        })
        .buttonStyle(OutlineButtonStyle(font: .subheadline))
        .frame(minWidth: 109)
    }
}

struct HintButton_Previews: PreviewProvider {
    static var previews: some View {
        HintButton()
    }
}
