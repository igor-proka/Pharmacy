// src/main/java/ru/nsu/prokofiev/pharmacy/repository/ReportRepository.java
package ru.nsu.prokofiev.pharmacy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.nsu.prokofiev.pharmacy.model.Orders;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Orders, Long> {

    // 1. Просроченные клиенты
    @Query(value = """
        SELECT
          c.id               AS customer_id,
          c.full_name        AS full_name,
          c.phone_number     AS phone_number,
          c.address          AS address,
          o.id               AS order_id,
          o.appointed_datetime AS appointed_datetime
        FROM customers c
        JOIN orders o ON o.customer_id = c.id
        WHERE
          o.obtaining_datetime IS NULL
          AND o.appointed_datetime < NOW()
        ORDER BY o.appointed_datetime
        """, nativeQuery = true)
    List<MissedCustomerProjection> findMissedCustomers();

    @Query(value = """
        SELECT
          COUNT(DISTINCT c.id) AS total_missed_customers
        FROM customers c
        JOIN orders o ON o.customer_id = c.id
        WHERE
          o.obtaining_datetime IS NULL
          AND o.appointed_datetime < NOW()
        """, nativeQuery = true)
    Long countMissedCustomers();

    // 2. Ожидающие поступления (общий и по типу)
    @Query(value = """
        WITH waiting_details AS (
          SELECT
            c.id               AS customer_id,
            c.full_name        AS customer_name,
            c.phone_number     AS phone_number,
            c.address          AS address,
            wm.medicament_id   AS package_id,
            m.name             AS medicament_name,
            wm.amount          AS waiting_amount
          FROM customers c
          JOIN orders o
            ON o.customer_id = c.id
          JOIN waiting_medicaments wm
            ON wm.order_id = o.id
          JOIN medicament_packages mp
            ON mp.id = wm.medicament_id
          JOIN medicaments m
            ON m.id = mp.medicament_id
        )
        SELECT
          wd.customer_id               AS customer_id,
          wd.customer_name             AS customer_name,
          wd.phone_number              AS phone_number,
          wd.address                   AS address,
          wd.package_id                AS package_id,
          wd.medicament_name           AS medicament_name,
          wd.waiting_amount            AS waiting_amount,
          (SELECT COUNT(DISTINCT customer_id) FROM waiting_details) AS total_waiting_customers
        FROM waiting_details wd
        ORDER BY wd.customer_name, wd.medicament_name
        """, nativeQuery = true)
    List<WaitingCustomerProjection> findWaitingCustomers();

    @Query(value = """
        WITH waiting_by_type AS (
          SELECT
            c.id               AS customer_id,
            c.full_name        AS customer_name,
            c.phone_number     AS phone_number,
            c.address          AS address,
            wm.medicament_id   AS package_id,
            m.name             AS medicament_name,
            wm.amount          AS waiting_amount
          FROM customers c
          JOIN orders o
            ON o.customer_id = c.id
          JOIN waiting_medicaments wm
            ON wm.order_id = o.id
          JOIN medicament_packages mp
            ON mp.id = wm.medicament_id
          JOIN medicaments m
            ON m.id = mp.medicament_id
          WHERE m.type_id = :typeId
        )
        SELECT
          wbt.customer_id               AS customer_id,
          wbt.customer_name             AS customer_name,
          wbt.phone_number              AS phone_number,
          wbt.address                   AS address,
          wbt.package_id                AS package_id,
          wbt.medicament_name           AS medicament_name,
          wbt.waiting_amount            AS waiting_amount,
          (SELECT COUNT(DISTINCT customer_id) FROM waiting_by_type) AS total_waiting_customers
        FROM waiting_by_type wbt
        ORDER BY wbt.customer_name, wbt.medicament_name
        """, nativeQuery = true)
    List<WaitingCustomerProjection> findWaitingCustomersByType(@Param("typeId") Integer typeId);

    // 3. Клиенты, заказывавшие за период (по лекарствам/типам)
    @Query(value = """
        WITH params AS (
          SELECT
            CAST(:startDate AS TIMESTAMP)    AS start_date,
            CAST(:endDate   AS TIMESTAMP)    AS end_date,
            CAST(:medicamentIds AS INT[])     AS medicament_ids,
            CAST(:typeIds       AS INT[])     AS type_ids
        ),
        relevant_orders AS (
          SELECT DISTINCT
            o.id            AS order_id,
            o.customer_id
          FROM orders o
          JOIN reserved_medicaments rm
            ON rm.order_id = o.id
          JOIN storage s
            ON s.id = rm.storage_id
          JOIN medicament_packages mp
            ON mp.id = s.medicament_id
          JOIN medicaments m
            ON m.id = mp.medicament_id
          JOIN params p
            ON TRUE
          WHERE
            o.registration_datetime BETWEEN p.start_date AND p.end_date
            AND (
                 mp.medicament_id = ANY(p.medicament_ids)
              OR m.type_id       = ANY(p.type_ids)
            )
        ),
        customers_list AS (
          SELECT DISTINCT
            c.id           AS customer_id,
            c.full_name    AS customer_name,
            c.phone_number,
            c.address
          FROM customers c
          JOIN relevant_orders ro
            ON ro.customer_id = c.id
        )
        SELECT
          cl.customer_id             AS customer_id,
          cl.customer_name           AS customer_name,
          cl.phone_number            AS phone_number,
          cl.address                 AS address,
          (SELECT COUNT(*) FROM customers_list) AS total_customers
        FROM customers_list cl
        ORDER BY cl.customer_name
        """, nativeQuery = true)
    List<CustomersOrderedProjection> findCustomersOrdered(
            @Param("startDate") String startDate,
            @Param("endDate")   String endDate,
            @Param("medicamentIds") Integer[] medicamentIds,
            @Param("typeIds")       Integer[] typeIds
    );

    // 4. Часто делающие заказы (по типу / по лекарствам)
    @Query(value = """
        WITH params AS (
          SELECT CAST(:typeId AS INT) AS type_id
        ),
        customer_orders AS (
          SELECT
            c.id                  AS customer_id,
            c.full_name           AS customer_name,
            c.phone_number        AS phone_number,
            c.address             AS address,
            COUNT(DISTINCT o.id)  AS orders_count
          FROM customers c
          JOIN orders o
            ON o.customer_id = c.id
          JOIN reserved_medicaments rm
            ON rm.order_id = o.id
          JOIN storage s
            ON s.id = rm.storage_id
          JOIN medicament_packages mp
            ON mp.id = s.medicament_id
          JOIN medicaments m
            ON m.id = mp.medicament_id
          JOIN params p
            ON m.type_id = p.type_id
          WHERE o.obtaining_datetime IS NOT NULL
          GROUP BY c.id, c.full_name, c.phone_number, c.address
        ),
        summary AS (
          SELECT COUNT(*) AS total_customers
          FROM customer_orders
        )
        SELECT
          co.customer_id,
          co.customer_name,
          co.phone_number,
          co.address,
          co.orders_count,
          s.total_customers
        FROM customer_orders co
        CROSS JOIN summary s
        ORDER BY co.orders_count DESC
        """, nativeQuery = true)
    List<FrequentCustomerProjection> findTopCustomersByType(@Param("typeId") Integer typeId);

    @Query(value = """
        WITH params AS (
          SELECT CAST(:medicamentIds AS INT[]) AS medicament_ids
        ),
        customer_orders AS (
          SELECT
            c.id                  AS customer_id,
            c.full_name           AS customer_name,
            c.phone_number        AS phone_number,
            c.address             AS address,
            COUNT(DISTINCT o.id)  AS orders_count
          FROM customers c
          JOIN orders o
            ON o.customer_id = c.id
          JOIN reserved_medicaments rm
            ON rm.order_id = o.id
          JOIN storage s
            ON s.id = rm.storage_id
          JOIN medicament_packages mp
            ON mp.id = s.medicament_id
          JOIN medicaments m
            ON m.id = mp.medicament_id
          JOIN params p
            ON m.id = ANY(p.medicament_ids)
          WHERE o.obtaining_datetime IS NOT NULL
          GROUP BY c.id, c.full_name, c.phone_number, c.address
        ),
        summary AS (
          SELECT COUNT(*) AS total_customers
          FROM customer_orders
        )
        SELECT
          co.customer_id,
          co.customer_name,
          co.phone_number,
          co.address,
          co.orders_count,
          s.total_customers
        FROM customer_orders co
        CROSS JOIN summary s
        ORDER BY co.orders_count DESC
        """, nativeQuery = true)
    List<FrequentCustomerProjection> findTopCustomersByMedicaments(@Param("medicamentIds") Integer[] medicamentIds);

    // 5. Топ-10 часто используемых медикаментов (без/с фильтром по типу)
    @Query(value = """
        WITH 
            used_in_cooking_drugs AS (
                SELECT
                    tc.component_id                                AS medicament_id,
                    SUM(tc.component_amount * p.medicament_amount) AS drug_amount
                FROM production p
                JOIN technology_components tc
                  ON p.technology_id = tc.technology_id
                WHERE p.start_time IS NOT NULL
                GROUP BY tc.component_id
            ),
            sold_drugs AS (
                SELECT
                    mp.medicament_id       AS medicament_id,
                    SUM(rm.package_count)  AS drug_amount
                FROM reserved_medicaments rm
                JOIN orders o
                  ON rm.order_id = o.id
                JOIN storage s
                  ON rm.storage_id = s.id
                JOIN medicament_packages mp
                  ON s.medicament_id = mp.id
                WHERE o.obtaining_datetime IS NOT NULL
                GROUP BY mp.medicament_id
            ),
            used_drugs AS (
                SELECT
                    medicament_id,
                    SUM(drug_amount) AS total_drug_amount
                FROM (
                    SELECT medicament_id, drug_amount FROM used_in_cooking_drugs
                    UNION ALL
                    SELECT medicament_id, drug_amount FROM sold_drugs
                ) AS combined
                GROUP BY medicament_id
            )
        SELECT
            ud.medicament_id               AS medicament_id,
            m.name                         AS medicament_name,
            ud.total_drug_amount           AS usage_score
        FROM used_drugs ud
        JOIN medicaments m
          ON m.id = ud.medicament_id
        ORDER BY ud.total_drug_amount DESC
        LIMIT 10
        """, nativeQuery = true)
    List<TopUsedDrugProjection> findTopUsedDrugs();

    @Query(value = """
        WITH 
            used_in_cooking_drugs AS (
                SELECT
                    tc.component_id                                AS medicament_id,
                    SUM(tc.component_amount * p.medicament_amount) AS drug_amount
                FROM production p
                JOIN technology_components tc
                  ON p.technology_id = tc.technology_id
                JOIN medicaments m_c
                  ON m_c.id = tc.component_id
                WHERE p.start_time IS NOT NULL
                  AND m_c.type_id = :typeId
                GROUP BY tc.component_id
            ),
            sold_drugs AS (
                SELECT
                    mp.medicament_id       AS medicament_id,
                    SUM(rm.package_count)  AS drug_amount
                FROM reserved_medicaments rm
                JOIN orders o
                  ON rm.order_id = o.id
                JOIN storage s
                  ON rm.storage_id = s.id
                JOIN medicament_packages mp
                  ON s.medicament_id = mp.id
                JOIN medicaments m_s
                  ON m_s.id = mp.medicament_id
                WHERE o.obtaining_datetime IS NOT NULL
                  AND m_s.type_id = :typeId
                GROUP BY mp.medicament_id
            ),
            used_drugs AS (
                SELECT
                    medicament_id,
                    SUM(drug_amount) AS total_drug_amount
                FROM (
                    SELECT medicament_id, drug_amount FROM used_in_cooking_drugs
                    UNION ALL
                    SELECT medicament_id, drug_amount FROM sold_drugs
                ) AS combined
                GROUP BY medicament_id
            )
        SELECT
            ud.medicament_id               AS medicament_id,
            m.name                         AS medicament_name,
            ud.total_drug_amount           AS usage_score
        FROM used_drugs ud
        JOIN medicaments m
          ON m.id = ud.medicament_id
        ORDER BY ud.total_drug_amount DESC
        LIMIT 10
        """, nativeQuery = true)
    List<TopUsedDrugProjection> findTopUsedDrugsByType(@Param("typeId") Integer typeId);

    // 6. Объём использованных пачек за период
    @Query(value = """
        WITH
            params AS (
                SELECT
                    CAST(:startDate AS TIMESTAMP)     AS start_date,
                    CAST(:endDate   AS TIMESTAMP)     AS end_date,
                    CAST(:medicamentIds AS INT[])     AS medicament_ids
            ),
            used_in_cooking AS (
                SELECT
                    tc.component_id                                AS medicament_id,
                    SUM(tc.component_amount * p.medicament_amount) AS packages_used
                FROM production p
                JOIN params par            ON TRUE
                JOIN technology_components tc
                  ON p.technology_id = tc.technology_id
                WHERE
                    p.start_time IS NOT NULL
                    AND p.end_time BETWEEN par.start_date AND par.end_date
                    AND tc.component_id = ANY(par.medicament_ids)
                GROUP BY tc.component_id
            ),
            sold_drugs AS (
                SELECT
                    mp.medicament_id      AS medicament_id,
                    SUM(rm.package_count) AS packages_used
                FROM reserved_medicaments rm
                JOIN params par         ON TRUE
                JOIN orders o
                  ON rm.order_id = o.id
                JOIN storage s
                  ON rm.storage_id = s.id
                JOIN medicament_packages mp
                  ON s.medicament_id = mp.id
                WHERE
                    o.obtaining_datetime BETWEEN par.start_date AND par.end_date
                    AND mp.medicament_id = ANY(par.medicament_ids)
                GROUP BY mp.medicament_id
            ),
            used_drugs AS (
                SELECT
                    medicament_id,
                    SUM(packages_used) AS total_packages_used
                FROM (
                    SELECT * FROM used_in_cooking
                    UNION ALL
                    SELECT * FROM sold_drugs
                ) AS combined
                GROUP BY medicament_id
            )
        SELECT
            ud.medicament_id               AS medicament_id,
            m.name                         AS medicament_name,
            ud.total_packages_used         AS total_packages_used
        FROM used_drugs ud
        JOIN medicaments m
          ON m.id = ud.medicament_id
        ORDER BY ud.total_packages_used DESC
        """, nativeQuery = true)
    List<UsedVolumeProjection> findUsedVolume(
            @Param("startDate") String startDate,
            @Param("endDate")   String endDate,
            @Param("medicamentIds") Integer[] medicamentIds
    );

    // 7. Лекарства на критическом уровне или закончились
    @Query(value = """
        WITH critical AS (
          SELECT DISTINCT
            m.id               AS medicament_id,
            m.name             AS medicament_name,
            mt.name            AS medicament_type,
            s.available_amount AS available_amount,
            s.critical_amount  AS critical_amount
          FROM storage s
          JOIN medicament_packages mp
            ON s.medicament_id = mp.id
          JOIN medicaments m
            ON mp.medicament_id = m.id
          JOIN medicament_types mt
            ON m.type_id = mt.id
          WHERE s.available_amount <= s.critical_amount
        )
        SELECT
          c.medicament_id            AS medicament_id,
          c.medicament_name          AS medicament_name,
          c.medicament_type          AS medicament_type,
          c.available_amount         AS available_amount,
          c.critical_amount          AS critical_amount,
          (SELECT COUNT(*) FROM critical) AS total_flagged_medications
        FROM critical c
        ORDER BY c.medicament_name
        """, nativeQuery = true)
    List<CriticalMedicationProjection> findCriticalMedications();

    // 8. Лекарства с минимальным запасом (общий)
    @Query(value = """
        WITH 
            total_stock AS (
              SELECT
                m.id                    AS medicament_id,
                m.name                  AS medicament_name,
                SUM(s.available_amount) AS total_available
              FROM storage s
              JOIN medicament_packages mp
                ON s.medicament_id = mp.id
              JOIN medicaments m
                ON mp.medicament_id = m.id
              GROUP BY m.id, m.name
            ),
            min_stock AS (
              SELECT MIN(total_available) AS minimal_available
              FROM total_stock
            )
        SELECT
          ts.medicament_id             AS medicament_id,
          ts.medicament_name           AS medicament_name,
          ts.total_available           AS available_amount,
          ms.minimal_available         AS minimal_overall,
          (SELECT COUNT(*) FROM total_stock WHERE total_available = ms.minimal_available) AS count_at_minimum
        FROM total_stock ts
        CROSS JOIN min_stock ms
        WHERE ts.total_available = ms.minimal_available
        ORDER BY ts.medicament_name
        """, nativeQuery = true)
    List<MinStockProjection> findMinStock();

    // 8. Лекарства с минимальным запасом (по типу)
    @Query(value = """
        WITH 
            total_stock_by_type AS (
              SELECT
                m.id                    AS medicament_id,
                m.name                  AS medicament_name,
                SUM(s.available_amount) AS total_available
              FROM storage s
              JOIN medicament_packages mp
                ON s.medicament_id = mp.id
              JOIN medicaments m
                ON mp.medicament_id = m.id
              WHERE m.type_id = :typeId
              GROUP BY m.id, m.name
            ),
            min_stock_by_type AS (
              SELECT MIN(total_available) AS minimal_available
              FROM total_stock_by_type
            )
        SELECT
          ts.medicament_id               AS medicament_id,
          ts.medicament_name             AS medicament_name,
          ts.total_available             AS available_amount,
          mst.minimal_available          AS minimal_in_type,
          (SELECT COUNT(*) 
           FROM total_stock_by_type 
           WHERE total_available = mst.minimal_available
          ) AS count_at_minimum_in_type
        FROM total_stock_by_type ts
        CROSS JOIN min_stock_by_type mst
        WHERE ts.total_available = mst.minimal_available
        ORDER BY ts.medicament_name
        """, nativeQuery = true)
    List<MinStockByTypeProjection> findMinStockByType(@Param("typeId") Integer typeId);

    // 9. Цена готового пакета
    @Query(value = """
        SELECT 
          mp.id            AS package_id,
          m.name           AS medicament_name,
          mp.price         AS price
        FROM medicament_packages mp
        JOIN medicaments m 
          ON mp.medicament_id = m.id
        WHERE m.id = :medicamentId
        """, nativeQuery = true)
    List<PreparedPackageProjection> findPreparedPackages(@Param("medicamentId") Long medicamentId);

    // 9. Стоимость компонентов для упаковок
    @Query(value = """
        SELECT 
          tc.technology_id                            AS technology_id,
          mp_comp.id                                  AS component_package_id,
          m_comp.name                                 AS component_medicament_name,
          tc.component_amount                         AS component_amount,
          mp_comp.price                               AS component_price,
          (tc.component_amount * mp_comp.price)       AS component_total_cost,
          t.amount                                    AS produced_packages,
          ROUND(
            SUM(tc.component_amount * mp_comp.price) 
            OVER (PARTITION BY tc.technology_id) 
            / t.amount, 2
          )                                           AS cost_per_one_package
        FROM technologies t
        JOIN technology_components tc 
          ON t.id = tc.technology_id
        JOIN medicament_packages mp_comp 
          ON tc.component_id = mp_comp.id
        JOIN medicaments m_comp 
          ON mp_comp.medicament_id = m_comp.id
        WHERE t.medicament_id = :packageId
        """, nativeQuery = true)
    List<ComponentCostProjection> findComponentCosts(@Param("packageId") Long packageId);

    // 10. Заказы в производстве
    @Query(value = """
        WITH in_production AS (
          SELECT
            pr.order_id,
            o.registration_datetime,
            o.customer_id,
            c.full_name           AS customer_name,
            pr.id                  AS production_id,
            pr.start_time
          FROM production pr
          JOIN orders o 
            ON pr.order_id = o.id
          JOIN customers c 
            ON o.customer_id = c.id
          WHERE
            pr.start_time IS NOT NULL
            AND pr.end_time   IS NULL
        )
        SELECT
          ip.order_id                         AS order_id,
          ip.registration_datetime            AS registration_datetime,
          ip.customer_id                      AS customer_id,
          ip.customer_name                    AS customer_name,
          ip.production_id                    AS production_id,
          ip.start_time                       AS start_time,
          (SELECT COUNT(DISTINCT order_id) FROM in_production) AS total_orders_in_production
        FROM in_production ip
        ORDER BY ip.start_time
        """, nativeQuery = true)
    List<InProductionOrderProjection> findOrdersInProduction();

    // 11. Препараты, требуемые для заказов в производстве
    @Query(value = """
        WITH
          in_production AS (
            SELECT
              pr.id               AS production_id,
              pr.technology_id,
              pr.medicament_amount
            FROM production pr
            WHERE pr.start_time IS NOT NULL
              AND pr.end_time   IS NULL
          ),
          required_drugs AS (
            SELECT
              tc.component_id                                              AS medicament_id,
              m.name                                                       AS medicament_name,
              SUM(tc.component_amount * ip.medicament_amount)              AS total_required_packs
            FROM in_production ip
            JOIN technology_components tc 
              ON ip.technology_id = tc.technology_id
            JOIN medicaments m 
              ON m.id = tc.component_id
            GROUP BY tc.component_id, m.name
          )
        SELECT
          rd.medicament_id                     AS medicament_id,
          rd.medicament_name                   AS medicament_name,
          rd.total_required_packs              AS total_required_packs,
          (SELECT COUNT(*) FROM required_drugs) AS total_required_medications
        FROM required_drugs rd
        ORDER BY rd.medicament_name
        """, nativeQuery = true)
    List<RequiredDrugProjection> findRequiredDrugsInProduction();

    // 12. Технологии приготовления (по типам)
    @Query(value = """
        WITH params AS (
          SELECT CAST(:typeIds AS INT[]) AS type_ids
        )
        SELECT
          t.id                        AS technology_id,
          pm.name                     AS preparation_method,
          mp.id                       AS package_id,
          m.name                      AS medicament_name,
          mt.name                     AS medicament_type,
          t.preparation_time          AS prep_time_minutes,
          t.amount                    AS amount_per_batch
        FROM params p
        JOIN technologies t 
          ON TRUE
        JOIN medicament_packages mp 
          ON t.medicament_id = mp.id
        JOIN medicaments m 
          ON mp.medicament_id = m.id
        JOIN medicament_types mt 
          ON m.type_id = mt.id
        JOIN preparation_methods pm 
          ON t.preparation_method_id = pm.id
        WHERE m.type_id = ANY(p.type_ids)
        ORDER BY t.id
        """, nativeQuery = true)
    List<TechnologyInfoProjection> findTechnologiesByType(@Param("typeIds") Integer[] typeIds);

    // 12. Технологии приготовления (по лекарствам)
    @Query(value = """
        WITH params AS (
          SELECT CAST(:medicamentIds AS INT[]) AS medicament_ids
        )
        SELECT
          t.id                        AS technology_id,
          pm.name                     AS preparation_method,
          mp.id                       AS package_id,
          m.name                      AS medicament_name,
          mt.name                     AS medicament_type,
          t.preparation_time          AS prep_time_minutes,
          t.amount                    AS amount_per_batch
        FROM params p
        JOIN technologies t 
          ON TRUE
        JOIN medicament_packages mp 
          ON t.medicament_id = mp.id
        JOIN medicaments m 
          ON mp.medicament_id = m.id
        JOIN medicament_types mt 
          ON m.type_id = mt.id
        JOIN preparation_methods pm 
          ON t.preparation_method_id = pm.id
        WHERE mp.medicament_id = ANY(p.medicament_ids)
        ORDER BY t.id
        """, nativeQuery = true)
    List<TechnologyInfoProjection> findTechnologiesByMedicaments(@Param("medicamentIds") Integer[] medicamentIds);

    // 12. Технологии приготовления (для заказов в производстве)
    @Query(value = """
        SELECT
          t.id                        AS technology_id,
          pm.name                     AS preparation_method,
          mp.id                       AS package_id,
          m.name                      AS medicament_name,
          mt.name                     AS medicament_type,
          t.preparation_time          AS prep_time_minutes,
          t.amount                    AS amount_per_batch
        FROM production pr
        JOIN technologies t 
          ON pr.technology_id = t.id
        JOIN preparation_methods pm 
          ON t.preparation_method_id = pm.id
        JOIN medicament_packages mp 
          ON t.medicament_id = mp.id
        JOIN medicaments m 
          ON mp.medicament_id = m.id
        JOIN medicament_types mt 
          ON m.type_id = mt.id
        WHERE pr.start_time IS NOT NULL
          AND pr.end_time   IS NULL
        GROUP BY
          t.id, pm.name, mp.id, m.name, mt.name, t.preparation_time, t.amount
        ORDER BY t.id
        """, nativeQuery = true)
    List<TechnologyInfoProjection> findTechnologiesInProduction();
}
