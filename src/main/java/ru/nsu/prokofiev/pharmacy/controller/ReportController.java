// src/main/java/ru/nsu/prokofiev/pharmacy/controller/ReportController.java
package ru.nsu.prokofiev.pharmacy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.prokofiev.pharmacy.model.*;
import ru.nsu.prokofiev.pharmacy.service.ReportService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // ------------ 1. Просроченные клиенты ------------

    @GetMapping("/notPickedUp")
    public ResponseEntity<Map<String, Object>> getMissedCustomers() {
        List<MissedCustomerDto> list = reportService.getMissedCustomers();
        Long total = reportService.countMissedCustomers();

        Map<String, Object> response = new HashMap<>();
        response.put("customers", list);
        response.put("total", total);
        return ResponseEntity.ok(response);
    }

    // ------------ 2. Ожидающие поступления ------------

    @GetMapping("/waitingStock")
    public ResponseEntity<Map<String, Object>> getWaitingStock(
            @RequestParam(name = "typeId", required = false) Integer typeId
    ) {
        List<WaitingCustomerDto> list = reportService.getWaitingCustomers(typeId);
        long total = list.isEmpty() ? 0L : list.get(0).getTotalWaiting();

        Map<String, Object> response = new HashMap<>();
        response.put("rows", list);
        response.put("total", total);
        return ResponseEntity.ok(response);
    }

    // ------------ 3. Клиенты, заказывавшие за период ------------

    @GetMapping("/orderedCustomers")
    public ResponseEntity<Map<String,Object>> getOrderedCustomers(
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate")   String endDate,
            @RequestParam(name = "medicamentIds", required = false) String medicamentIdsCsv,
            @RequestParam(name = "typeIds",       required = false) String typeIdsCsv
    ) {
        Integer[] medIds = parseCsvToIntegerArray(medicamentIdsCsv);
        Integer[] typeIds = parseCsvToIntegerArray(typeIdsCsv);

        List<CustomerOrderedDto> list = reportService.getCustomersOrdered(
                startDate, endDate, medIds, typeIds
        );
        long total = list.isEmpty() ? 0L : list.get(0).getTotalCustomers();

        Map<String,Object> response = new HashMap<>();
        response.put("rows", list);
        response.put("total", total);
        return ResponseEntity.ok(response);
    }

    // ------------ 4. Часто делающие заказы ------------

    @GetMapping("/topCustomers")
    public ResponseEntity<Map<String,Object>> getTopCustomers(
            @RequestParam(name = "typeId", required = false) Integer typeId,
            @RequestParam(name = "medicamentIds", required = false) String medicamentIdsCsv
    ) {
        List<FrequentCustomerDto> list;
        if (typeId != null) {
            list = reportService.getTopCustomersByType(typeId);
        } else {
            Integer[] medIds = parseCsvToIntegerArray(medicamentIdsCsv);
            list = reportService.getTopCustomersByMedicaments(medIds);
        }
        long total = list.isEmpty() ? 0L : list.get(0).getTotalCustomers();

        Map<String,Object> response = new HashMap<>();
        response.put("rows", list);
        response.put("total", total);
        return ResponseEntity.ok(response);
    }

    // ------------ 5. Топ-10 часто используемых медикаментов ------------

    @GetMapping("/topUsedDrugs")
    public ResponseEntity<Map<String,Object>> getTopUsedDrugs(
            @RequestParam(name = "typeId", required = false) Integer typeId
    ) {
        List<TopUsedDrugDto> list;
        if (typeId == null) {
            list = reportService.getTopUsedDrugs();
        } else {
            list = reportService.getTopUsedDrugsByType(typeId);
        }
        long total = list.size();

        Map<String,Object> response = new HashMap<>();
        response.put("rows", list);
        response.put("total", total);
        return ResponseEntity.ok(response);
    }

    // ------------ 6. Объём использованных пачек за период ------------

    @GetMapping("/usedVolume")
    public ResponseEntity<Map<String,Object>> getUsedVolume(
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate")   String endDate,
            @RequestParam(name = "medicamentIds", required = false) String medicamentIdsCsv
    ) {
        Integer[] medIds = parseCsvToIntegerArray(medicamentIdsCsv);
        List<UsedVolumeDto> list = reportService.getUsedVolume(startDate, endDate, medIds);

        Map<String,Object> response = new HashMap<>();
        response.put("rows", list);
        return ResponseEntity.ok(response);
    }

    // ------------ 7. Критические медикаменты ------------

    @GetMapping("/criticalMedications")
    public ResponseEntity<Map<String,Object>> getCriticalMedications() {
        List<CriticalMedicationDto> list = reportService.getCriticalMedications();
        long total = list.isEmpty() ? 0L : list.get(0).getTotalFlaggedMedications();

        Map<String,Object> response = new HashMap<>();
        response.put("rows", list);
        response.put("total", total);
        return ResponseEntity.ok(response);
    }

    // ------------ 8. Лекарства с минимальным запасом ------------

    @GetMapping("/minStock")
    public ResponseEntity<Map<String,Object>> getMinStock(
            @RequestParam(name = "typeId", required = false) Integer typeId
    ) {
        if (typeId == null) {
            List<MinStockDto> list = reportService.getMinStock();
            long total = list.isEmpty() ? 0L : list.get(0).getCountAtMinimum();
            Map<String,Object> resp = new HashMap<>();
            resp.put("rows", list);
            resp.put("total", total);
            return ResponseEntity.ok(resp);
        } else {
            List<MinStockByTypeDto> list = reportService.getMinStockByType(typeId);
            long total = list.isEmpty() ? 0L : list.get(0).getCountAtMinimumInType();
            Map<String,Object> resp = new HashMap<>();
            resp.put("rows", list);
            resp.put("total", total);
            return ResponseEntity.ok(resp);
        }
    }

    // ------------ 9. Цена готового пакета и стоимость компонентов ------------

    @GetMapping("/packagePrice")
    public ResponseEntity<Map<String,Object>> getPackagePrice(
            @RequestParam(name = "medicamentId") Long medicamentId
    ) {
        List<PreparedPackageDto> list = reportService.getPreparedPackages(medicamentId);
        Map<String,Object> resp = new HashMap<>();
        resp.put("rows", list);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/componentCosts")
    public ResponseEntity<Map<String,Object>> getComponentCosts(
            @RequestParam(name = "packageId") Long packageId
    ) {
        List<ComponentCostDto> list = reportService.getComponentCosts(packageId);
        Map<String,Object> resp = new HashMap<>();
        resp.put("rows", list);
        return ResponseEntity.ok(resp);
    }

    // ------------ 10. Заказы в производстве ------------

    @GetMapping("/inProductionOrders")
    public ResponseEntity<Map<String,Object>> getOrdersInProduction() {
        List<InProductionOrderDto> list = reportService.getOrdersInProduction();
        long total = list.isEmpty() ? 0L : list.get(0).getTotalOrdersInProduction();
        Map<String,Object> resp = new HashMap<>();
        resp.put("rows", list);
        resp.put("total", total);
        return ResponseEntity.ok(resp);
    }

    // ------------ 11. Препараты, требуемые в производстве ------------

    @GetMapping("/requiredDrugs")
    public ResponseEntity<Map<String,Object>> getRequiredDrugsInProduction() {
        List<RequiredDrugDto> list = reportService.getRequiredDrugsInProduction();
        long total = list.isEmpty() ? 0L : list.get(0).getTotalRequiredMedications();
        Map<String,Object> resp = new HashMap<>();
        resp.put("rows", list);
        resp.put("total", total);
        return ResponseEntity.ok(resp);
    }

    // ------------ 12. Технологии приготовления ------------

    @GetMapping("/technologiesByType")
    public ResponseEntity<Map<String,Object>> getTechnologiesByType(
            @RequestParam(name = "typeIds") String typeIdsCsv
    ) {
        Integer[] typeIds = parseCsvToIntegerArray(typeIdsCsv);
        List<TechnologyInfoDto> list = reportService.getTechnologiesByType(typeIds);
        Map<String,Object> resp = new HashMap<>();
        resp.put("rows", list);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/technologiesByMedicaments")
    public ResponseEntity<Map<String,Object>> getTechnologiesByMedicaments(
            @RequestParam(name = "medicamentIds") String medicamentIdsCsv
    ) {
        Integer[] medIds = parseCsvToIntegerArray(medicamentIdsCsv);
        List<TechnologyInfoDto> list = reportService.getTechnologiesByMedicaments(medIds);
        Map<String,Object> resp = new HashMap<>();
        resp.put("rows", list);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/technologiesInProduction")
    public ResponseEntity<Map<String,Object>> getTechnologiesInProduction() {
        List<TechnologyInfoDto> list = reportService.getTechnologiesInProduction();
        Map<String,Object> resp = new HashMap<>();
        resp.put("rows", list);
        return ResponseEntity.ok(resp);
    }

    private Integer[] parseCsvToIntegerArray(String csv) {
        if (csv == null || csv.isBlank()) {
            return new Integer[0];
        }
        String[] parts = csv.split(",");
        Integer[] arr = new Integer[parts.length];
        for (int i = 0; i < parts.length; i++) {
            arr[i] = Integer.valueOf(parts[i].trim());
        }
        return arr;
    }
}
