//
//  FontStyleScreen.swift
//  TaskManagement
//
//  Created by Nguyen Minh Khang on 14/4/26.
//

import SwiftUI

struct FontStyleScreen: View {
    @Environment(\.dismiss) var dismiss
    
    // UI State equivalent
    @State private var selectedFontStyle: String = "default"
    
    let fontStyleOptions: [(String, String, Font.Design?)] = [
        ("default", "System Default", .default),
        ("serif", "Serif", .serif),
        ("sans_serif", "Sans Serif", .default), // SwiftUI nativo default uses sans-serif
        ("monospace", "Monospace", .monospaced),
        ("cursive", "Cursive", .serif) // SwiftUI native fallback for cursive
    ]
    
    var body: some View {
        VStack(alignment: .leading, spacing: 0) {
            
            // Options
            VStack(spacing: 0) {
                ForEach(fontStyleOptions, id: \.0) { option in
                    Button(action: {
                        selectedFontStyle = option.0
                    }) {
                        HStack {
                            Text(option.1)
                                .font(.system(.body, design: option.2 ?? .default))
                                .foregroundColor(.primary)
                                .padding(.leading, 4)
                            
                            Spacer()
                            
                            Image(systemName: selectedFontStyle == option.0 ? "largecircle.fill.circle" : "circle")
                                .foregroundColor(selectedFontStyle == option.0 ? .blue : .gray)
                                .font(.system(size: 20))
                        }
                        .padding(.vertical, 8)
                        .padding(.horizontal, 16)
                    }
                }
            }
            
            Spacer()
        }
        .navigationTitle(Text("Font Style")
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
    FontStyleScreen()
}
