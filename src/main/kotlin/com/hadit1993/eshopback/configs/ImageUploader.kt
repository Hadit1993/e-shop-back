package com.hadit1993.eshopback.configs

import io.imagekit.sdk.ImageKit
import io.imagekit.sdk.models.FileCreateRequest
import io.imagekit.sdk.utils.Utils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*


enum class ImageType {
    user, product
}

@Configuration
class ImageUploader {

//    @Bean
//    fun imageKit(): ImageKit {
//        val imageKit = ImageKit.getInstance()
//        val config = Utils.getSystemConfig(ImageUploader::class.java)
//        imageKit.config = config
//        return imageKit
//    }

    fun uploadImage(image: MultipartFile, imageType: ImageType): String {
//        val fileName = "${Date().time}.${image.contentType!!.split("/")[1]}"
//        val folderName = "eshop/${if(imageType == ImageType.user) "users" else "products"}"
//        val fileCreateRequest = FileCreateRequest(image.bytes, fileName)
//        fileCreateRequest.folder = folderName
//        fileCreateRequest.useUniqueFileName=true
//        val result = ImageKit.getInstance().upload(fileCreateRequest)
//        return result.url

        val staticFolderPath = "src/main/resources/static"
        val extension = getFileExtension(image.originalFilename!!)
        val uniqueFileName = "${UUID.randomUUID()}.$extension"
        val filePath = Paths.get("$staticFolderPath/${uniqueFileName}")
        Files.copy(image.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
        val url = URI.create("http://localhost:8080/images/${uniqueFileName}")
        return url.toString()
    }

    private fun getFileExtension(fileName: String): String {
        val dotIndex = fileName.lastIndexOf('.')
        return if (dotIndex == -1) "" else fileName.substring(dotIndex + 1)
    }
}