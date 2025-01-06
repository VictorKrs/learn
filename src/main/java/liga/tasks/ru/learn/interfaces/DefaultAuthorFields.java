package liga.tasks.ru.learn.interfaces;


import com.fasterxml.jackson.annotation.JsonIgnore;

public interface DefaultAuthorFields {
    String getSecondName();
    String getFirstName();
    String getMiddleName();

    @JsonIgnore
    default String getFullName() {
        return getSecondName() + " " + getFirstName() + (getMiddleName() != null && !getMiddleName().isEmpty() ? " " + getMiddleName() : "");
    }
}
