package com.example.mediaserver.api

import com.example.mediaserver.api.dto.MediaDTO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException

@Api("Media Controller")
@RestController
@RequestMapping("/v1/api/media")
class MediaController(
    @Value("\${file.upload-dir}") var uploadDir: String,
    @Value("\${host}") var host: String
) {
    @ApiOperation("Upload media")
    @PostMapping("/{filename}")
    fun uploadMedia(@RequestPart("file") multipartFile: MultipartFile, @PathVariable filename: String): MediaDTO {
        val path = "${uploadDir}/$filename"
        val file = File(path)

        try {
            file.parentFile.mkdirs()

            multipartFile.transferTo(file)
            println("File saved successfully to $path")
        } catch (e: IOException) {
            println("Failed to save the file to $path")
            throw e
        }
        return MediaDTO("$host/v1/api/media/$filename")
    }

    @GetMapping("/{filename}")
    fun getFile(@PathVariable filename: String): ResponseEntity<FileSystemResource> {
        val filePath = "${uploadDir}/$filename"
        val file = File(filePath)

        return if (file.exists()) {
            val resource = FileSystemResource(file)
            val headers = HttpHeaders().apply {
                add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$filename\"")
            }
            ResponseEntity(resource, headers, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}
