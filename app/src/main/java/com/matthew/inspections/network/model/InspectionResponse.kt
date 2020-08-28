package com.matthew.inspections.network.model

/**
 * Initial response retrieved by server
 */
data class InspectionResponse(
    val received: String,
    val inspections: List<InspectionData>?,
    val errorResponse: String?
)

/**
 * Individual Inspection Record
 * contains list of Question Ids and
 * group Ids and an association
 */
data class InspectionData(
    val id: Int,
    val name: String,
    val status: Int,
    val questions: List<Int>?,
    val area: Int?,
    val associatedWith: Int,
    val accessibleBy: List<Int>?
)

/**
 * Individual Question Record
 * contains list of Answer Ids
 */
data class QuestionData(
    val id: Int,
    val displayText: String,
    val answers: List<Int>?
)

/**
 * Group Record
 */
data class GroupData(
    val id: Int,
    val name: String
)

/**
 * Individual Answer Record
 */
data class AnswerData(
    val id: Int,
    val displayText: String,
    val value: Int,
    val applicable: Boolean
)