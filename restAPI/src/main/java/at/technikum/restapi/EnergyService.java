package at.technikum.restapi;

import at.technikum.restapi.model.CurrentPercentage;
import at.technikum.restapi.model.HourlyUsage;
import at.technikum.restapi.repository.CurrentPercentageRepository;
import at.technikum.restapi.repository.HourlyUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class EnergyService {

    @Autowired
    private CurrentPercentageRepository currentPercentageRepository;

    @Autowired
    private HourlyUsageRepository hourlyUsageRepository;




    public CurrentPercentage getCurrentPercentage(){

        return currentPercentageRepository.findLatest().orElse(new CurrentPercentage()); //nicht mehr hardcoded
    }

    public List<HourlyUsage> getHistorical(LocalDateTime start, LocalDateTime end){
        return hourlyUsageRepository.findByHourBetweenOrderByHourAsc(start, end);
    }
}

