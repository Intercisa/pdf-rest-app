package com.bsipiczki.pdfservice.service

import com.bsipiczki.pdfservice.constants.ImageExtension.PNG_EXT
import com.bsipiczki.pdfservice.model.InputStreamPdf
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@Service
class PdfService {
    fun convertPdfToImage(file: MultipartFile): MutableList<InputStreamPdf> {
        val document: PDDocument = PDDocument.load(file.inputStream)
        val pdfRenderer = PDFRenderer(document)
        val pdfInputStreamList = mutableListOf<InputStreamPdf>()
        document.pages.withIndex().forEach {
            val bim = pdfRenderer.renderImageWithDPI(it.index, 300f, ImageType.RGB)
            val os = ByteArrayOutputStream()
            ImageIO.write(bim, PNG_EXT, os)
            val inputStream = ByteArrayInputStream(os.toByteArray())
            val formattedName = file.originalFilename!!.substringBeforeLast(".")
            pdfInputStreamList.add(
                 InputStreamPdf(
                    inputStream = inputStream,
                    contentType = file.contentType!!,
                    size = os.size().toLong(),
                    name = "${formattedName}_${it.index}.$PNG_EXT"
                )
            )
        }
        document.close()
        return pdfInputStreamList
    }
}