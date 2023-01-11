package com.brandon.hidalgo.nycschools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.brandon.hidalgo.nycschools.dal.ApiClient
import com.brandon.hidalgo.nycschools.dal.SchoolRepository
import com.brandon.hidalgo.nycschools.ui.component.MainScreen
import com.brandon.hidalgo.nycschools.ui.theme.NYCSchoolsTheme
import com.brandon.hidalgo.nycschools.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    private val mViewModel: MainViewModel by viewModels()

    private lateinit var mSchoolRepository: SchoolRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSchoolRepository = SchoolRepository(ApiClient.Factory().createNewInstance())

        fetchData()

        setContent {
            NYCSchoolsTheme {
                MainScreen(viewModel = mViewModel)
            }
        }
    }

    private fun fetchData() {
        mViewModel.fetchData(mSchoolRepository)
    }

}