package by.grodmir.IT_project_hub.domain.service;

import by.grodmir.IT_project_hub.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


}
