package com.example.srosasmusicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.compose.AsyncImage
import com.example.srosasmusicapp.api.MusicApi
import com.example.srosasmusicapp.components.MiniPlayer
import com.example.srosasmusicapp.models.Album
import com.example.srosasmusicapp.models.AlbumId
import com.example.srosasmusicapp.screens.HomeScreen
import com.example.srosasmusicapp.ui.theme.PurpleGradA
import com.example.srosasmusicapp.ui.theme.PurpleGradB
import com.example.srosasmusicapp.ui.theme.SRosasMusicAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class   MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SRosasMusicAppTheme {
                val nav = rememberNavController()

                // Estado global del mini-reproductor
                var nowPlaying by remember { mutableStateOf<Album?>(null) }

                Scaffold(
                    bottomBar = {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .windowInsetsPadding(WindowInsets.navigationBars)
                        ) {
                            val a = nowPlaying
                            MiniPlayer(
                                coverUrl = a?.image,
                                title    = a?.title  ?: "Browse albums",
                                artist   = a?.artist ?: "No song playing"
                            )
                        }
                    }
                ) { inner ->
                    Box(Modifier.padding(inner)) {
                        NavHost(navController = nav, startDestination = "home") {

                            composable("home") {
                                HomeScreen(
                                    onOpenDetail = { albumId ->
                                        // Pasar Serializable según requisito
                                        nav.currentBackStackEntry?.arguments
                                            ?.putSerializable("albumId", AlbumId(albumId))
                                        nav.navigate("detail")
                                    },
                                    onPlay = { album -> nowPlaying = album }
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}



