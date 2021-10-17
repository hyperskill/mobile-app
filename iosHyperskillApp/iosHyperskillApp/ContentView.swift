import SwiftUI
import shared

struct ContentView: View {
    let sampleFeature = SampleFeatureBuilder.init()
        .build()
    
    @ObservedObject var stateStore: StateStore
    
    var body: some View {
        VStack(alignment: .leading) {
            let counter = (self.stateStore.state as? SampleFeatureStateData)?.counter ?? 0
            Text("\(counter)")
            Button(action: {
                self.buttonPressed()
            }, label: {
                Text("+")
            })
        }
    }
    
    init() {
        stateStore = StateStore(feature: sampleFeature)
    }
    
    func buttonPressed() -> Void {
        sampleFeature.onNewMessage(message: SampleFeatureMessageIncCounterClicked.init())
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

class StateStore: ObservableObject {
    init(feature: Presentation_reduxFeature) {
        feature.addStateListener(listener: { newState in
            self.state = newState as! SampleFeatureState
        })
    }
    
    @Published var state: SampleFeatureState =
        SampleFeatureStateData(counter: 0)
}
