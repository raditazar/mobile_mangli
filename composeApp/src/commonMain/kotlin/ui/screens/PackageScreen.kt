package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.viewmodel.PackageViewModel

@Composable
fun PackageScreen(
    viewModel: PackageViewModel,
    onBack: () -> Unit
) {
    val colorPrimary = Color(0xFF3F72AF)
    val colorBackground = Color(0xFFF9F7F7)

    LaunchedEffect(Unit) {
        viewModel.loadPackages()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBackground)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (viewModel.screenMode == "list") onBack()
                else viewModel.goBackToList()
            }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Kembali")
            }
            Text(
                text = when (viewModel.screenMode) {
                    "createPkg" -> "Tambah Paket"
                    "editPkg" -> "Edit Paket"
                    "createPrice" -> "Tambah Tipe Tiket"
                    "editPrice" -> "Edit Tipe Tiket"
                    else -> "Kelola Paket"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Messages
        viewModel.errorMessage?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Text(text = error, color = Color.Red, modifier = Modifier.padding(12.dp))
            }
        }
        viewModel.successMessage?.let { msg ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Text(text = msg, color = Color(0xFF2E7D32), modifier = Modifier.padding(12.dp))
            }
        }

        if (viewModel.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorPrimary)
            }
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when (viewModel.screenMode) {
                "list" -> PackageListContent(viewModel, colorPrimary)
                "createPkg", "editPkg" -> PackageFormContent(viewModel, colorPrimary)
                "createPrice", "editPrice" -> PriceFormContent(viewModel, colorPrimary)
            }
        }
    }
}

// ═══ LIST: Daftar Paket ═══
@Composable
fun PackageListContent(viewModel: PackageViewModel, colorPrimary: Color) {
    Button(
        onClick = { viewModel.goToCreatePackage() },
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorPrimary)
    ) {
        Icon(Icons.Filled.Add, contentDescription = "Tambah")
        Spacer(modifier = Modifier.width(8.dp))
        Text("Tambah Paket Baru")
    }
    Spacer(modifier = Modifier.height(16.dp))

    viewModel.packages.forEach { pkg ->
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header paket
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(pkg.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(
                            text = if (pkg.isActive) "Aktif" else "Nonaktif",
                            fontSize = 12.sp,
                            color = if (pkg.isActive) Color(0xFF4CAF50) else Color.Gray
                        )
                    }
                    Row {
                        IconButton(onClick = { viewModel.goToEditPackage(pkg) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit", tint = colorPrimary)
                        }
                        IconButton(onClick = { viewModel.deletePackage(pkg.id) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Hapus", tint = Color.Red)
                        }
                    }
                }

                // Info paket
                Text("Max: ${pkg.maxParticipants} orang | ${pkg.durationDays} hari", fontSize = 12.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))

                // Tipe tiket
                Text("Tipe Tiket:", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))

                pkg.packagePrices?.forEach { price ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(price.name, fontSize = 14.sp)
                            Text("Rp ${formatRupiah(price.price)}", fontSize = 13.sp, color = Color.Gray)
                        }
                        Row {
                            IconButton(onClick = { viewModel.goToEditPrice(pkg, price) }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Edit", modifier = Modifier.size(18.dp))
                            }
                            IconButton(onClick = { viewModel.deletePrice(price.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Hapus", tint = Color.Red, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                // Tombol tambah tipe tiket
                TextButton(onClick = { viewModel.goToCreatePrice(pkg) }) {
                    Icon(Icons.Filled.Add, contentDescription = "Tambah", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Tambah Tipe Tiket", fontSize = 13.sp)
                }
            }
        }
    }
}

// ═══ FORM: Tambah/Edit Paket ═══
@Composable
fun PackageFormContent(viewModel: PackageViewModel, colorPrimary: Color) {
    FormField("Nama Paket", viewModel.pkgName, { viewModel.updatePkgName(it) })
    FormField("Slug (url-friendly)", viewModel.pkgSlug, { viewModel.updatePkgSlug(it) })
    FormField("Deskripsi", viewModel.pkgDescription, { viewModel.updatePkgDescription(it) })
    FormField("Durasi (hari)", viewModel.pkgDuration, { viewModel.updatePkgDuration(it) }, KeyboardType.Number)
    FormField("Max Peserta", viewModel.pkgMaxParticipants, { viewModel.updatePkgMaxParticipants(it) }, KeyboardType.Number)
    FormField("Lokasi", viewModel.pkgLocation, { viewModel.updatePkgLocation(it) })

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Status Aktif", modifier = Modifier.weight(1f))
        Switch(
            checked = viewModel.pkgIsActive,
            onCheckedChange = { viewModel.togglePkgIsActive() },
            colors = SwitchDefaults.colors(checkedTrackColor = colorPrimary)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = { viewModel.savePackage() },
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorPrimary),
        enabled = !viewModel.isLoading
    ) {
        Text("Simpan Paket", fontWeight = FontWeight.Bold)
    }
}

// ═══ FORM: Tambah/Edit Tipe Tiket ═══
@Composable
fun PriceFormContent(viewModel: PackageViewModel, colorPrimary: Color) {
    Text("Paket: ${viewModel.selectedPackage?.name}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    Spacer(modifier = Modifier.height(16.dp))

    FormField("Nama Tipe (cth: Dewasa)", viewModel.priceName, { viewModel.updatePriceName(it) })
    FormField("Harga", viewModel.priceAmount, { viewModel.updatePriceAmount(it) }, KeyboardType.Number)
    FormField("Harga Diskon (opsional)", viewModel.priceDiscount, { viewModel.updatePriceDiscount(it) }, KeyboardType.Number)

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Status Aktif", modifier = Modifier.weight(1f))
        Switch(
            checked = viewModel.priceIsActive,
            onCheckedChange = { viewModel.togglePriceIsActive() },
            colors = SwitchDefaults.colors(checkedTrackColor = colorPrimary)
        )
    }

    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = { viewModel.savePrice() },
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = colorPrimary),
        enabled = !viewModel.isLoading
    ) {
        Text("Simpan Tipe Tiket", fontWeight = FontWeight.Bold)
    }
}

// ═══ Reusable Form Field ═══
@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF3F72AF),
            focusedLabelColor = Color(0xFF3F72AF)
        )
    )
}
