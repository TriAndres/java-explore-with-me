package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BaseClient {
    protected final RestTemplate rest;

    protected ResponseEntity<Object> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    protected <F> ResponseEntity<F> getList(String uri, @Nullable Map<String, Object> parameters,
                                            ParameterizedTypeReference<F> type) {
        return sendRequestType(uri, HttpMethod.GET, null, parameters, type);
    }

    private <T, F> ResponseEntity<F> sendRequestType(String path, HttpMethod method,
                                                     @Nullable T body,
                                                     @Nullable Map<String, Object> parameters,
                                                     ParameterizedTypeReference<F> type) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<F> statsServerResponse;
        try {
            if (parameters != null) {
                statsServerResponse = rest.exchange(path, method, requestEntity, type, parameters);
            } else {
                statsServerResponse = rest.exchange(path, method, requestEntity, type);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAs(type));
        }
        return prepareGatewayResponseType(statsServerResponse);
    }

    protected <T> ResponseEntity<Object> post(String path, T body) {
        return post(path, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, parameters, body);
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> statsServerResponse;
        try {
            if (parameters != null) {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                statsServerResponse = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareGatewayResponse(statsServerResponse);
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }

    private static <T> ResponseEntity<T> prepareGatewayResponseType(ResponseEntity<T> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }
        return responseBuilder.build();
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}