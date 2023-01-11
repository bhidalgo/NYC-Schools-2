package com.brandon.hidalgo.nycschools.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brandon.hidalgo.nycschools.dal.SchoolRepository
import com.brandon.hidalgo.nycschools.model.SchoolModel
import com.brandon.hidalgo.nycschools.model.ScoresModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainViewModel() : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext =
        viewModelScope.coroutineContext + Dispatchers.Default

    private var filterJob: Job? = null

    // Ensure that MainViewModel is the SSOT for the following Live Data
    var searchCriteria by mutableStateOf("")
        private set

    val onSearchCriteriaChanged: (String) -> Unit = {
        searchCriteria = it

        filterJob?.cancel()
        filterJob = launch(coroutineContext) {
            filterSchools()
        }
    }

    private val _allSchoolsAndScores = MutableLiveData(emptyMap<SchoolModel, ScoresModel?>())

    private val _schoolsAndScores = MutableLiveData(emptyMap<SchoolModel, ScoresModel?>())
    val schoolsAndScores: LiveData<Map<SchoolModel, ScoresModel?>> = _schoolsAndScores

    private val _loading = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private var _schools = emptyList<SchoolModel>()

    private var _scores = emptyList<ScoresModel>()

    // Marked for internal usage of Compose Previews
    internal constructor(
        schools: List<SchoolModel>, scores: List<ScoresModel>, loading: Boolean
    ) : this() {
        _schools = schools
        _scores = scores
        _loading.value = loading
        _allSchoolsAndScores.value = schoolsAssociatedWithScores()
        _schoolsAndScores.value = _allSchoolsAndScores.value
    }

    fun fetchData(schoolRepository: SchoolRepository): Job {
        return launch(coroutineContext) {
            withContext(Dispatchers.Main) {
                _loading.value = true
            }

            val fetchSchools = async(coroutineContext) {
                // TODO: Gracefully handle any issues with fetching schools
                runCatching {
                    _schools = schoolRepository.getSchools()
                }
            }
            val fetchScores = async(coroutineContext) {
                // TODO: Gracefully handle any issues with fetching scores
                runCatching {
                    _scores = schoolRepository.getScores()
                }
            }
            awaitAll(fetchSchools, fetchScores)

            val schoolsAndScores = schoolsAssociatedWithScores()
            withContext(Dispatchers.Main) {
                _loading.value = false
                _allSchoolsAndScores.value = schoolsAndScores
                filterSchools()
            }
        }
    }

    private fun filterSchools() {
        if (searchCriteria.isBlank()) {
            _schoolsAndScores.postValue(_allSchoolsAndScores.value)
        } else {
            _schoolsAndScores.postValue(_allSchoolsAndScores.value?.filter {
                it.key.schoolName.contains(
                    searchCriteria
                ) || it.key.boro.contains(searchCriteria)
            })
        }
    }

    private fun schoolsAssociatedWithScores(): Map<SchoolModel, ScoresModel?> {
        return _schools.associateWith { school -> _scores.find { score -> school.dbn == score.dbn } }
    }

}