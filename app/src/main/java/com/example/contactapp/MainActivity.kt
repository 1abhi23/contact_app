package com.example.contactapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.contactapp.model.ContactDatabase.Companion.getDatabase
import com.example.contactapp.model.ContactRepository
import com.example.contactapp.model.Contacts
import com.example.contactapp.ui.theme.BlueJC
import com.example.contactapp.ui.theme.ContactAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = getDatabase(this)
        val repository = ContactRepository(database.contactDao())

        val viewModel: ContactViewModel by viewModels { ContactViewModelFactory(repository) }

        enableEdgeToEdge()
        setContent {
            ContactAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "contactList") {
                    composable("contactList") { ContactListScreen(viewModel, navController) }
                    composable("addContact") { AddContactScreen(viewModel, navController) }
                    composable("contactDetail/{contactId}") { navBackStackEntry ->
                        val contactId = navBackStackEntry.arguments?.getString("contactId")?.toInt()
                        val contact =
                            viewModel.contacts.collectAsState().value.find { it.id == contactId }
                        contact?.let { ContactDetailScreen(it, viewModel, navController) }
                    }
                    composable("editContact/{contactId}") { navBackStackEntry ->
                        val contactId = navBackStackEntry.arguments?.getString("contactId")?.toInt()
                        val contact =
                            viewModel.contacts.collectAsState().value.find { it.id == contactId }
                        contact?.let { EditContactScreen(it, viewModel, navController) }
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    viewModel: ContactViewModel,
    navController: NavController
) {
    val context = LocalContext.current.applicationContext

    Scaffold(topBar = {
        TopAppBar(
            modifier = Modifier.statusBarsPadding(),
            title = {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically)
                ) {
                    Text(text = "Add Contact", fontSize = 18.sp)
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    Toast.makeText(context, "Add Contact", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.contacticon),
                        contentDescription = "Top Bar"
                    )
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = BlueJC,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )
    }, floatingActionButton = {
        FloatingActionButton(containerColor = BlueJC, onClick = {
            navController.navigate("addContact")
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Contact")
        }
    }) { paddingValues ->
        val contacts by viewModel.contacts.collectAsState()
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(contacts) { contact ->
                ContactItem(contacts = contact) {
                    navController.navigate("contactDetail/${contact.id}")
                }
            }
        }
    }
}

@Composable
fun ContactItem(contacts: Contacts, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(contacts.image),
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                contentDescription = contacts.name
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(contacts.name, modifier = Modifier.padding(start = 8.dp))
        }
    }
}