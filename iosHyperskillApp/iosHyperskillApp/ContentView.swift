import SwiftUI
import shared

struct ContentView: View {
    let usersFeature = UsersListFeatureBuilder.init()
        .build()
    
    @ObservedObject var stateStore: StateStore
    @State private var query: String = "eadm"
    
    var body: some View {
        let state = self.stateStore.state
        
        VStack(alignment: .center) {
            
            HStack(alignment: .top) {
                TextField("Query", text: $query)
                
                Button(action: {
                    self.searchButtonPressed()
                }, label: {
                    Text("Search")
                })
                
            }.padding(16)
            
            switch state {
            case is UsersListFeatureStateIdle:
                Text("Empty")
            case is UsersListFeatureStateLoading:
                ProgressView()
            case let data as UsersListFeatureStateData:
                ScrollView {
                    LazyVStack {
                        ForEach(data.users, id: \.id) { user in
                            UserRow(user: user)
                                .onAppear {
                                    self.loadMoreIfNeeded(user: user)
                                }
                        }
                        
                        if data.isLoading {
                            ProgressView()
                        }
                    }
                }
            case is UsersListFeatureStateNetworkError:
                Text("Network error")
            default:
                Text("Unkwown state")
            }
            
            VStack(alignment: .center) {
                
            }
            
            Spacer()
        }
    }
    
    init() {
        stateStore = StateStore(feature: usersFeature)
    }
    
    func searchButtonPressed() -> Void {
        usersFeature.onNewMessage(message_: UsersListFeatureMessageInit.init(
            forceUpdate: true,
            usersQuery: UsersQuery(page: 0, pageSize: 20, userName: query)
        ))
    }
    
    func loadMoreIfNeeded(user: User) {
        guard let data = self.stateStore.state as? UsersListFeatureStateData else {
            return
        }
        
        if data.users.last == user {
            usersFeature.onNewMessage(
                message_: UsersListFeatureMessageLoadNextPage.init()
            )
        }
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
            self.state = newState as! UsersListFeatureState
        })
    }
    
    @Published var state: UsersListFeatureState =
        UsersListFeatureStateIdle.init()
}
