package com.thopham.projects.desktop.demo.domain.api

import org.springframework.stereotype.Component
import java.net.NetworkInterface

@Component
class DeviceAPI{
    fun fetchMacAddress(): String {
        for(network in NetworkInterface.getNetworkInterfaces()){
            val macAddress = extractMacAddress(network)
            if(!macAddress.isNullOrBlank())
                return macAddress
        }
        return "A:B:C:X:Y:Z"
    }
    fun extractMacAddress(network: NetworkInterface): String?{
        val mac = network.hardwareAddress
        if(mac == null) return null
        val macString = StringBuilder()
        for (i in mac.indices) {
            macString.append(String.format("%02X%s", mac[i], if (i < mac.size - 1) "-" else ""))
        }
        return macString.toString()
    }
}