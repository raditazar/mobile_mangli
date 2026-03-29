package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.model.Admin
import ui.navigation.Screen

@Composable
fun ProfileScreen(
    admin: Admin?,
    onNavigate: (Screen) -> Unit,
    onLogout: () -> Unit
) {
    val colorPrimary = Color(0xFF3F72AF)
    val colorBackground = Color(0xFFF9F7F7)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentScreen = Screen.Profile,
                admin = admin,
                onNavigate = onNavigate
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorBackground)
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(colorPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = admin?.name?.firstOrNull()?.uppercase() ?: "A",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = admin?.name ?: "Admin",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF112D4E)
            )
            Text(
                text = admin?.role ?: "admin",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Info Cards
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    ProfileInfoRow(
                        icon = Icons.Filled.Person,
                        label = "Nama",
                        value = admin?.name ?: "-"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    ProfileInfoRow(
                        icon = Icons.Filled.Email,
                        label = "Email",
                        value = admin?.email ?: "-"
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    ProfileInfoRow(
                        icon = Icons.Filled.Shield,
                        label = "Role",
                        value = admin?.role ?: "-"
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Logout
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Icon(Icons.Filled.Logout, contentDescription = "Logout")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label, tint = Color(0xFF3F72AF), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Medium)
        }
    }
}
