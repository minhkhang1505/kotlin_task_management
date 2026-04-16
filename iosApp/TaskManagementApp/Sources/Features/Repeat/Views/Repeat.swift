//
//  Repeat.swift
//  TaskManagement
//
//  Created by Nguyen Minh Khang on 16/4/26.
//

import SwiftUI

struct RepeatScreen : View {
    @Environment(\.dismiss) var dismiss
    
    @State private var repeatEvery = ""
    
    @State private var haveChanged = false
    
    @State private var selectedDate: Date? =  Date()
    @State private var selectedTime: Date? =  Date()
    
    @State private var selected = "Month"
    let options = ["Month", "Day", "Year"]
    
    var body: some View {
        ScrollView {
            VStack (spacing: 16) {
                HStack {
                    TextField("1", text: $repeatEvery)
                        .padding(12)
                        .background(.ultraThinMaterial)
                        .clipShape(RoundedRectangle(cornerRadius: 12))
                    
                    Picker("Select day", selection: $selected) {
                        ForEach(options, id: \.self) { item in
                            Text(item)
                        }
                    }
                    
                }
                HStack(spacing: 12) {
                    ForEach(["Mon", "Tue", "Wed", "Thu", "Fri", "Sat"], id: \.self) { day in
                        Button(action: {
                            print(day)
                        }) {
                            Text(day)
                                .font(.subheadline)
                                .foregroundColor(.white)
                                .frame(width: 50, height: 50)
                                .background(Color.blue)
                                .clipShape(Circle())
                        }
                    }
                }
                HStack {
                    Image(systemName: "calendar")
                        .foregroundColor(.red)
                        .font(.title3)
                    
                    DatePicker(
                        "Date",
                        selection: Binding(
                            get: { selectedDate ?? Date() },
                            set: { selectedDate = $0 }
                        ),
                        displayedComponents: .date
                    )
                    .datePickerStyle(.compact)
                    .labelsHidden()
                    
                    Spacer()
                    
                    if selectedDate != nil {
                        Button(action: { selectedDate = nil }) {
                            Image(systemName: "xmark.circle.fill")
                                .foregroundColor(.secondary)
                        }
                    } else {
                        Text("No Date")
                            .foregroundColor(.secondary)
                            .font(.subheadline)
                    }
                }
                .glassMaterial()
                
                HStack {
                    Image(systemName: "clock.fill")
                        .foregroundColor(.orange)
                        .font(.title3)
                    
                    DatePicker(
                        "Time",
                        selection: Binding(
                            get: { selectedTime ?? Date() },
                            set: { selectedTime = $0 }
                        ),
                        displayedComponents: .hourAndMinute
                    )
                    .datePickerStyle(.compact)
                    .labelsHidden()
                    
                    Spacer()
                    
                    if selectedTime != nil {
                        Button(action: { selectedTime = nil }) {
                            Image(systemName: "xmark.circle.fill")
                                .foregroundColor(.secondary)
                        }
                    } else {
                        Text("No Time")
                            .foregroundColor(.secondary)
                            .font(.subheadline)
                    }
                }
                .glassMaterial()
            }
            
        }
        .navigationTitle(Text("Repeat")
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
            ToolbarItem(placement: .topBarTrailing) {
                Button {
                    // save
                } label: {
                    Image(systemName: "checkmark")
                }
                .disabled(!haveChanged)
            }
        }
        .padding(.horizontal, 16)
    }
}

#Preview {
    NavigationView {
        RepeatScreen()
    }
    
}
