package com.juniorjrc.orderservice.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class OrderServiceMockHttpRequestUtils {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext context;

    public MvcResult executeHttpRequest(
            OrderServiceHttpMethodUtils method,
            String path,
            Object requestBody,
            Map<String, String> queryParams
    ) throws Exception {
        return switch (method) {
            case GET -> executeGet(path, requestBody, queryParams);
            case POST -> executePost(path, requestBody, queryParams);
            case PUT -> executePut(path, requestBody, queryParams);
        };
    }

    private MvcResult executeGet(String path, Object requestBody, Map<String, String> queryParams)
            throws Exception {
        MockHttpServletRequestBuilder requestBuilder = get(path)
                .queryParams(getQueryParams(queryParams))
                .content(toJson(requestBody))
                .contentType(APPLICATION_JSON);
        return mockMvc.perform(requestBuilder).andReturn();
    }

    private MvcResult executePost(String path, Object requestBody, Map<String, String> queryParams)
            throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post(path)
                .queryParams(getQueryParams(queryParams))
                .content(toJson(requestBody))
                .contentType(APPLICATION_JSON);
        return mockMvc.perform(requestBuilder).andReturn();
    }

    private MvcResult executePut(String path, Object requestBody, Map<String, String> queryParams)
            throws Exception {
        MockHttpServletRequestBuilder requestBuilder = put(path)
                .queryParams(getQueryParams(queryParams))
                .content(toJson(requestBody))
                .contentType(APPLICATION_JSON);
        return mockMvc.perform(requestBuilder).andReturn();
    }

    private MultiValueMap<String, String> getQueryParams(Map<String, String> queryParams) {
        LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        queryParams.forEach(multiValueMap::add);
        return multiValueMap;
    }

    public static String toJson(Object object) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        return gson.toJson(object);
    }

    public static <T> T fromJson(final String json, final Class<T> clazz) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();

        return gson.fromJson(json, clazz);
    }

    public static <T> List<T> fromJsonToList(final String json, final Class<T> clazz) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();

        return gson.fromJson(json, TypeToken.getParameterized(List.class, clazz).getType());
    }

    public static <T> Page<T> fromJsonToPage(String json, Class<T> clazz) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .registerTypeAdapter(Pageable.class, new PageableDeserializer())
                .create();

        Type pageType = TypeToken.getParameterized(PageImpl.class, clazz).getType();

        return gson.fromJson(json, pageType);
    }

    static class LocalDateAdapter implements JsonSerializer<LocalDate> {
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }

    static class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
        }
    }

    static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime> {
        public JsonElement serialize(LocalDateTime datetime, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(datetime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }

    static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }
    }

    static class PageableDeserializer implements JsonDeserializer<Pageable> {
        @Override
        public Pageable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            JsonObject jsonObject = json.getAsJsonObject();
            int page = jsonObject.get("pageNumber").getAsInt();
            int size = jsonObject.get("pageSize").getAsInt();
            return PageRequest.of(page, size);
        }
    }
}
