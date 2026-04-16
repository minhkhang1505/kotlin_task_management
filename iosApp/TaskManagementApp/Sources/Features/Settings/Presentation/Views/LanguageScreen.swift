//
//  LanguageScreen.swift
//  TaskManagement
//
//  Created by Nguyen Minh Khang on 14/4/26.
//

import SwiftUI

struct LanguageScreen: View {
    @Environment(\.dismiss) var dismiss
    
    // UI State equivalent
    @State private var selectedLanguageCode: String = "en"
    
    let languageOptions = [
        ("en", "English"),
        ("vi", "Vietnamese")
    ]
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            
            // Options
            VStack(spacing: 0) {
                ForEach(languageOptions, id: \.0) { option in
                    Button(action: {
                        selectedLanguageCode = option.0
                    }) {
                        HStack {
                            Text(option.1)
                                .font(.body)
                                .foregroundColor(.primary)
                                .padding(.leading, 4)
                            
                            Spacer()
                            
                            Image(systemName: selectedLanguageCode == option.0 ? "largecircle.fill.circle" : "circle")
                                .foregroundColor(selectedLanguageCode == option.0 ? .blue : .gray)
                                .font(.system(size: 20))
                        }
                        .padding(.vertical, 8)
                        .padding(.horizontal, 16)
                    }
                }
            }
            
            Spacer()
        }
        .navigationTitle(Text("Language")
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
    }
}

#Preview {
    LanguageScreen()
}
