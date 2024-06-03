package detailsaver.detailsaver.repositories;

import detailsaver.detailsaver.exceptions.EtResourceNotFoundException;
import detailsaver.detailsaver.models.Detail;

import java.util.List;

public interface DetailRepository {

    List<Detail> findAll(Integer userId) throws EtResourceNotFoundException;

    Detail findById(Integer userId, Integer detailId) throws EtResourceNotFoundException;

    Integer create(Integer userId, String username, String password, String note) throws EtResourceNotFoundException;

    void update(Integer userId, Integer DetailId,  Detail detail);

    void removeById(Integer userId, Integer categoryId);
}
