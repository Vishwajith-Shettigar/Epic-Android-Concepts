package com.example.navigation_compose

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink

class NavigationExample : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      // Example for navigate and popBackStack
//      ExampleNavigateAndPopBackStack()

      // Example for popUpTo()
//      ExamplePopUpToAndNavigateUp()

      // Example for saveState and restoreState.
      ExampleSaveStateAndRestoreState()
    }
  }
}

@Composable
fun ExampleSaveStateAndRestoreState() {
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = "home") {
    composable("home") {
      HomeScreen() {
        navController.navigate("profile") {
          // pop home screen and navigate to profile screen.
          popUpTo("home") {
            inclusive = true
            // Save the state of the popped screen.
            saveState = true
          }
        }
      }
    }
    composable("profile") {
      ProfileScreen(btnLabel = "home") {
        navController.navigate("home") {
          // Restore the UI state of the home screen.
          restoreState = true
        }
      }
    }
  }
}

@Composable
fun ExamplePopUpToAndNavigateUp() {
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = "splash") {

    composable("splash") {

      SplashScreen() {
        navController.navigate("login") {
        }
      }
    }
    composable("login") {
      LoginScreen() {
        navController.navigate("home") {
// Before navigating to home, it will pop the screen from the top to the splash screen,
// and then navigate to the home screen and push the home screen onto the stack.

          popUpTo("splash") {
            inclusive = true
          }
        }
      }
    }
    composable("home") {
      HomeScreen() {
//  Behaviour
//  No Parent Destination: If the current destination has no parent defined in the navigation graph (e.g., if it was the initial destination), navigateUp() will effectively do nothing. The app remains on the current screen since there’s nowhere to navigate up to.
//  Back Navigation: If you try to navigate up and there are no more destinations in the back stack, it generally means you would be returning to the home screen of the app or exiting if the home screen is the only destination.
//  On the Home Screen: In a typical app setup, if you're already on the home screen (the first screen in your navigation graph) and you call navigateUp(), it won't change the screen since you’re at the root of the navigation hierarchy.

// In our case, it will navigate to the splash screen, despite the absence of the splash screen in the back stack.
        navController.navigateUp()

      }
    }
  }
}

@Composable
fun ExampleNavigateAndPopBackStack() {
  // Here we have created navController to navigate between screens.
  val navController = rememberNavController()

  // When the app opens, the default home screen will display because
  // we set startDestination = "home".

  NavHost(navController = navController, startDestination = "home") {

    composable("home") {

      HomeScreen() {
        // When the user clicks the button on the home screen, this will be triggered.
        navController.navigate("profile") {
        }
      }
    }
    composable("profile") {
      ProfileScreen() {
        navController.navigate("details")
      }
    }
    // Here we added a new screen.
    composable("details") {
      DetailsScreen() {
        // This will pop the screen back to the profile.
        navController.popBackStack(route = "profile", inclusive = true)
      }
    }
  }
}

@Composable
fun HomeScreen(onClick: () -> Unit) {
  var num by rememberSaveable {
    mutableStateOf(1)
  }
  Row(
    modifier = Modifier.fillMaxSize(),
  )
  {
    LazyColumn(
      modifier = Modifier
        .wrapContentSize()
        .weight(1f),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      items(130) {
        Text(text = "Home Screen!   ->" + it)
      }
    }
    Column(
      modifier = Modifier
        .wrapContentSize()
        .weight(1f)
    ) {
      Text(text = "Home Screen!   ->" + num)
      Button(onClick = { num++ }) {
        Text("Increment")
      }
      Button(onClick = { onClick() }) {
        Text("Go to profile")
      }
    }
  }
}

@Composable
fun ProfileScreen(btnLabel: String = "details", onClick: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  )
  {
    Text(text = "Profile Screen!")
    Button(onClick = { onClick() }) {
      Text("Go to ${btnLabel}")
    }
  }
}

@Composable
fun DetailsScreen(onClick: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  )
  {
    Text(text = "Details Screen!")
    Button(onClick = { onClick() }) {
      Text("Go to Home")
    }
  }
}

@Composable
fun SplashScreen(onClick: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  )
  {
    Text(text = "Splash Screen!")
    Button(onClick = { onClick() }) {
      Text("Go to Login Screen")
    }
  }
}

@Composable
fun LoginScreen(onClick: () -> Unit) {
  Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  )
  {
    Text(text = "Login Screen!")
    Button(onClick = { onClick() }) {
      Text("Go to Home Screen")
    }
  }
}
