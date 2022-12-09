import SwiftUI

struct GemsBarButtonItem: View {
    let hypercoinsCount: Int

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

                    Text("\(hypercoinsCount)")
                        .foregroundColor(.primaryText)
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
                        GemsBarButtonItem(hypercoinsCount: 3, onTap: {})
                    }
                }
        }
    }
}
