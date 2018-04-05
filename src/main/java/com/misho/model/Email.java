package com.misho.model;

/**
 * Created by Grant Little grant@grantlittle.me
 */
public class Email {

    private String name;
    private String emailAddress;
    private String message;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Email email = (Email) o;

        if (name != null ? !name.equals(email.name) : email.name != null) return false;
        if (emailAddress != null ? !emailAddress.equals(email.emailAddress) : email.emailAddress != null) return false;
        return message != null ? message.equals(email.message) : email.message == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (emailAddress != null ? emailAddress.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Email{");
        sb.append("name='").append(name).append('\'');
        sb.append(", emailAddress='").append(emailAddress).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
