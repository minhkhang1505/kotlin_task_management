//
//  HomeScreen.swift
//  TaskManagement
//
//  Created by Nguyen Minh Khang on 14/4/26.
//

import SwiftUI

struct HomeScreen: View {
    // UI State Mocks
    @State private var selectedTabIndex: Int = 0
    let tabs = ["Today", "Upcoming", "Task Done", "+ Add New"]
    
    var body: some View {
        ZStack {
            Color(.systemBackground)
                .ignoresSafeArea()
            
            VStack(alignment: .leading, spacing: 0) {
                // TopBar Mock
                HStack {
                    Text("Task Management")
                        .font(.title2)
                        .fontWeight(.bold)
                    
                    Spacer()
                    
                    Image(systemName: "person.circle.fill")
                        .font(.system(size: 32))
                        .foregroundColor(.gray)
                }
                .padding(.horizontal, 16)
                .padding(.top, 16)
                .padding(.bottom, 24)
                
                // Segmented Control
                Picker("Tabs", selection: $selectedTabIndex) {
                    ForEach(0..<tabs.count, id: \.self) { index in
                        Text(tabs[index]).tag(index)
                    }
                }
                .pickerStyle(.segmented)
                .padding(.horizontal, 16)
                .padding(.bottom, 8)
                
                // Task list area (Left blank as requested)
                Spacer()
            }
        }
    }
}

#Preview {
    HomeScreen()
}
