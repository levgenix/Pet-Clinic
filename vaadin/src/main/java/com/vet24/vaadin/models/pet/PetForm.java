package com.vet24.vaadin.models.pet;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vet24.vaadin.models.enums.Gender;
import com.vet24.vaadin.models.enums.PetType;

import java.time.LocalDate;

public class PetForm extends VerticalLayout {
    private TextField name = new TextField("","Name");
    private TextField avatar = new TextField("","Avatar");
    private TextField breed = new TextField("","Breed");
    private DatePicker birthDay = new DatePicker(LocalDate.now());
    private ComboBox<PetType> petType = new ComboBox<>();
    private ComboBox<Gender> gender = new ComboBox<>();
    private Pet pet;

    private Button save = new Button("Save");
    private Button close = new Button("Cancel");

    private Binder<Pet> binder = new Binder<>(Pet.class);

    public PetForm() {
        add(name, avatar, breed, birthDay, petType, gender, createButtonsLayout());
        binder.bindInstanceFields(this);
        petType.setItems(PetType.values());
        gender.setItems(Gender.values());
    }

    public void setPet(Pet pet){
        this.pet = pet;
        binder.readBean(pet);
        fillFields(pet);
        name.focus();
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, close);
    }

    private void fillFields(Pet pet) {
        name.setValue(pet.getName() != null ? pet.getName() : "");
        avatar.setValue(pet.getAvatar() != null ? pet.getAvatar() : "");
        breed.setValue(pet.getBreed() != null ? pet.getBreed() : "Unknown");
        birthDay.setValue(pet.getBirthDay() != null ? pet.getBirthDay() : LocalDate.now());
        petType.setValue(pet.getPetType() != null ? pet.getPetType() : PetType.DOG);
        gender.setValue(pet.getGender() != null ? pet.getGender() : Gender.MALE);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(pet);
            fireEvent(new SaveEvent(this, pet));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class PetFormEvent extends ComponentEvent<PetForm> {
        private Pet pet;
        protected PetFormEvent(PetForm source, Pet pet) {
            super(source, false);
            this.pet = pet;
        }
        public Pet getPet() {
            return pet;
        }
    }

    public static class SaveEvent extends PetFormEvent {
        SaveEvent(PetForm source, Pet pet) {
            super(source, pet);
        }
    }

    public static class CloseEvent extends PetFormEvent {
        CloseEvent(PetForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
