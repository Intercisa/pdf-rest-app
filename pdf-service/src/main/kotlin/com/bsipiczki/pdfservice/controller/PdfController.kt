package com.bsipiczki.pdfservice.controller

import com.bsipiczki.pdfservice.constants.MinioBucket.IMAGE_BUCKET
import com.bsipiczki.pdfservice.model.FileResponse
import com.bsipiczki.pdfservice.model.MetaData
import com.bsipiczki.pdfservice.model.PdfResponse
import com.bsipiczki.pdfservice.service.MinioService
import com.bsipiczki.pdfservice.service.PdfService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class PdfController(
    private val minioService: MinioService,
    private val pdfService: PdfService
) {
    @PostMapping("/single/upload")
    fun uploadPdf(
        @RequestParam("file") file: MultipartFile,
    ): ResponseEntity<PdfResponse> {
        val writeResponse = minioService.uploadToBucket(file)
        println("writeResponse: ${writeResponse.etag()}")
        pdfService.convertPdfToImage(file).forEach {
            val uploadToBucket = minioService.uploadToBucket(it, IMAGE_BUCKET)
            println("uploadToBucket: $uploadToBucket")
        }

        return ResponseEntity.ok(
            PdfResponse(
                msg = "Pdf uploaded",
                metaData = MetaData(file.originalFilename!!, file.contentType!!)
            ))
    }

    @GetMapping("/list/")
    fun getFiles(@RequestParam(required = false) bucketName: String?):ResponseEntity<List<FileResponse>> =
        bucketName?.let {
            ResponseEntity.ok(minioService.getFileListResponse(it))
        } ?:
            ResponseEntity.ok(minioService.getFileListResponse())

    @GetMapping("/check")
    fun check(): ResponseEntity<String> = ResponseEntity.ok("Pdf service is running")
}