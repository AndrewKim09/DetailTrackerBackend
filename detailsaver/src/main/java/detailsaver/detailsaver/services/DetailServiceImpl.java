package detailsaver.detailsaver.services;

import detailsaver.detailsaver.exceptions.EtBadRequestException;
import detailsaver.detailsaver.exceptions.EtResourceNotFoundException;
import detailsaver.detailsaver.models.Detail;
import detailsaver.detailsaver.repositories.DetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DetailServiceImpl implements DetailService{

    @Autowired
    private DetailRepository detailRepository;

    @Override
    public List<Detail> fetchAllDetails(int userId) {
        return detailRepository.findAll(userId);
    }

    @Override
    public Detail fetchDetailById(int userId, Integer detailId) throws EtResourceNotFoundException {
        return detailRepository.findById(userId, detailId);
    }

    @Override
    public Detail addDetail(Integer userId, String username, String password, String note) throws EtBadRequestException {
        int categoryId = detailRepository.create(userId, username, password, note);
        return detailRepository.findById(userId, categoryId);
    }

    @Override
    public void updateDetail(Integer userId, Integer detailId, Detail detail) throws EtBadRequestException {
        detailRepository.update(userId, detailId, detail);
    }

    @Override
    public void removeDetail(Integer userId, Integer detailId) throws EtResourceNotFoundException {
        detailRepository.removeById(userId, detailId);
    }
}
