package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.model.Admin
import ui.navigation.Screen
import ui.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(viewModel: DashboardViewModel, admin: Admin?, onNavigate: (Screen) -> Unit) {
    val colorPrimaryDark = Color(0xFF112D4E)
    val colorPrimary = Color(0xFF3F72AF)
    val colorBackground = Color(0xFFF9F7F7)

    LaunchedEffect(Unit) { viewModel.loadSummary() }

    Scaffold(
            bottomBar = {
                BottomNavigationBar(
                        currentScreen = Screen.Dashboard,
                        admin = admin,
                        onNavigate = onNavigate
                )
            }
    ) { padding ->
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .background(colorBackground)
                                .padding(padding)
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Text(
                    text = "Halo, ${admin?.name ?: "Admin"}!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorPrimaryDark
            )
            Text(
                    text = "Ringkasan Desa Wisata Mangli Hari Ini",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            // Loading state
            if (viewModel.isLoading) {
                Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = colorPrimary) }
            }
            // Error state
            viewModel.errorMessage?.let { error ->
                Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) { Text(text = error, color = Color.Red, modifier = Modifier.padding(16.dp)) }
            }

            // Summary Cards
            viewModel.summary?.let { data ->
                SummaryCard(
                        title = "Total Pendapatan",
                        value = "Rp ${formatRupiah(data.totalRevenue)}",
                        color = Color(0xFF4CAF50),
                )
                Spacer(modifier = Modifier.height(12.dp))
                SummaryCard(
                        title = "Total Pesanan",
                        value = "${data.totalOrders} pesanan",
                        color = colorPrimary
                )
                Spacer(modifier = Modifier.height(12.dp))
                SummaryCard(
                        title = "Pengunjung",
                        value = "${data.totalVisitors} pengunjung",
                        color = Color(0xFFFF9800)
                )
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedButton(
                        onClick = { onNavigate(Screen.PackageManagement) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                ) { Text("Kelola Paket Wisata") }
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, color: Color) {
    Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                    modifier =
                            Modifier.width(4.dp)
                                    .height(40.dp)
                                    .background(color, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontSize = 13.sp, color = Color.Gray)
                Text(
                        text = value,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF112D4E)
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(currentScreen: Screen, admin: Admin?, onNavigate: (Screen) -> Unit) {
    val isSuperAdmin = admin?.role == "superadmin"
    // Daftar item navigasi berdasarkan role
    val items = buildList {
        if (isSuperAdmin) {
            add(NavItem("Beranda", Icons.Filled.Home, Screen.Dashboard))
        }
        add(NavItem("Scanner", Icons.Filled.QrCodeScanner, Screen.Scanner))
        add(NavItem("Kasir", Icons.Filled.ShoppingCart, Screen.Pos))
        add(NavItem("Profil", Icons.Filled.Person, Screen.Profile))
    }
    NavigationBar(containerColor = Color.White) {
        items.forEach { item ->
            NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label, fontSize = 11.sp) },
                    selected = currentScreen == item.screen,
                    onClick = { onNavigate(item.screen) },
                    colors =
                            NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color(0xFF3F72AF),
                                    selectedTextColor = Color(0xFF3F72AF),
                                    indicatorColor = Color(0xFFDBE2EF)
                            )
            )
        }
    }
}

data class NavItem(val label: String, val icon: ImageVector, val screen: Screen)

fun formatRupiah(amount: Double): String {
    val long = amount.toLong()
    return long.toString().reversed().chunked(3).joinToString(".").reversed()
}
