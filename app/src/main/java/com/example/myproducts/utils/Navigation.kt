package com.example.myproducts.utils

import android.os.Bundle
import androidx.navigation.NavController

object Navigation {

    fun NavController.safeNavigation(parentId:Int, destinationId:Int, bundle: Bundle? = null) {
       if(currentDestination?.id == parentId){
           navigate(destinationId, bundle)
       }
    }
}