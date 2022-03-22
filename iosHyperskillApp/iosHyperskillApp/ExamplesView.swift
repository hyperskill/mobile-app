//
//  ExamplesView.swift
//  iosHyperskillApp
//
//  Created by Владислав Кащей on 22.03.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct ExamplesView: View {
    @State var isAlerting: Bool = false
    @State var isProgressing: Bool = false
    var body: some View {
        if isProgressing {
                    ZStack{
                        RoundedRectangle(cornerRadius: 15).fill(.black.opacity(0.1))
                        ProgressView()
                    }.frame(width: 100, height: 100, alignment: .center)
                    
                }
        VStack{
            Spacer()
            VStack {
                Button("Alert", action: {
                    isProgressing = true
                    let seconds = 4.0
                    DispatchQueue.main.asyncAfter(deadline: .now() + seconds) {
                        isProgressing = false
                        isAlerting = true
                    }
                }).alert(isPresented: $isAlerting) {
                    Alert(
                        title: Text("Title"),
                        message: Text("Message")
                    )
                }
            }
            Spacer()
        }
    }
}

struct ExamplesView_Previews: PreviewProvider {
    static var previews: some View {
        ExamplesView()
    }
}
