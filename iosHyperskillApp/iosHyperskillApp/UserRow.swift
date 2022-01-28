//
//  UserRow.swift
//  iosHyperskillApp
//
//  Created by Ruslan Davletshin on 07.01.2022.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct UserRow: View {
    var user: User
    
    var body: some View {
        HStack(alignment: .center) {
            Text(user.login)
            Spacer()
        }.padding(16)
    }
}
