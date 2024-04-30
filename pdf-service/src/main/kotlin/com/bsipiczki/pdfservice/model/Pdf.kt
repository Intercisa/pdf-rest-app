package com.bsipiczki.pdfservice.model

import java.io.ByteArrayInputStream

data class PdfResponse(
    val msg : String,
    val metaData: MetaData
)

data class MetaData(
    val title : String,
    val type : String
)

data class FileResponse(
    val id: String,
    val name: String,
    val size: Long,
    val url: String
)

data class InputStreamPdf(
    val inputStream: ByteArrayInputStream,
    val contentType: String,
    val size: Long,
    val name: String
)
