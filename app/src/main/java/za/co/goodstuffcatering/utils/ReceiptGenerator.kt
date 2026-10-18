package za.co.goodstuffcatering.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReceiptGenerator {

    fun generateReceipt(
        context: Context,
        invoiceNumber: String,
        customerName: String,
        eventType: String,
        eventDate: String,
        totalAmount: Double
    ): File {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        paint.color = Color.WHITE
        canvas.drawRect(0f, 0f, 595f, 842f, paint)

        // Header
        paint.color = Color.parseColor("#1B4332")
        canvas.drawRect(0f, 0f, 595f, 100f, paint)
        paint.color = Color.WHITE
        paint.textSize = 28f
        paint.isFakeBoldText = true
        canvas.drawText("GOOD STUFF CATERING", 40f, 60f, paint)
        paint.textSize = 14f
        paint.isFakeBoldText = false
        canvas.drawText("Premium Catering Services", 40f, 85f, paint)

        // Details
        paint.color = Color.BLACK
        paint.textSize = 16f
        var y = 150f
        canvas.drawText("INVOICE / RECEIPT", 40f, y, paint)
        y += 35f // INCREASED SPACING
        paint.textSize = 14f
        canvas.drawText("Invoice No: $invoiceNumber", 40f, y, paint)
        y += 25f // INCREASED SPACING
        val date = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date())
        canvas.drawText("Date Issued: $date", 40f, y, paint)
        y += 45f // INCREASED SPACING

        // Customer Details
        canvas.drawText("Billed To:", 40f, y, paint)
        y += 25f // INCREASED SPACING
        canvas.drawText(customerName, 40f, y, paint)
        y += 45f // INCREASED SPACING

        // Table Header
        paint.color = Color.parseColor("#E0E0E0")
        canvas.drawRect(40f, y, 555f, y + 35f, paint)
        paint.color = Color.BLACK
        canvas.drawText("Description", 50f, y + 22f, paint)
        canvas.drawText("Amount (ZAR)", 450f, y + 22f, paint)
        y += 45f // INCREASED SPACING

        // Table Data
        canvas.drawText("$eventType - $eventDate", 50f, y, paint)
        canvas.drawText(String.format("R %.2f", totalAmount), 450f, y, paint)
        y += 35f // INCREASED SPACING

        // VAT
        val vatAmount = totalAmount - (totalAmount / 1.15)
        val subTotal = totalAmount - vatAmount
        canvas.drawLine(40f, y, 555f, y, paint)
        y += 35f // INCREASED SPACING
        canvas.drawText("Subtotal:", 400f, y, paint)
        canvas.drawText(String.format("R %.2f", subTotal), 450f, y, paint)
        y += 25f // INCREASED SPACING
        canvas.drawText("VAT (15%):", 400f, y, paint)
        canvas.drawText(String.format("R %.2f", vatAmount), 450f, y, paint)
        y += 25f // INCREASED SPACING
        canvas.drawText("Total:", 400f, y, paint)
        paint.isFakeBoldText = true
        canvas.drawText(String.format("R %.2f", totalAmount), 450f, y, paint)

        // BANKING DETAILS
        y += 60f // EXTRA SPACE HERE
        paint.isFakeBoldText = true
        paint.textSize = 14f
        canvas.drawText("Banking Details for EFT:", 40f, y, paint)
        y += 25f // INCREASED SPACING
        paint.isFakeBoldText = false
        paint.textSize = 12f
        canvas.drawText("Bank: First National Bank (FNB)", 40f, y, paint)
        y += 20f // INCREASED SPACING
        canvas.drawText("Account Name: Good Stuff Catering", 40f, y, paint)
        y += 20f // INCREASED SPACING
        canvas.drawText("Account Number: 627 409 1123", 40f, y, paint)
        y += 20f // INCREASED SPACING
        canvas.drawText("Branch Code: 250655", 40f, y, paint)
        y += 20f // INCREASED SPACING
        canvas.drawText("Reference: $invoiceNumber", 40f, y, paint)

        // Footer
        y = 780f
        paint.textSize = 12f
        paint.color = Color.GRAY
        canvas.drawText("Thank you for choosing Good Stuff Catering!", 40f, y, paint)
        canvas.drawText("POPIA Compliant Document | Generated on Mobile App", 40f, y + 25f, paint)

        pdfDocument.finishPage(page)

        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Receipt_$invoiceNumber.pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            pdfDocument.close()
        }
        return file
    }
}