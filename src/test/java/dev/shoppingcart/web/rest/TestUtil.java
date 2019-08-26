package dev.shoppingcart.web.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Utility class for testing REST controllers.
 */
public final class TestUtil {

    private static final ObjectMapper mapper = createObjectMapper();

    /** MediaType for JSON UTF8 */
    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON_UTF8;

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert.
     * @return the JSON byte array.
     * @throws IOException
     */
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    /**
     * Makes a an executes a query to the EntityManager finding all stored objects.
     * @param <T> The type of objects to be searched
     * @param em The instance of the EntityManager
     * @param clss The class type to be searched
     * @return A list of all found objects
     */
    public static <T> List<T> findAll(EntityManager em, Class<T> clss) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clss);
        Root<T> rootEntry = cq.from(clss);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    private TestUtil() {}
}
