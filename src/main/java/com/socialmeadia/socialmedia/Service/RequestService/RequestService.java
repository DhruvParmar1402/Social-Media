package com.socialmeadia.socialmedia.Service.RequestService;

import com.socialmeadia.socialmedia.DTO.RequestDTO;
import com.socialmeadia.socialmedia.Exception.AlreadyFriend;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Repository.RequestRepository;
import com.socialmeadia.socialmedia.Service.FriendService.FriendService;
import com.socialmeadia.socialmedia.Service.UserService.UserService;
import com.socialmeadia.socialmedia.Util.AuthenticatedUserProvider;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RequestService implements RequestServiceInterface {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSourceImpl messageSource;

    public void save(RequestDTO request) throws Exception {
        String userName=authenticatedUserProvider.getUserName();
        if(!userService.doesUserExist(request.getSentTo()))
        {
            throw new EntityNotFound(messageSource.getMessage("user.notExists"));
        }
        if(friendService.isFriend(userName,request.getSentTo()))
        {
            throw new AlreadyFriend(messageSource.getMessage("already.friends"));
        }

        request.setSentBy(userName);
        requestRepository.save(request);
    }

    public void delete(String sendTo) {
        String userName=authenticatedUserProvider.getUserName();
        RequestDTO request=requestRepository.findRequest(userName,sendTo);
        if (request != null) {
            requestRepository.delete(request);
        }
    }

    public PaginationResponse getAll(int pageSize, String lastEvaluatedKey) {
        String userName=authenticatedUserProvider.getUserName();

        List<RequestDTO> list=requestRepository.getAll(userName,lastEvaluatedKey,pageSize);

        boolean hasMore=!(list.size()<pageSize);
        lastEvaluatedKey=hasMore?list.get(list.size() - 1).getSentBy():null;

        return new PaginationResponse(list,lastEvaluatedKey,pageSize,hasMore);
    }

    public void acceptRequest(String sendBy) throws EntityNotFound {
        String userName=authenticatedUserProvider.getUserName();

        RequestDTO request=requestRepository.findRequest(sendBy,userName);

        if (request==null)
        {
            throw new EntityNotFound(messageSource.getMessage("request.notFound"));
        }

        requestRepository.delete(request);
        friendService.add(userName,sendBy);
    }

    public void rejectRequest(String sendBy) throws EntityNotFound {
        String userName=authenticatedUserProvider.getUserName();

        RequestDTO request=requestRepository.findRequest(sendBy,userName);

        if (request==null)
        {
            throw new EntityNotFound(messageSource.getMessage("request.notFound"));
        }

        requestRepository.delete(request);
    }
}
