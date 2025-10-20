package org.ruphile.bigdata.controller;

import org.ruphile.bigdata.entity.RiskLog;
import org.ruphile.bigdata.entity.RiskLogKey;
import org.ruphile.bigdata.service.RiskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/risk")
public class RiskController {
    @Autowired
    private RiskService riskService;

    @GetMapping("/query-by-id")
    public String queryRisk(String sno, String uno) {
        RiskLog risk = riskService.getRiskById(new RiskLogKey(sno, uno));
        if (risk == null) {
            return "Risk record not found";
        }
        return risk.toString();
    }

    @PostMapping("/add/{sno}/{uno}")
    public String addRisk(@PathVariable String sno, @PathVariable String uno, @RequestBody RiskLog risk) {
        riskService.putRisk(new RiskLogKey(sno, uno), risk);
        return "success";
    }

    /*@GetMapping("/filter")
    public String testFilter() {
        riskService.testFilter();
        return "success";
    }*/
}
