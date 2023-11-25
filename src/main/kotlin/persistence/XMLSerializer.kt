package persistence

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import models.Deck
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * Class that implements the Serializer interface for reading and writing objects in XML format using XStream library with DomDriver.
 *
 * @property file The file used for reading and writing operations.
 */
class XMLSerializer(private val file: File) : Serializer {

    /**
     * Reads an object from a file using XStream library with DomDriver and returns the read object.
     *
     * @return The object read from the file.
     * @throws Exception if any error occurs during the reading process.
     */
    override fun read(): Any {
        val xStream = XStream(DomDriver())
        xStream.allowTypes(arrayOf(Deck::class.java))
        val inputStream = xStream.createObjectInputStream(FileReader(file))
        val obj = inputStream.readObject() as Any
        inputStream.close()
        return obj
    }

    /**
     * Writes the provided object to a file using XStream library with DomDriver.
     *
     * @param obj The object to be written to the file.
     * @throws Exception if any error occurs during the writing process.
     */
    override fun write(obj: Any?) {
        val xStream = XStream(DomDriver())
        val outputStream = xStream.createObjectOutputStream(FileWriter(file))
        outputStream.writeObject(obj)
        outputStream.close()
    }
}
