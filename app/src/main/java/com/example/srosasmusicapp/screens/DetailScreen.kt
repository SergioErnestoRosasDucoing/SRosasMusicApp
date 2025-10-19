package com.example.srosasmusicapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
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
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.srosasmusicapp.api.MusicApi
import com.example.srosasmusicapp.models.Album
import com.example.srosasmusicapp.ui.theme.PurpleGradA
import com.example.srosasmusicapp.ui.theme.ScrimPurple
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DetailScreen(
    albumId: String,
    onBack: () -> Unit,
    onPlay: (Album) -> Unit
) {
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var album by remember { mutableStateOf<Album?>(null) }

    LaunchedEffect(albumId) {
        if (albumId.isBlank()) {
            loading = false
            return@LaunchedEffect
        }
        try {
            loading = true; error = null
            album = withContext(Dispatchers.IO) { MusicApi.service.getAlbum(albumId) }
        } catch (e: Exception) {
            error = e.message ?: "Error"
        } finally { loading = false }
    }


    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = PurpleGradA)
        }
        return
    }
    error?.let {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error: $it",
                color = MaterialTheme.colorScheme.error
            )
            TextButton(onClick = onBack) { Text("Volver") }
        }
        return
    }

    val a = album ?: return

    LazyColumn(
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {


        // Header con imagen grande + scrim morado y acciones
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                AsyncImage(
                    model = a.image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Scrim (degradado morado/oscuro)
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0x66000000), Color.Transparent, ScrimPurple)
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = a.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text= a.artist,
                        color = Color.White
                    )
                    Spacer(
                        Modifier.height(12.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        FilledIconButton(onClick = { onPlay(a) }) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Play")
                        }
                        OutlinedIconButton(onClick = { /* shuffle UI */ }) {
                            Icon(Icons.Default.Shuffle, contentDescription = "Shuffle")
                        }
                    }
                }
            }
        }

        // About this album + Chip de artista
        item {
            Spacer(Modifier.height(12.dp))
            Card(
                modifier = Modifier.padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "About this album",
                        style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(text = a.description)
                }
            }
            Spacer(Modifier.height(12.dp))
            AssistChip(
                onClick = { },
                label = { Text("Artist: ${a.artist}") },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Tracks",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(4.dp))
        }

        // 10 canciones ficticias
        items((1..10).map { i -> "${a.title} • Track $i" }) { trackTitle ->
            ListItem(
                headlineContent = { Text(trackTitle) },
                supportingContent = { Text(a.artist) },
                leadingContent = {
                    AsyncImage(
                        model = a.image,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                },
                trailingContent = {
                    Icon(Icons.Default.MoreVert, contentDescription = "More")
                }
            )
            Divider()
        }
    }
}