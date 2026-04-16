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
                
                // Task list area
                ScrollView {
                    VStack(spacing: 12) {
                        NavigationLink(value: Route.taskDetail(id: 1)) {
                            HStack {
                                Image(systemName: "circle")
                                    .foregroundColor(.gray)
                                    .font(.title3)
                                
                                VStack(alignment: .leading, spacing: 4) {
                                    Text("Mock Task")
                                        .font(.headline)
                                        .foregroundColor(.primary)
                                    Text("Tap me to open Task Detail")
                                        .font(.subheadline)
                                        .foregroundColor(.secondary)
                                }
                                
                                Spacer()
                                
                                Image(systemName: "chevron.right")
                                    .foregroundColor(.gray)
                            }
                            .padding()
                            .background(Color(.secondarySystemGroupedBackground))
                            .cornerRadius(12)
                            // Thêm shadow nhẹ giống phong cách thẻ 
                            .shadow(color: Color.black.opacity(0.05), radius: 5, x: 0, y: 2)
                            .padding(.horizontal, 16)
                        }
                    }
                    .padding(.top, 16)
                }
                
                Spacer()
            }
        }
    }
}

#Preview {
    HomeScreen()
}
