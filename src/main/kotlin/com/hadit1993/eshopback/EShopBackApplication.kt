package com.hadit1993.eshopback

import com.hadit1993.eshopback.configs.ImageUploader
import io.imagekit.sdk.ImageKit
import io.imagekit.sdk.utils.Utils
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EShopBackApplication

fun main(args: Array<String>) {


	runApplication<EShopBackApplication>(*args)
//	val imageKit = ImageKit.getInstance()
//	val config = Utils.getSystemConfig(EShopBackApplication::class.java)
//	imageKit.config = config

}
