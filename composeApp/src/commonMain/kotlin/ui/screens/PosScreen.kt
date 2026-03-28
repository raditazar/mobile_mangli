package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.model.Admin
import ui.navigation.Screen
import ui.viewmodel.PosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PosScreen(viewModel: PosViewModel, admin: Admin?, onNavigate: (Screen) -> Unit) {
    val colorPrimary = Color(0xFF3F72AF)
    val colorBackground = Color(0xFFF9F7F7)

    LaunchedEffect(Unit) { viewModel.loadPackages() }

    if (viewModel.showConfirmSheet) {
        ModalBottomSheet(onDismissRequest = { viewModel.hideConfirmation() }) {
            Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Konfirmasi Pembayaran", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Terima uang pas sejumlah", fontSize = 14.sp, color = Color.Gray)
                Text(
                        text = "Rp ${formatRupiah(viewModel.calculateTotal())}?",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorPrimary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                            onClick = { viewModel.hideConfirmation() },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                    ) { Text("Batal") }
                    Button(
                            onClick = { viewModel.processPayment() },
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) { Text("Lanjutkan") }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
    Scaffold(
            bottomBar = {
                BottomNavigationBar(
                        currentScreen = Screen.Pos,
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
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
        ) {
            viewModel.errorMessage?.let { error ->
                Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) { Text(text = error, color = Color.Red, modifier = Modifier.padding(12.dp)) }
            }
            if (viewModel.isLoading) {
                Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = colorPrimary) }
            }
            when (viewModel.currentStep) {
                1 -> PosStep1(viewModel, colorPrimary)
                2 -> PosStep2(viewModel, colorPrimary)
                3 -> PosStep3(viewModel, colorPrimary)
            }
        }
    }
}

@Composable
fun PosStep1(viewModel: PosViewModel, colorPrimary: Color) {
    Text(
            text = "Pilih Tiket",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF112D4E)
    )
    Spacer(modifier = Modifier.height(16.dp))

    viewModel.packages.forEach { pkg ->
        Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                        text = pkg.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF112D4E)
                )
                Spacer(modifier = Modifier.height(8.dp))
                pkg.packagePrices?.filter { it.isActive }?.forEach { price ->
                    Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = price.name, fontSize = 14.sp)
                            Text(
                                    text = "Rp ${formatRupiah(price.price)}",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                            )
                        }
                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(
                                    onClick = { viewModel.decrementQuantity(price.id) },
                                    enabled = (viewModel.quantities[price.id] ?: 0) > 0
                            ) { Icon(Icons.Filled.Remove, contentDescription = "Kurang") }
                            Text(
                                    text = "${viewModel.quantities[price.id] ?: 0}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(onClick = { viewModel.incrementQuantity(price.id) }) {
                                Icon(Icons.Filled.Add, contentDescription = "Tambah")
                            }
                        }
                    }
                }
            }
        }
    }
    // Total & tombol lanjut
    if (viewModel.hasSelection()) {
        Spacer(modifier = Modifier.height(8.dp))
        Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
        ) {
            Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total:", fontWeight = FontWeight.Bold)
                Text(
                        text = "Rp ${formatRupiah(viewModel.calculateTotal())}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = colorPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
                onClick = { viewModel.goToStep2() },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorPrimary)
        ) { Text("Lanjut ke Data Pengunjung", fontWeight = FontWeight.Bold) }
    }
}

@Composable
fun PosStep2(viewModel: PosViewModel, colorPrimary: Color) {
    // Tombol kembali
    TextButton(onClick = { viewModel.goBackToStep1() }) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
        Spacer(modifier = Modifier.width(4.dp))
        Text("Kembali ke pilih tiket")
    }
    Spacer(modifier = Modifier.height(8.dp))
    Text(
            text = "Data Pengunjung",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF112D4E)
    )
    Spacer(modifier = Modifier.height(16.dp))
    OutlinedTextField(
            value = viewModel.fullName,
            onValueChange = { viewModel.updateFullName(it) },
            label = { Text("Nama Lengkap") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors =
                    OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorPrimary,
                            focusedLabelColor = colorPrimary
                    )
    )
    Spacer(modifier = Modifier.height(12.dp))
    OutlinedTextField(
            value = viewModel.phoneNumber,
            onValueChange = { viewModel.updatePhoneNumber(it) },
            label = { Text("No. WhatsApp") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors =
                    OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorPrimary,
                            focusedLabelColor = colorPrimary
                    )
    )
    Spacer(modifier = Modifier.height(16.dp))
    // Ringkasan total
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total Bayar:", fontWeight = FontWeight.Bold)
            Text(
                    text = "Rp ${formatRupiah(viewModel.calculateTotal())}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = colorPrimary
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Button(
            onClick = { viewModel.showConfirmation() },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            enabled = !viewModel.isLoading
    ) { Text("Bayar Tunai & Cetak", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
}
// ═══════════════════════════════════════
// STEP 3: Sukses
// ═══════════════════════════════════════
@Composable
fun PosStep3(viewModel: PosViewModel, colorPrimary: Color) {
    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("✅", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                    text = "Pembayaran Berhasil!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
            )
            Spacer(modifier = Modifier.height(8.dp))
            viewModel.successMessage?.let { Text(text = it, fontSize = 14.sp, color = Color.Gray) }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                    onClick = { viewModel.resetTransaction() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorPrimary)
            ) { Text("Transaksi Baru") }
        }
    }
}
