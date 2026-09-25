package by.grodmir.IT_project_hub.domain.service;

public class FakePasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(String rawPassword) {
        return "hash:" + rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return encodedPassword != null && encodedPassword.equals("hash:" + rawPassword);
    }
}