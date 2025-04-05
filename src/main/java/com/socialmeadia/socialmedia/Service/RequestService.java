package com.socialmeadia.socialmedia.Service;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.socialmeadia.socialmedia.DTO.RequestDTO;
import com.socialmeadia.socialmedia.Exception.AlreadyFriend;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Repository.RequestRepository;
import com.socialmeadia.socialmedia.Util.AuthenticatedUserProvider;
import com.socialmeadia.socialmedia.Util.MessageSourceImpl;
import com.socialmeadia.socialmedia.Util.PaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RequestService {

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
        System.out.println(request);
        requestRepository.save(request);
    }

    public void delete(String sendTo) {
        String userName=authenticatedUserProvider.getUserName();
        RequestDTO request=requestRepository.findRequest(userName,sendTo);
        requestRepository.delete(request);
    }

    public PaginationResponse getAll(int pageSize, String lastEvaluatedKey) {
        String userName=authenticatedUserProvider.getUserName();

        Map<String ,AttributeValue> startKey=new HashMap<>();
        if(lastEvaluatedKey!=null)
        {
            startKey.put("sentTo",new AttributeValue().withS(userName));
            startKey.put("sentBy",new AttributeValue().withS(lastEvaluatedKey));
        }
        
        List<RequestDTO> list=requestRepository.getAll(userName,startKey,pageSize);

        boolean hasMore=!(list.size()<pageSize);
        lastEvaluatedKey=hasMore?list.getLast().getSentBy():null;

        return new PaginationResponse(list,lastEvaluatedKey,pageSize,hasMore);
    }

    public void acceptRequest(String sendBy) throws EntityNotFound {
        String userName=authenticatedUserProvider.getUserName();

        RequestDTO request=requestRepository.findRequest(userName,sendBy);

        if (request==null)
        {
            throw new EntityNotFound(messageSource.getMessage("request.notFound"));
        }

//        RequestDTO request=new RequestDTO();
//        request.setSentTo(userName);
//        request.setSentBy(sendBy);

        requestRepository.delete(request);
        friendService.add(userName,sendBy);
    }
}
