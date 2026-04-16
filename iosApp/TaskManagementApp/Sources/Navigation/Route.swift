//
//  Route.swift
//  TaskManagement
//
//  Created by Nguyen Minh Khang on 17/4/26.
//

enum Route : Hashable {
    case home
    case search
    case settings
    case taskDetail(id: Int)
    case theme
    case language
    case font
    case repeatScreen(id: Int)
}
