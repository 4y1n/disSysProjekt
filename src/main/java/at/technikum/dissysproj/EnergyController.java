package at.technikum.dissysproj;


import at.technikum.dissysproj.model.CurrentPercentage;
import at.technikum.dissysproj.model.HourlyUsage;
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

    @GetMapping("/current")
    public CurrentPercentage getCurrent() {
        //Beispiele!
        return new CurrentPercentage(
                LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).toString(),
                78.54,
                7.23
        );
    }

    @GetMapping("/historical")
    public List<HourlyUsage> getUsage(
            @RequestParam String start,
            @RequestParam String end) {
    //Beispiele!
        return List.of(
                new HourlyUsage("2025-01-10T14:00:00", 143.024, 130.101, 14.75),
                new HourlyUsage("2025-01-10T13:00:00", 120.5,   110.3,   12.1),
                new HourlyUsage("2025-01-10T12:00:00", 98.2,    90.0,    9.5)
        );
    }
}