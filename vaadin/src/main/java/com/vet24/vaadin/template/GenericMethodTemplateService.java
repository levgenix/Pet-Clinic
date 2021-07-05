package com.vet24.vaadin.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
public abstract class GenericMethodTemplateService {

    protected final RestTemplate restTemplate = new RestTemplate();
    protected ObjectMapper mapper = new ObjectMapper();

    public void getCheckedUrlResponseInConsole(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        HttpHeaders headers = response.getHeaders();
        String set_cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
        System.out.println("Set-Cookie: " + set_cookie);

        String responseBody = response.getBody();
        MediaType contentType = response.getHeaders().getContentType();
        HttpStatus statusCode = response.getStatusCode();
        System.out.println("Тело ответа: " + responseBody);
        System.out.println("Формат ответа: " + contentType + ", Код ответа: " + statusCode);
    }

    public <T> T getEntity(String url, Class<T> clazz) {
        try {
            ResponseEntity<T> responseEntity = restTemplate.getForEntity(url, clazz);
            return responseEntity.getBody();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get data, server is temporarily unavailable", e);
        }
    }

    public <T> Set<T> getSetEntity(String url, HttpMethod method) {
        try {
            ResponseEntity<Set<T>> responseEntity = restTemplate.exchange(
                    url,
                    method,
                    null,
                    new ParameterizedTypeReference<>() {
                    });
            return responseEntity.getBody();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get data, server is temporarily unavailable", e);
        }
    }

    public <T> ResponseEntity<T> saveEntity(String url, HttpMethod method, T entity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            return restTemplate.exchange(
                    url,
                    method,
                    new HttpEntity<>(mapper.writeValueAsString(entity), headers),
                    new ParameterizedTypeReference<>() {
                    });
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get data, server is temporarily unavailable", e);
        }
    }

    public <T> boolean deleteEntity(String url, T entityId) {
        try {
            restTemplate.delete(url + entityId);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
