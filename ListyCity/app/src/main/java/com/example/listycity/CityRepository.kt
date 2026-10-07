package com.example.listycity

import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CityRepository {
    private val _cities = mutableStateListOf<City>()

    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")

    val cities: List<City>
        get() = _cities

    init {
        citiesRef.addSnapshotListener { value, error ->
            if (error == null && value != null) {
                _cities.clear()
                for (doc in value) {
                    _cities.add(doc.toObject(City::class.java))
                }
            }
        }
    }

    fun addCity(city: City) {
        citiesRef.document(city.name).set(city)
    }

    fun updateCity(oldCity: City, updatedCity: City) {
        if (oldCity.name != updatedCity.name) {
            citiesRef.document(oldCity.name).delete()
        }
        citiesRef.document(updatedCity.name).set(updatedCity)
    }

    fun deleteCity(city: City) {
        citiesRef.document(city.name).delete()
    }
}