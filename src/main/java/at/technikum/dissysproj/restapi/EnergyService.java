package at.technikum.dissysproj.restapi;

import at.technikum.dissysproj.restapi.model.CurrentPercentage;
import at.technikum.dissysproj.restapi.model.HourlyUsage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class EnergyService {
    public CurrentPercentage getCurrentPercentage(){
        CurrentPercentage cp = new CurrentPercentage();
        cp.setHour(LocalDateTime.now());
        cp.setCommunityPoolUsed(20); //demodata (same in list)
        cp.setGridPortion(30); //demodata
        return cp;
    }

    public List<HourlyUsage> getHistorical(LocalDateTime start, LocalDateTime end){
        HourlyUsage use1 = new HourlyUsage();
        use1.setHour(LocalDateTime.now()); //change to historical later
        use1.setGridUsed(10);
        use1.setCommunityUsed(20);
        use1.setCommunityProduced(10);

        HourlyUsage use2 = new HourlyUsage();
        use2.setHour(LocalDateTime.of(2025, 1, 10, 14, 0));
        use2.setCommunityProduced(18.0);
        use2.setCommunityUsed(18.0);
        use2.setGridUsed(1.0);

        return List.of(use1, use2);
    }
}

