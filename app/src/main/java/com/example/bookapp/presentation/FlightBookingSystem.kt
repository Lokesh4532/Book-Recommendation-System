package com.example.bookapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.State
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class Flight(
    val id: String = "",
    val flightNumber: String = "",
    val destination: String = "",
    val departureTime: String = ""
)
class FlightRepository {

    private val dbRef = FirebaseDatabase.getInstance().getReference("flights")

    fun addFlight(flight: Flight, onResult: (Boolean) -> Unit) {
        val id = dbRef.push().key ?: ""
        val newFlight = flight.copy(id = id)
        dbRef.child(id).setValue(newFlight)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }

    fun getFlights(onDataChange: (List<Flight>) -> Unit) {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val flights = snapshot.children.mapNotNull { it.getValue(Flight::class.java) }
                onDataChange(flights)
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun deleteFlight(id: String, onResult: (Boolean) -> Unit) {
        dbRef.child(id).removeValue()
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
    }
}
@HiltViewModel
class FlightViewModel @Inject constructor() : ViewModel() {

    private val repository = FlightRepository()

    private val _flights = mutableStateOf<List<Flight>>(emptyList())
    val flights:State<List<Flight>> = _flights

    init {
        fetchFlights()
    }

    fun fetchFlights() {
        repository.getFlights { flightsList ->
            _flights.value = flightsList
        }
    }

    fun addFlight(flightNumber: String, destination: String, departureTime: String) {
        val flight = Flight(flightNumber = flightNumber, destination = destination, departureTime = departureTime)
        repository.addFlight(flight) { success ->
            if (success) fetchFlights()
        }
    }

    fun deleteFlight(id: String) {
        repository.deleteFlight(id) { success ->
            if (success) fetchFlights()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightListScreen(
    viewModel: FlightViewModel,
    navController: NavController
) {
    val flights by viewModel.flights

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Flights") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_flight") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Flight")
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(flights) { flight ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Flight: ${flight.flightNumber}", style = MaterialTheme.typography.titleMedium)
                        Text("Destination: ${flight.destination}", style = MaterialTheme.typography.bodyMedium)
                        Text("Departure: ${flight.departureTime}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.deleteFlight(flight.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFlightScreen(
    viewModel: FlightViewModel,
    navController: NavController
) {
    var flightNumber by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var departureTime by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Flight") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = flightNumber,
                onValueChange = { flightNumber = it },
                label = { Text("Flight Number") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Destination") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = departureTime,
                onValueChange = { departureTime = it },
                label = { Text("Departure Time") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.addFlight(flightNumber, destination, departureTime)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Flight")
            }
        }
    }
}
@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "flight_list"
    ) {
        composable("flight_list") {
            FlightListScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable("add_flight") {
            AddFlightScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}
