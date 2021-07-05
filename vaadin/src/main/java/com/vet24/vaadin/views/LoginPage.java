package com.vet24.vaadin.views;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Tag("sa-login-view")
@Route(value = LoginPage.ROUTE)
@PageTitle("Login")
public class LoginPage extends VerticalLayout {

    public static final String ROUTE = "login";

    private LoginOverlay login = new LoginOverlay();

    public LoginPage() {
        login.setAction("login");
        login.setOpened(true);
        login.setTitle("Vaadin Title");
        login.setDescription("Login Overlay Example");
        getElement().appendChild(login.getElement());
    }
}
