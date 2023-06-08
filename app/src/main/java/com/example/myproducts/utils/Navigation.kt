package com.example.myproducts.utils

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections

object Navigation {

    fun NavController.isSafeToNavigate(direction: NavDirections) : Boolean{
        return currentDestination?.getAction(direction.actionId) != null
    }

    fun NavController.safeNavigation(parentId:Int, destinationId:Int, bundle: Bundle? = null) {
       if(currentDestination?.id == parentId){
           navigate(destinationId, bundle)
       }
    }
}