package com.vet24.vaadin.template.user;

import com.vet24.vaadin.template.GenericMethodTemplateService;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ClientTemplateService extends GenericMethodTemplateService {

    @Override
    public void getCheckedUrlResponseInConsole(String url) {
        super.getCheckedUrlResponseInConsole(url);
    }

    @Override
    public <T> T getEntity(String url, Class<T> clazz) {
        return super.getEntity(url, clazz);
    }

    @Override
    public <T> Set<T> getSetEntity(String url, HttpMethod method) {
        return super.getSetEntity(url, method);
    }
}
