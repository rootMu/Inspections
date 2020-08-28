package com.matthew.inspections.network

import com.matthew.inspections.network.model.InspectionResponse
import com.matthew.inspections.user.Authorisation
import retrofit2.http.*


interface InspectionsService {

    companion object {
        const val AUTHORISATION_ROUTE = "/authorise"
        const val INSPECTION_ROUTE = "/inspections"
        const val USER_ROUTE = "/user"
    }

    /**
     * Authenticate the user for login
     */
    @Multipart
    @POST(AUTHORISATION_ROUTE)
    suspend fun authorise(
        @Part("username") username: String,
        @Part("password") password: String
    ): Authorisation

    /**
     * Get the complete list of Inspections
     * this is to allow for offline work. Access
     * to individual records in app will be controlled
     * via login
     */
    @POST(INSPECTION_ROUTE)
    suspend fun getInspections(
        @Body authorisationToken: String,
        @Query("searchByArea") searchByArea: Boolean? = null,
        @Query("areaId") areaId: Int = 0,
        @Query("searchByType") searchByType: Boolean? = null,
        @Query("typeId") typeId: Int = 0
    ): InspectionResponse

    /**
     * Get the inspections related only to
     * selected user
     */
    @GET(INSPECTION_ROUTE + USER_ROUTE)
    suspend fun getInspectionsByUser(
        @Query("authorisationToken") authorisationToken: String,
        @Query("userId") userId: Int
    ): InspectionResponse
}