//
//  ThemeScreen.swift
//  TaskManagement
//
//  Created by Nguyen Minh Khang on 14/4/26.
//

import SwiftUI

struct ThemeScreen: View {
    @Environment(\.dismiss) var dismiss
    
    // UI State equivalents
    @State private var selectedThemeMode: String = "light"
    @State private var selectedColorTheme: String = "purple"
    
    let themeModeOptions = [
        ("light", "Light"),
        ("dark", "Dark"),
        ("system", "System Default")
    ]
    
    let colorThemeOptions: [(String, String, Color)] = [
        ("purple", "Purple", .purple),
        ("red", "Red", .red),
        ("green", "Green", .green),
        ("blue", "Blue", .blue),
        ("orange", "Orange", .orange)
    ]
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            
            ScrollView {
                VStack(alignment: .leading, spacing: 24) {
                    
                    // Appearance Section
                    VStack(alignment: .leading, spacing: 16) {
                        Text("Appearance")
                            .font(.headline)
                        
                        VStack(spacing: 4) {
                            ForEach(themeModeOptions, id: \.0) { option in
                                Button(action: {
                                    selectedThemeMode = option.0
                                }) {
                                    HStack {
                                        Text(option.1)
                                            .font(.body)
                                            .foregroundColor(.primary)
                                            .padding(.leading, 4)
                                        
                                        Spacer()
                                        
                                        Image(systemName: selectedThemeMode == option.0 ? "largecircle.fill.circle" : "circle")
                                            .foregroundColor(selectedThemeMode == option.0 ? .blue : .gray)
                                            .font(.system(size: 20))
                                    }
                                    .padding(.vertical, 8)
                                }
                            }
                        }
                    }
                    .padding(.horizontal, 16)
                    
                    // Color Theme Section
                    VStack(alignment: .leading, spacing: 16) {
                        Text("Color Theme")
                            .font(.headline)
                        
                        VStack(spacing: 12) {
                            ForEach(colorThemeOptions, id: \.0) { option in
                                Button(action: {
                                    selectedColorTheme = option.0
                                }) {
                                    HStack(spacing: 12) {
                                        Circle()
                                            .fill(option.2)
                                            .frame(width: 24, height: 24)
                                        
                                        Text(option.1)
                                            .font(.body)
                                            .foregroundColor(.primary)
                                        
                                        Spacer()
                                        
                                        Image(systemName: selectedColorTheme == option.0 ? "largecircle.fill.circle" : "circle")
                                            .foregroundColor(selectedColorTheme == option.0 ? .blue : .gray)
                                            .font(.system(size: 20))
                                    }
                                    .padding(.vertical, 4)
                                }
                            }
                        }
                    }
                    .padding(.horizontal, 16)
                }
                .padding(.top, 16)
            }
        }
        .navigationTitle(Text("Theme")
            .font(.title2))
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .topBarLeading) {
                Button(action: {
                    dismiss()
                }) {
                    Image(systemName: "chevron.left")
                        .fontWeight(.semibold)
                }
            }
        }
        .toolbar(.hidden, for: .tabBar)
    }
}

#Preview {
    ThemeScreen()
}
