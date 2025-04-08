package com.socialmeadia.socialmedia.Service.RequestService;

import com.socialmeadia.socialmedia.DTO.RequestDTO;
import com.socialmeadia.socialmedia.Exception.EntityNotFound;
import com.socialmeadia.socialmedia.Util.PaginationResponse;

public interface RequestServiceInterface {
    void save(RequestDTO request) throws Exception;
    void delete(String sendTo);
    PaginationResponse getAll(int pageSize, String lastEvaluatedKey);
    void acceptRequest(String sendBy) throws EntityNotFound;
    void rejectRequest(String sendBy) throws EntityNotFound ;
}
