//
//  SearchScreen.swift
//  TaskManagement
//
//  Created by Nguyen Minh Khang on 14/4/26.
//

import SwiftUI

struct SearchScreen: View {
    // Thuộc tính lưu trữ chuỗi cần tìm
    @State private var searchQuery: String = ""
    
    var body: some View {
        NavigationStack {
            // Khối chứa nội dung động (thay đổi tuỳ vào việc người dùng có nhập từ khoá hay không)
            ZStack(alignment: .top) {
                if searchQuery.isEmpty {
                    DefaultContentMock()
                        .transition(.opacity)
                } else {
                    SearchContentMock(searchQuery: searchQuery)
                        .transition(.opacity)
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            // Thay thế toàn bộ search bar mock bằng 1 dòng Native .searchable của Apple!
            // Khi vuốt lên/xuống hoặc focus, hệ điều hành sẽ tự lo layout lấp lánh (liquid glass / frosted glass)
            .searchable(text: $searchQuery, placement: .navigationBarDrawer(displayMode: .always), prompt: "Search task")
            .navigationTitle("Search")
            .animation(.easeInOut(duration: 0.2), value: searchQuery.isEmpty)
        }
    }
}

// MARK: - Mocks corresponding to Compose counterparts

struct SearchContentMock: View {
    var searchQuery: String
    
    var body: some View {
        VStack {
            Text("Search Results for: \(searchQuery)")
                .foregroundColor(.secondary)
            Spacer()
        }
        .padding(.top, 16)
    }
}

struct DefaultContentMock: View {
    var body: some View {
        VStack {
            Text("Today's Tasks / Default Content")
                .foregroundColor(.secondary)
            Spacer()
        }
        .padding(.top, 16)
    }
}

#Preview {
    SearchScreen()
}
