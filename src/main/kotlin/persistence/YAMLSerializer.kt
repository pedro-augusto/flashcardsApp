package persistence
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import models.Deck
import java.io.File

class YAMLSerializer(private val file: File): Serializer {

    override fun write(obj: Any?) {
        var mapper =  ObjectMapper(YAMLFactory()).registerModule(JavaTimeModule())
        mapper.writeValue(file, obj)
    }

    override fun read(): Any? {
        var mapper = ObjectMapper(YAMLFactory()).registerModule(JavaTimeModule())
        // I cast your readValue from an array to an arraylist
        return ArrayList (mapper.readValue(file, Array<Deck>::class.java).asList())
    }
}