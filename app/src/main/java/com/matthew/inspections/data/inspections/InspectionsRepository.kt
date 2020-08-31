package com.matthew.inspections.data.inspections

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.matthew.inspections.data.login.AuthenticationRepository
import com.matthew.inspections.network.InspectionsService
import com.matthew.inspections.room.InspectionsDao
import com.matthew.inspections.room.data.*
import com.matthew.inspections.ui.inspections.*
import com.matthew.inspections.util.Resource
import kotlinx.coroutines.Dispatchers
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend

@Singleton
class InspectionsRepository @Inject constructor(
    private val service: InspectionsService,
    private val dao: InspectionsDao
) {

    fun getAllInspections(
        status: InspectionListStatus?,
        search: String?,
        searchType: InspectionsSearch?
    ): LiveData<Resource<List<InspectionWithQuestions>>> =
        liveData(Dispatchers.IO) {
            emit(Resource.loading())
            val source: LiveData<Resource<List<InspectionWithQuestions>>> = try {
                val s = status?.name ?: InspectionListStatus.Past.name
                getInspections(s, search, searchType)
            } catch (e: Exception) {
                /**
                 * Retrieve inspections from server if wanting to update
                 * else emit an empty list
                 */
                liveData<List<InspectionWithQuestions>> {
                    emit(emptyList())
                }
            }.map { Resource.success(it) }
            emitSource(source)
        }

    private fun getInspections(
        status: String,
        search: String?,
        searchType: InspectionsSearch?
    ): LiveData<List<InspectionWithQuestions>> = liveData {

        val safeSearchType = searchType ?: InspectionsSearch.ById
        val searchBy = search != null

        val source: LiveData<List<InspectionWithQuestions>> = try {

            val functionName = "get$status" + "Inspections" +
                    if (searchBy && (safeSearchType == InspectionsSearch.ByName || safeSearchType == InspectionsSearch.ById)) {
                        safeSearchType.name
                    } else ""

            val func =
                this@InspectionsRepository::class.members.first { it.name == functionName } as KFunction<LiveData<List<InspectionWithQuestions>>>

            when {
                searchBy && safeSearchType.searchByName() -> func.callSuspend(
                    this@InspectionsRepository,
                    search!!
                )
                searchBy && safeSearchType.searchById() -> func.callSuspend(
                    this@InspectionsRepository,
                    search!!.toInt()
                )
                else -> func.callSuspend(this@InspectionsRepository)
            }

        } catch (e: Exception) {
            liveData {
                emit(emptyList())
            }
        }
        emitSource(source)
    }

    /**
     * instead of calling the database we're just displaying fake generated data
     */

    fun getPastInspections() = liveData {
        emit(generateInspections())
    }//dao.getPastInspections()

    fun getPastInspectionsByName(name: String) = liveData {
        emit(generateInspections())
    }//dao.getPastInspectionsByName(name = name)

    fun getPastInspectionsById(inspectionId: Int) = liveData {
        emit(generateInspections())
    }//dao.getPastInspectionsById(inspectionId = inspectionId)

    fun getCurrentInspections() = liveData {
        emit(generateInspections())
    }//dao.getCurrentInspections()

    fun getCurrentInspectionsByName(name: String) = liveData {
        emit(generateInspections())
    }//dao.getCurrentInspectionsByName(name = name)

    fun getCurrentInspectionsById(inspectionId: Int) = liveData {
        emit(generateInspections())
    }//dao.getCurrentInspectionsById(inspectionId = inspectionId)

    fun getFutureInspections() = liveData {
        emit(generateInspections())
    }//dao.getFutureInspections()

    fun getFutureInspectionsByName(name: String) = liveData {
        emit(generateInspections())
    }//dao.getFutureInspectionsByName(name = name)

    fun getFutureInspectionsById(inspectionId: Int) = liveData {
        emit(generateInspections())
    }//dao.getFutureInspectionsById(inspectionId = inspectionId)

    fun getAreaNameById(areaId: Int) = try {
        dao.getAreaNameById(areaId) ?: ""
    } catch (e: Exception) {
        //Log exception
        randomString()
    }

    fun getTypeNameById(typeId: Int) = try {
        dao.getTypeNameById(typeId) ?: ""
    } catch (e: Exception) {
        //Log exception
        randomString()
    }

    /**
     * function to generate a random number of inspections with a random questions
     */
    private fun generateInspections() =
        mutableListOf<InspectionWithQuestions>().apply {
            val numberOfInspections = Random.nextInt().coerceAtLeast(1).coerceAtMost(12)
            repeat(numberOfInspections) { inspectionId ->
                val numberOfQuestions = Random.nextInt().coerceAtLeast(1).coerceAtMost(20)
                val questions = mutableListOf<Question>()
                repeat(numberOfQuestions) { questionId ->

                    val answers = mutableListOf<Answer>()
                    repeat(Random.nextInt().coerceAtMost(15).coerceAtLeast(2)) {
                        val answer = Answer(
                            answerId = Random.nextInt().coerceAtLeast(2).coerceAtMost(10),
                            questionId = questionId,
                            displayText = randomString()
                        )
                        answers.add(answer)
                    }

                    questions.add(
                            Question(
                                0,
                                (questionId + numberOfQuestions) * numberOfInspections,
                                inspectionId,
                                randomString()
                            )
                    )
                }
                val inspection = Inspection(
                    0,
                    inspectionId,
                    randomString(),
                    InspectionStatus.getByValue(
                        Random.nextInt().coerceAtLeast(0).coerceAtMost(5)
                    ),
                    LocalDateTime.now()
                        .minusDays(Random.nextLong().coerceAtLeast(0).coerceAtMost(100))
                )
                val inspectionWithQuestions = InspectionWithQuestions(
                    inspection,
                    questions,
                    Area(
                        0,
                        Random.nextInt().coerceAtLeast(0),
                        randomString()
                    ),
                    Type(
                        0,
                        Random.nextInt().coerceAtLeast(0), randomString()
                    )
                )
                add(inspectionWithQuestions)
            }
        }


    /**
     * companion objects to generate random string used with the fake inspection generation
     */
    companion object {

        private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        fun randomString(): String = (1..AuthenticationRepository.STRING_LENGTH)
            .map { Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")

    }
}
