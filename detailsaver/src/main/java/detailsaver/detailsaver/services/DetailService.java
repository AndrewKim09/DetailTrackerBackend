package detailsaver.detailsaver.services;

import detailsaver.detailsaver.exceptions.EtBadRequestException;
import detailsaver.detailsaver.exceptions.EtResourceNotFoundException;
import detailsaver.detailsaver.models.Detail;

import java.util.List;

public interface DetailService {
    List<Detail> fetchAllDetails(int userId);

    Detail fetchDetailById(int userId, Integer detailId) throws EtResourceNotFoundException;

    Detail addDetail(Integer userId, String username, String password, String note) throws EtBadRequestException;

    void updateDetail(Integer userId, Integer detailId, Detail detail) throws EtBadRequestException;

    void removeDetail(Integer userId, Integer detailId) throws EtResourceNotFoundException;

}
