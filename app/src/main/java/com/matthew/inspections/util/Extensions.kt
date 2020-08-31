package com.matthew.inspections.util

inline fun Any?.ifNull(unit: () -> Unit){
    this?.let{}?:run{ unit.invoke() }
}

