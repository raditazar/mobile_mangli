package ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
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
import ui.viewmodel.ScannerViewModel

@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel,
    admin: Admin?,
    onNavigate: (Screen) -> Unit
){
    var selectedTab by remember{
        mutableIntStateOf(0)
    }
    val tabs = listOf("QR Scanner", "Input Manual")
    val colorPrimary = Color(0xFF3F72AF)
    val colorBackground = Color(0xFFF9F7F7)

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentScreen = Screen.Scanner,
                admin = admin,
                onNavigate = onNavigate
            )
        }
    ){padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorBackground)
                .padding(padding)
        ){
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = colorPrimary
            ){
                tabs.forEachIndexed { 
                    index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = {selectedTab = index},
                            text = {Text(title)},
                            icon = {
                                Icon(
                                    if (index == 0) Icons.Filled.QrCodeScanner
                                    else Icons.Filled.Edit,
                                    contentDescription = title
                                )
                            }
                        )
                 }
            }
            when (selectedTab){
                0 -> QrScannerTab()
                1 -> ManualInputTab(viewModel)
            }
        }
    }
}

@Composable
fun QrScannerTab(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Icon(
                Icons.Filled.QrCodeScanner,
                contentDescription = "QR Scanner",
                modifier = Modifier.size(80.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "QR Scanner akan ditambahkan",
                color = Color.Gray,
                fontSize = 16.sp
            )
            Text(
                text = "Gunakan tab Input Manual",
                color = Color.LightGray,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun ManualInputTab(viewModel: ScannerViewModel){
    val colorPrimary = Color(0xFF3F72AF)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        OutlinedTextField(
            value = viewModel.orderCode,
            onValueChange = {viewModel.updateOrderCode(it)},
            label = {Text("Kode Booking")},
            leadingIcon ={
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search"
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorPrimary,
                focusedLabelColor = colorPrimary
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                viewModel.lookupOrder()
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorPrimary),
            enabled = !viewModel.isLoading
        ){
            if(viewModel.isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White
                )
            }else{
                Text(
                    text = "Cari Pesanan",
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        viewModel.errorMessage?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
            ) {
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        viewModel.successMessage?.let{ message ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ){
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = "Success",
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = message,
                        color = Color(0xFF2E7D32),
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        viewModel.orderDetail?.let{
            order ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ){
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ){
                        Text(
                            text = "Detail Pesanan",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        DetailRow("Nomor", order.orderNumber)
                        DetailRow("Name", order.fullName)
                        DetailRow("Tanggal", order.visitDate)
                        DetailRow("Status", order.status)
                        DetailRow("Total", "Rp ${formatRupiah(order.totalAmount)}")
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Jumlah Pengunjung:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                        IconButton(
                            onClick = {
                                viewModel.updateVisitorCount(viewModel.visitorCount - 1)
                            },
                            enabled = viewModel.visitorCount > 0
                        ){
                            Icon(Icons.Filled.Remove, contentDescription = "Kurang")
                        }
                        Text(
                            text = "${viewModel.visitorCount}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        IconButton(
                            onClick = {
                                viewModel.updateVisitorCount(viewModel.visitorCount + 1)
                            }
                        ){
                            Icon(Icons.Filled.Add, contentDescription = "Tambah")
                        }
                    }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.checkinManual()
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        enabled = !viewModel.isLoading
                    ){
                        Text("Check-in Pengunjung", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }


@Composable
fun DetailRow(label: String, value: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = label, color = Color.Gray, fontSize = 14.sp)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}