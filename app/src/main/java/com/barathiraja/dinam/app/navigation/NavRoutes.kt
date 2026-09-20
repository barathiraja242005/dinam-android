package com.barathiraja.dinam.app.navigation

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Today : NavRoutes("today")
    object NewList : NavRoutes("new_list")
    object ListDetail : NavRoutes("list/{listId}") {
        fun createRoute(listId: String) = "list/$listId"
    }
    object ItemDetail : NavRoutes("item_detail/{itemId}") {
        fun createRoute(itemId: String) = "item_detail/$itemId"
    }
    object ListItemDetail : NavRoutes("list_item_detail/{itemId}") {
        fun createRoute(itemId: String) = "list_item_detail/$itemId"
    }
    object Reschedule : NavRoutes("reschedule/{itemId}") {
        fun createRoute(itemId: String) = "reschedule/$itemId"
    }
}