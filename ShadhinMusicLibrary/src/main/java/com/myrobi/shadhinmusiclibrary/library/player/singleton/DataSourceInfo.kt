package com.myrobi.shadhinmusiclibrary.library.player.singleton

internal object DataSourceInfo {
    var isDataSourceError:Boolean = false
    var dataSourceErrorCode:Int? = null
    var dataSourceErrorMessage:String?=null
}