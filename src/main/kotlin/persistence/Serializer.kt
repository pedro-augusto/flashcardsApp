package persistence

/**
 * Interface for serialization and deserialization operations.
 */
interface Serializer {

    /**
     * Writes the provided object to a storage medium.
     *
     * @param obj The object to be written.
     * @throws Exception if any error occurs during the writing process.
     */
    @Throws(Exception::class)
    fun write(obj: Any?)

    /**
     * Reads an object from a storage medium.
     *
     * @return The object read from the storage medium, or null if no object is available.
     * @throws Exception if any error occurs during the reading process.
     */
    @Throws(Exception::class)
    fun read(): Any?
}
