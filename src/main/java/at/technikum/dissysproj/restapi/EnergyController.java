package at.technikum.dissysproj.restapi;


import at.technikum.dissysproj.restapi.model.CurrentPercentage;
import at.technikum.dissysproj.restapi.model.HourlyUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/energy")
public class EnergyController {

    @Autowired
    private EnergyService energyService;


    @GetMapping("/current")
    public CurrentPercentage getCurrent() {
        return energyService.getCurrentPercentage();
    }

    @GetMapping("/historical")
    public List<HourlyUsage> getUsage(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ){
        return energyService.getHistorical(start, end);
    }
}