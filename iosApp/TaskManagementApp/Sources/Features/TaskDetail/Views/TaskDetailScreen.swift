//
//  TaskDetailScreen.swift
//  TaskManagement
//
//  Created by Nguyen Minh Khang on 16/4/26.
//

import SwiftUI

struct GlassMaterialModifier: ViewModifier {
    func body(content: Content) -> some View {
        content
            .padding(16)
            .background(.ultraThinMaterial)
            .clipShape(RoundedRectangle(cornerRadius: 16, style: .continuous))
            .shadow(color: Color.black.opacity(0.05), radius: 10, x: 0, y: 5)
    }
}

extension View {
    func glassMaterial() -> some View {
        self.modifier(GlassMaterialModifier())
    }
}

struct TaskDetailScreen : View {
    let id : Int
    @Environment(\.dismiss) var dismiss
    
    // Mock State for UI Demonstration
    @State private var taskTitle: String = "Complete SwiftUI Migration"
    @State private var taskDetail: String = "Ensure all views utilize native Liquid Glass aesthetics to match Apple's modern design language."
    @State private var currentCollection: String = "Personal"
    @State private var isFavorite: Bool = false
    @State private var isEditMode: Bool = false
    
    @State private var selectedDate: Date? = Date()
    @State private var selectedTime: Date? = Date()
    
    @State private var showCollectionSheet = false
    @State private var repeatSummaryText: String = "Does not repeat"
    
    @State private var path: [Route] = []
    
    var body: some View {
        ZStack {
            
            ScrollView {
                VStack(spacing: 16) {
                    // Collection Row
                    Button(action: { showCollectionSheet = true }) {
                        HStack {
                            Image(systemName: "folder.fill")
                                .foregroundColor(.blue)
                                .font(.title3)
                            Text(currentCollection)
                                .foregroundColor(.primary)
                                .font(.body.weight(.medium))
                            Spacer()
                            Image(systemName: "chevron.up.chevron.down")
                                .foregroundColor(.secondary)
                                .font(.caption.weight(.bold))
                        }
                    }
                    .glassMaterial()
                    
                    // Task Title
                    HStack {
                        if isEditMode {
                            TextField("Task Title", text: $taskTitle)
                                .font(.title2.weight(.bold))
                                .foregroundColor(.primary)
                            
                            Button(action: { isEditMode.toggle() }) {
                                Image(systemName: "checkmark.circle.fill")
                                    .foregroundColor(.green)
                                    .font(.title2)
                            }
                        } else {
                            Text(taskTitle.isEmpty ? "New Task" : taskTitle)
                                .font(.title2.weight(.bold))
                                .foregroundColor(.primary)
                                .frame(maxWidth: .infinity, alignment: .leading)
                            
                            Button(action: { isEditMode.toggle() }) {
                                Image(systemName: "pencil.circle.fill")
                                    .foregroundColor(.blue)
                                    .font(.title2)
                            }
                        }
                    }
                    .glassMaterial()
                    
                    // Detail Input
                    HStack(alignment: .top) {
                        Image(systemName: "text.alignleft")
                            .foregroundColor(.secondary)
                            .padding(.top, 8)
                        
                        TextField("Add details...", text: $taskDetail, axis: .vertical)
                            .lineLimit(4...8)
                            .foregroundColor(.primary)
                    }
                    .glassMaterial()
                    
                    // Date Row
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
                    
                    // Time Row
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
                    
                    // Repeat Row
                    NavigationLink(value: Route.repeatScreen(id: id)) {
                        HStack {
                            Image(systemName: "repeat")
                                .foregroundColor(.purple)
                                .font(.title3)
                            Text(repeatSummaryText)
                                .foregroundColor(.primary)
                                .font(.body)
                            Spacer()
                            Image(systemName: "chevron.right")
                                .foregroundColor(.secondary)
                                .font(.subheadline.weight(.semibold))
                        }
                    }
                    .glassMaterial()
                    
                    // Add To Calendar Button
                    Button(action: {
                        print("Add to calendar")
                    }) {
                        HStack(spacing: 8) {
                            Image(systemName: "calendar.badge.plus")
                            Text("Add to Calendar")
                                .fontWeight(.semibold)
                        }
                        .foregroundColor(.blue)
                        .frame(maxWidth: .infinity)
                    }
                    .glassMaterial()
                }
                .padding()
                .padding(.bottom, 100) // Padding for bottom FAB-replacement
            }
        }
        .navigationTitle("Detail")
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: {
                    dismiss()
                }) {
                    Image(systemName: "chevron.left")
                        .fontWeight(.semibold)
                }
            }
            
            ToolbarItem(placement: .navigationBarTrailing) {
                Button(action: {
                    isFavorite.toggle()
                }) {
                    Image(systemName: isFavorite ? "heart.fill" : "heart")
                        .foregroundColor(isFavorite ? .red : .primary)
                }
            }
        }
        .sheet(isPresented: $showCollectionSheet) {
            // Mock Collection Change Sheet
            NavigationView {
                List {
                    Button("Personal") {
                        currentCollection = "Personal"
                        showCollectionSheet = false
                    }
                    Button("Work") {
                        currentCollection = "Work"
                        showCollectionSheet = false
                    }
                    Button("Inbox") {
                        currentCollection = "Inbox"
                        showCollectionSheet = false
                    }
                }
                .navigationTitle("Change Collection")
                .navigationBarTitleDisplayMode(.inline)
                .toolbar {
                    ToolbarItem(placement: .navigationBarTrailing) {
                        Button("Done") {
                            showCollectionSheet = false
                        }
                    }
                }
            }
            .presentationDetents([.medium, .large])
        }
        // Floating / Fixed Action Button mapped from Android BottomEnd FAB
        .safeAreaInset(edge: .bottom) {
            Button(action: {
                print("Mark as done pressed")
            }) {
                Text("Mark as Done")
                    .font(.headline.weight(.bold))
                    .foregroundColor(.white)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(
                        RoundedRectangle(cornerRadius: 16, style: .continuous)
                            .fill(Color.blue)
                            // Shadow to elevate it above scroll content
                            .shadow(color: Color.blue.opacity(0.3), radius: 10, x: 0, y: 5)
                    )
            }
            .padding(.horizontal)
            .padding(.bottom, 8)
            // Liquid Glass backing behind the button to ensure readability if scrolling beneath it
            .background(.ultraThinMaterial)
        }
        .toolbar(.hidden, for: .tabBar)
    }
}
//
//#Preview {
//    NavigationView {
//        TaskDetailScreen()
//    }
//}
