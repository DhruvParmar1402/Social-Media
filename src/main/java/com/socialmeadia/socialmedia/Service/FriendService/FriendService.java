package com.socialmeadia.socialmedia.Service.FriendService;

import com.socialmeadia.socialmedia.DTO.FriendDTO;
import com.socialmeadia.socialmedia.DTO.UserDTO;
import com.socialmeadia.socialmedia.Repository.FriendRepository;
import com.socialmeadia.socialmedia.Repository.UserRepository;
import com.socialmeadia.socialmedia.Util.AuthenticatedUserProvider;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FriendService implements FriendsServiceInterface{

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    public void add(String userName,String sentBy) {
        FriendDTO friend=new FriendDTO();
        friend.setFriendId(sentBy);
        friend.setUserId(userName);

        friendRepository.save(friend);
        friend.setFriendId(userName);
        friend.setUserId(sentBy);
        friendRepository.save(friend);

        UserDTO user=userRepository.findUserByUserName(userName);
        user.setFriendCount(user.getFriendCount()+1);
        userRepository.save(user);

        user=userRepository.findUserByUserName(sentBy);
        user.setFriendCount(user.getFriendCount()+1);
        userRepository.save(user);
    }



    public boolean isFriend(String id1, String id2) {
        return friendRepository.find(id1, id2) != null;
    }

    public PaginationResponse getAll(int pageSize, String lastEvaluatedKey) {
        String userName=authenticatedUserProvider.getUserName();

        List<FriendDTO>friends = friendRepository.getAll(userName,pageSize,lastEvaluatedKey);
        boolean hasMore=!(friends.size()<pageSize);
        lastEvaluatedKey=hasMore?friends.getLast().getFriendId():null;
        return new PaginationResponse(friends,lastEvaluatedKey,pageSize,hasMore);
    }

    public void delete(String id2) {
        FriendDTO friend=new FriendDTO();
        String id1=authenticatedUserProvider.getUserName();

        friend.setUserId(id1);
        friend.setFriendId(id2);
        friendRepository.delete(friend);

        friend.setUserId(id2);
        friend.setFriendId(id1);
        friendRepository.delete(friend);

        UserDTO user=userRepository.findUserByUserName(id1);
        user.setFriendCount(user.getFriendCount()-1);
        userRepository.save(user);

        user=userRepository.findUserByUserName(id2);
        user.setFriendCount(user.getFriendCount()-1);
        userRepository.save(user);
    }
}
