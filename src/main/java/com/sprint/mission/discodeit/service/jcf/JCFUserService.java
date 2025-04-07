package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {

    private final Map<UUID, User> userList;

    public JCFUserService() {
        userList = new HashMap<>();
    }

    @Override
    public User create(String userName, String userId, String userPassword, String userEmail) {
        User user = new User(userName, userId, userPassword, userEmail);
        userList.put(user.getId(), user);
        return user;
    }

    @Override
    public User readById(UUID userId) {
        return userList.get(userId);
    }

    @Override
    public List<User> readAll() {
        return userList.values().stream().collect(Collectors.toList());
    }

    //수정 필요성 (DTO로 수정할 파라미터를 받는게 제일 깔끔할 듯, 그러나 오버라이드도 가능)
    @Override
    public void update(User user, String userName, String ModifiedUserId
            , String userPassword, String userEmail) {
        User u = userList.get(user.getId());
        u.update(userName, ModifiedUserId, userPassword, userEmail); //DTO로 넘기는게 오버로디하는 것보다 효율적일 듯

    }

    @Override
    public void deleteById(User user) {
        userList.remove(user.getId());
    }
}


