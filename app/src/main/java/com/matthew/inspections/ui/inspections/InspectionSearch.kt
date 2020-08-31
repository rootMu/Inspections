package com.matthew.inspections.ui.inspections

import androidx.annotation.MainThread

enum class InspectionsSearch(val value: Int) {
    ByName(0),
    ById(1),
    ByArea(2),
    ByType(3)
}

@MainThread
fun InspectionsSearch.searchByName() = this == InspectionsSearch.ByName

@MainThread
fun InspectionsSearch.searchById() = this == InspectionsSearch.ById

@MainThread
fun InspectionsSearch.searchByArea() = this == InspectionsSearch.ByArea

@MainThread
fun InspectionsSearch.searchByType() = this == InspectionsSearch.ByType