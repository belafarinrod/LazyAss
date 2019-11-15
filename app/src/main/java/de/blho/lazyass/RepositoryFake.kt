package de.blho.lazyass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RepositoryFake {
    companion object{
        val timeForNextAlarm:MutableLiveData<Long> by lazy {
            MutableLiveData<Long>()
        }
    }
}