package ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.api.OrderApi
import data.model.PackagePrice
import data.model.TourPackage
import data.model.CreatePackageRequest
import data.model.CreatePriceRequest
import kotlinx.coroutines.launch

class PackageViewModel : ViewModel() {
    var screenMode by mutableStateOf("list")
        private set
    var packages by mutableStateOf<List<TourPackage>>(emptyList())
        private set

    var selectedPackage by mutableStateOf<TourPackage?>(null)
        private set
    var selectedPrice by mutableStateOf<PackagePrice?>(null)
        private set

    var pkgName by mutableStateOf("")
        private set
    var pkgSlug by mutableStateOf("")
        private set
    var pkgDescription by mutableStateOf("")
        private set
    var pkgDuration by mutableStateOf("1")
        private set
    var pkgMaxParticipants by mutableStateOf("100")
        private set
    var pkgLocation by mutableStateOf("")
        private set
    var pkgIsActive by mutableStateOf(true)
        private set

    var priceName by mutableStateOf("")
        private set
    var priceAmount by mutableStateOf("")
        private set
    var priceDiscount by mutableStateOf("")
        private set
    var priceIsActive by mutableStateOf(true)
        private set

    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var successMessage by mutableStateOf<String?>(null)
        private set

    fun loadPackages() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = OrderApi.getPackages()
                packages = response.data
            } catch (e: Exception) {
                errorMessage = e.message ?: "Gagal memuat paket"
            } finally {
                isLoading = false
            }
        }
    }

    fun goToCreatePackage() {
        clearPackageForm()
        screenMode = "createPkg"
    }

    fun goToEditPackage(pkg: TourPackage) {
        selectedPackage = pkg
        pkgName = pkg.name
        pkgSlug = pkg.slug
        pkgDescription = pkg.description
        pkgDuration = pkg.durationDays.toString()
        pkgMaxParticipants = pkg.maxParticipants.toString()
        pkgLocation = pkg.location
        pkgIsActive = pkg.isActive
        screenMode = "editPkg"
    }

    fun goToCreatePrice(pkg: TourPackage) {
        selectedPackage = pkg
        clearPriceForm()
        screenMode = "createPrice"
    }

    fun goToEditPrice(pkg: TourPackage, price: PackagePrice) {
        selectedPackage = pkg
        selectedPrice = price
        priceName = price.name
        priceAmount = price.price.toLong().toString()
        priceDiscount = price.discountPrice?.toLong()?.toString() ?: ""
        priceIsActive = price.isActive
        screenMode = "editPrice"
    }

    fun goBackToList() {
        screenMode = "list"
        errorMessage = null
        successMessage = null
    }

    fun updatePkgName(v: String) {
        pkgName = v
    }
    fun updatePkgSlug(v: String) {
        pkgSlug = v
    }
    fun updatePkgDescription(v: String) {
        pkgDescription = v
    }
    fun updatePkgDuration(v: String) {
        pkgDuration = v
    }
    fun updatePkgMaxParticipants(v: String) {
        pkgMaxParticipants = v
    }
    fun updatePkgLocation(v: String) {
        pkgLocation = v
    }
    fun togglePkgIsActive() {
        pkgIsActive = !pkgIsActive
    }
    fun updatePriceName(v: String) {
        priceName = v
    }
    fun updatePriceAmount(v: String) {
        priceAmount = v
    }
    fun updatePriceDiscount(v: String) {
        priceDiscount = v
    }
    fun togglePriceIsActive() {
        priceIsActive = !priceIsActive
    }

    fun savePackage(){
        if(pkgName.isBlank() || pkgSlug.isBlank()){
            errorMessage = "Nama dan Slug Paket Wajib Diisi"
            return
        }
        viewModelScope.launch{
            isLoading = true
            errorMessage = null
            try{
                val request = CreatePackageRequest(
                    name = pkgName,
                    slug = pkgSlug,
                    description = pkgDescription,
                    durationDays = pkgDuration.toIntOrNull()?:1,
                    maxParticipants = pkgMaxParticipants.toIntOrNull()?:100,
                    location = pkgLocation,
                    isActive = pkgIsActive
                )
                if (screenMode == "createPkg"){
                    OrderApi.createPackage(request)
                    successMessage = "Paket berhasil ditambahkan"
                }else{
                    selectedPackage?.let{
                        OrderApi.updatePackage(it.id, request)
                        successMessage = "Paket berhasil diupdate"
                    }
                }
                loadPackages()
                goBackToList()
            }catch(e: Exception){
                errorMessage = e.message ?: "Gagal menyimpan paket"
            }finally{
                isLoading = false
            }
        }
    }

    fun deletePackage(id:String){
        viewModelScope.launch{
            isLoading = true
            try{
                OrderApi.deletePackage(id)
                loadPackage()
                successMessage = "Paket berhasil dihapus"
            }catch(e:Exception){
                errorMessage = e.message ?: "Gagal menghapus paket"
            }finally{
                isLoading = false
            }
        }
    }

    fun savePrice(){
        if(priceName.isBlank() || priceAmount.isBlank()){
            errorMessage = "Name dan harga wajib diisi"
            return
        }
        viewModelScope.launch{
            isLoading = true
            errorMessage = null
            try{
                val request = CreatePriceRequest(
                    tourPackageId = selectedPackage?.id ?: "",
                    name = priceName,
                    price = priceAmount.toDoubleOrNull() ?: 0.0,
                    discountPrice = priceDiscount.toDoubleOrNull(),
                    isActive = priceIsActive
                )
                if (screenMode == "createPrice"){
                    OrderApi.createPrice(request)
                    successMessage = "Tipe tiket berhasil ditambahkan"
                }else{
                    selectedPrice?.let{
                        OrderApi.updatePrice(it.id, request)
                        successMessage = "Tipe tiket berhasil diupdate"
                    }
                }
                loadPackages()
                goBackToList()
            }catch(e: Exception){
                errorMessage = e.message ?: "Gagal menyimpan tipe tiket"
            }finally{
                isLoading = false
            }
        }
    }

    fun deletePrice(id:String){
        viewModelScope.launch{
            isLoading = true
            try{
                OrderApi.deletePrice(id)
                loadPackages()
                successMessage = "Tipe tiket berhasil dihapus"
            }catch(e:Exception){
                errorMessage = e.message ?: "Gagal menghapus tipe tiket"
            }finally{
                isLoading = false
            }
        }
    }

    fun clearPackageForm() {
        pkgName = ""
        pkgSlug = ""
        pkgDescription = ""
        pkgDuration = "1"
        pkgMaxParticipants = "100"
        pkgLocation = ""
        pkgIsActive = true
    }

    fun clearPriceForm() {
        priceName = ""
        priceAmount = ""
        priceDiscount = ""
        priceIsActive = true
        selectedPrice = null
    }
}
