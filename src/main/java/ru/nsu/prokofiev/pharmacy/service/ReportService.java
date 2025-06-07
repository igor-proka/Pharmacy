// src/main/java/ru/nsu/prokofiev/pharmacy/service/ReportService.java
package ru.nsu.prokofiev.pharmacy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.prokofiev.pharmacy.model.*;
import ru.nsu.prokofiev.pharmacy.repository.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // 1. Просроченные клиенты
    public List<MissedCustomerDto> getMissedCustomers() {
        List<MissedCustomerProjection> projList = reportRepository.findMissedCustomers();
        return projList.stream()
                .map(p -> new MissedCustomerDto(
                        p.getCustomerId(),
                        p.getFullName(),
                        p.getPhoneNumber(),
                        p.getAddress(),
                        p.getOrderId(),
                        p.getAppointedDatetime().toLocalDateTime()
                ))
                .collect(Collectors.toList());
    }

    public Long countMissedCustomers() {
        return reportRepository.countMissedCustomers();
    }

    // 2. Ожидающие поступления
    public List<WaitingCustomerDto> getWaitingCustomers(Integer typeId) {
        List<WaitingCustomerProjection> projections;
        if (typeId == null) {
            projections = reportRepository.findWaitingCustomers();
        } else {
            projections = reportRepository.findWaitingCustomersByType(typeId);
        }
        return projections.stream()
                .map(p -> new WaitingCustomerDto(
                        p.getCustomerId(),
                        p.getCustomerName(),
                        p.getPhoneNumber(),
                        p.getAddress(),
                        p.getPackageId(),
                        p.getMedicamentName(),
                        p.getWaitingAmount(),
                        (p.getTotalWaitingCustomers() == null ? 0L : p.getTotalWaitingCustomers().longValue())
                ))
                .collect(Collectors.toList());
    }

    // 3. Клиенты, заказывавшие за период
    public List<CustomerOrderedDto> getCustomersOrdered(String startDate, String endDate,
                                                        Integer[] medicamentIds, Integer[] typeIds) {
        String startTs = startDate + " 00:00:00";
        String endTs   = endDate   + " 23:59:59";
        List<CustomersOrderedProjection> projList = reportRepository.findCustomersOrdered(
                startTs, endTs, medicamentIds, typeIds);
        return projList.stream()
                .map(p -> new CustomerOrderedDto(
                        p.getCustomerId(),
                        p.getCustomerName(),
                        p.getPhoneNumber(),
                        p.getAddress(),
                        p.getTotalCustomers().longValue()
                ))
                .collect(Collectors.toList());
    }

    // 4. Часто делающие заказы
    public List<FrequentCustomerDto> getTopCustomersByType(Integer typeId) {
        List<FrequentCustomerProjection> projList = reportRepository.findTopCustomersByType(typeId);
        return projList.stream()
                .map(p -> new FrequentCustomerDto(
                        p.getCustomerId(),
                        p.getCustomerName(),
                        p.getPhoneNumber(),
                        p.getAddress(),
                        p.getOrdersCount(),
                        p.getTotalCustomers().longValue()
                ))
                .collect(Collectors.toList());
    }

    public List<FrequentCustomerDto> getTopCustomersByMedicaments(Integer[] medicamentIds) {
        List<FrequentCustomerProjection> projList = reportRepository.findTopCustomersByMedicaments(medicamentIds);
        return projList.stream()
                .map(p -> new FrequentCustomerDto(
                        p.getCustomerId(),
                        p.getCustomerName(),
                        p.getPhoneNumber(),
                        p.getAddress(),
                        p.getOrdersCount(),
                        p.getTotalCustomers().longValue()
                ))
                .collect(Collectors.toList());
    }

    // 5. Топ-10 часто используемых медикаментов
    public List<TopUsedDrugDto> getTopUsedDrugs() {
        List<TopUsedDrugProjection> projections = reportRepository.findTopUsedDrugs();
        return projections.stream()
                .map(p -> new TopUsedDrugDto(
                        p.getMedicamentId(),
                        p.getMedicamentName(),
                        p.getUsageScore().longValue()
                ))
                .collect(Collectors.toList());
    }

    public List<TopUsedDrugDto> getTopUsedDrugsByType(Integer typeId) {
        List<TopUsedDrugProjection> projections = reportRepository.findTopUsedDrugsByType(typeId);
        return projections.stream()
                .map(p -> new TopUsedDrugDto(
                        p.getMedicamentId(),
                        p.getMedicamentName(),
                        p.getUsageScore().longValue()
                ))
                .collect(Collectors.toList());
    }

    // 6. Объём использованных пачек за период
    public List<UsedVolumeDto> getUsedVolume(String startDate, String endDate, Integer[] medicamentIds) {
        String startTs = startDate + " 00:00:00";
        String endTs   = endDate   + " 23:59:59";
        List<UsedVolumeProjection> projections =
                reportRepository.findUsedVolume(startTs, endTs, medicamentIds);
        return projections.stream()
                .map(p -> new UsedVolumeDto(
                        p.getMedicamentId(),
                        p.getMedicamentName(),
                        p.getTotalPackagesUsed().longValue()
                ))
                .collect(Collectors.toList());
    }


    // 7. Лекарства на критическом уровне
    public List<CriticalMedicationDto> getCriticalMedications() {
        return reportRepository.findCriticalMedications().stream()
                .map(p -> new CriticalMedicationDto(
                        p.getMedicamentId(),
                        p.getMedicamentName(),
                        p.getMedicamentType(),
                        p.getAvailableAmount(),
                        p.getCriticalAmount(),
                        p.getTotalFlaggedMedications().longValue()
                ))
                .collect(Collectors.toList());
    }

    // 8. Лекарства с минимальным запасом
    public List<MinStockDto> getMinStock() {
        return reportRepository.findMinStock().stream()
                .map(p -> new MinStockDto(
                        p.getMedicamentId(),
                        p.getMedicamentName(),
                        p.getAvailableAmount(),
                        p.getMinimalOverall(),
                        p.getCountAtMinimum().longValue()
                ))
                .collect(Collectors.toList());
    }

    public List<MinStockByTypeDto> getMinStockByType(Integer typeId) {
        return reportRepository.findMinStockByType(typeId).stream()
                .map(p -> new MinStockByTypeDto(
                        p.getMedicamentId(),
                        p.getMedicamentName(),
                        p.getAvailableAmount(),
                        p.getMinimalInType(),
                        p.getCountAtMinimumInType().longValue()
                ))
                .collect(Collectors.toList());
    }

    // 9. Цена готового пакета и стоимость компонентов
    public List<PreparedPackageDto> getPreparedPackages(Long medicamentId) {
        return reportRepository.findPreparedPackages(medicamentId).stream()
                .map(p -> new PreparedPackageDto(
                        p.getPackageId(),
                        p.getMedicamentName(),
                        p.getPrice()
                ))
                .collect(Collectors.toList());
    }

    public List<ComponentCostDto> getComponentCosts(Long packageId) {
        return reportRepository.findComponentCosts(packageId).stream()
                .map(p -> new ComponentCostDto(
                        p.getTechnologyId(),
                        p.getComponentPackageId(),
                        p.getComponentMedicamentName(),
                        p.getComponentAmount(),
                        p.getComponentPrice(),
                        p.getComponentTotalCost(),
                        p.getProducedPackages(),
                        p.getCostPerOnePackage()
                ))
                .collect(Collectors.toList());
    }

    // 10. Заказы в производстве
    public List<InProductionOrderDto> getOrdersInProduction() {
        return reportRepository.findOrdersInProduction().stream()
                .map(p -> new InProductionOrderDto(
                        p.getOrderId(),
                        p.getRegistrationDatetime().toLocalDateTime(),
                        p.getCustomerId(),
                        p.getCustomerName(),
                        p.getProductionId(),
                        p.getStartTime().toLocalDateTime(),
                        p.getTotalOrdersInProduction().longValue()
                ))
                .collect(Collectors.toList());
    }

    // 11. Препараты, требуемые в производстве
    public List<RequiredDrugDto> getRequiredDrugsInProduction() {
        return reportRepository.findRequiredDrugsInProduction().stream()
                .map(p -> new RequiredDrugDto(
                        p.getMedicamentId(),
                        p.getMedicamentName(),
                        p.getTotalRequiredPacks(),
                        p.getTotalRequiredMedications().longValue()
                ))
                .collect(Collectors.toList());
    }

    // 12. Технологии приготовления
    public List<TechnologyInfoDto> getTechnologiesByType(Integer[] typeIds) {
        return reportRepository.findTechnologiesByType(typeIds).stream()
                .map(p -> new TechnologyInfoDto(
                        p.getTechnologyId(),
                        p.getPreparationMethod(),
                        p.getPackageId(),
                        p.getMedicamentName(),
                        p.getMedicamentType(),
                        p.getPrepTimeMinutes(),
                        p.getAmountPerBatch()
                ))
                .collect(Collectors.toList());
    }

    public List<TechnologyInfoDto> getTechnologiesByMedicaments(Integer[] medicamentIds) {
        return reportRepository.findTechnologiesByMedicaments(medicamentIds).stream()
                .map(p -> new TechnologyInfoDto(
                        p.getTechnologyId(),
                        p.getPreparationMethod(),
                        p.getPackageId(),
                        p.getMedicamentName(),
                        p.getMedicamentType(),
                        p.getPrepTimeMinutes(),
                        p.getAmountPerBatch()
                ))
                .collect(Collectors.toList());
    }

    public List<TechnologyInfoDto> getTechnologiesInProduction() {
        return reportRepository.findTechnologiesInProduction().stream()
                .map(p -> new TechnologyInfoDto(
                        p.getTechnologyId(),
                        p.getPreparationMethod(),
                        p.getPackageId(),
                        p.getMedicamentName(),
                        p.getMedicamentType(),
                        p.getPrepTimeMinutes(),
                        p.getAmountPerBatch()
                ))
                .collect(Collectors.toList());
    }
}
