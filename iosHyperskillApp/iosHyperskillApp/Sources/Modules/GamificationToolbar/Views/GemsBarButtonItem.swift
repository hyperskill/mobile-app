import SwiftUI

struct GemsBarButtonItem: View {
    let hypercoinsBalance: Int

    let onTap: () -> Void

    var body: some View {
        Button(
            action: onTap,
            label: {
                HStack {
                    Image(Images.NavigationBar.gems)
                        .renderingMode(.original)
                        .resizable()
                        .aspectRatio(contentMode: .fit)

                    if #available(iOS 17.0, *) {
                        Text("\(hypercoinsBalance)")
                            .foregroundColor(.primaryText)
                            .animation(.default, value: hypercoinsBalance)
                            .contentTransition(.numericText(value: Double(hypercoinsBalance)))
                    } else {
                        Text("\(hypercoinsBalance)")
                            .foregroundColor(.primaryText)
                            .animation(.default, value: hypercoinsBalance)
                    }
                }
            }
        )
    }
}

struct GemsBarButtonItem_Previews: PreviewProvider {
    static var previews: some View {
        NavigationView {
            Text("Hello, World!")
                .navigationTitle("Navigation")
                .toolbar {
                    ToolbarItem(placement: .primaryAction) {
                        GemsBarButtonItem(hypercoinsBalance: 3, onTap: {})
                    }
                }
        }
    }
}
