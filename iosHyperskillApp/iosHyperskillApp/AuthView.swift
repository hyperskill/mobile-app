//
//  AuthView.swift
//  iosHyperskillApp
//
//  Created by Владислав Кащей on 19.03.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import GoogleSignIn

struct AuthView: View {    
    var body: some View {
        VStack {
            Divider()
            Image("logo")
                .resizable()
                .border(.black, width: 1)
                .frame(width: 40, height: 40, alignment: .center)
            
            VStack(spacing: 40) {
                Divider()
                Divider()
                Divider()
                Text("Log in to Hyperskill")
                    .font(.title).bold()
                Divider()
                Button(action: {
                    
                }, label: {
                    Text("Google").padding()
                        .overlay(
                            RoundedRectangle(cornerRadius: 5)
                                            .stroke(.purple, lineWidth: 2)
                                            .frame(width: 120, height: 40, alignment: .center)
                        ).foregroundColor(.purple)
                })
            }
            
            
            Spacer()
            Text("Log in with Email").bold()
        }
    }
}

struct AuthView_Previews: PreviewProvider {
    static var previews: some View {
        AuthView()
    }
}
