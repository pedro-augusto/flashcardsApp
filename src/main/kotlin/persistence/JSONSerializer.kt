package persistence

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver
import models.Deck
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * Class that implements the Serializer interface for reading and writing objects in JSON format.
 *
 * @property file The file used for reading and writing operations.
 */
class JSONSerializer(private val file: File) : Serializer {

    /**
     * Reads an object from a file using XStream library and returns the read object.
     *
     * @return The object read from the file.
     * @throws Exception if any error occurs during the reading process.
     */
    @Throws(Exception::class)
    override fun read(): Any {
        val xStream = XStream(JettisonMappedXmlDriver())
        xStream.allowTypes(arrayOf(Deck::class.java))
        val inputStream = xStream.createObjectInputStream(FileReader(file))
        val obj = inputStream.readObject() as Any
        inputStream.close()
        return obj
    }

    /**
     * Writes the provided object to a file using XStream library.
     *
     * @param obj The object to be written to the file.
     * @throws Exception if any error occurs during the writing process.
     */
    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val xStream = XStream(JettisonMappedXmlDriver())
        val outputStream = xStream.createObjectOutputStream(FileWriter(file))
        outputStream.writeObject(obj)
        outputStream.close()
    }
}
