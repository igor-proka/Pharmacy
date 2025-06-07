// src/components/SellerReports.js
import React, { useState } from 'react';
import './SellerReports.css';

const reportCategories = [
  { key: 'clients',      title: 'Клиенты',             icon: '👥' },
  { key: 'medicines',    title: 'Медикаменты',         icon: '💊' },
  { key: 'ordersProd',   title: 'Заказы/Производство', icon: '🏭' },
  { key: 'warehouse',    title: 'Склад',               icon: '📦' }, // можно добавить позже
];

const clientReports = [
  { key: 'notPickedUp',   title: 'Клиенты, не забрали заказ' },
  { key: 'waitingStock',  title: 'Клиенты, ожидающие прибытия' },
  { key: 'orderedByDrug', title: 'Клиенты, заказывавшие лекарство' },
  { key: 'topCustomers',  title: 'Часто делающие заказы' },
];

const medicineReports = [
  { key: 'topUsed',        title: '10 чаще всего используемых' },
  { key: 'usageVolume',    title: 'Объём использованных пачек' },
  { key: 'criticalShort',  title: 'Лекарства на критическом уровне' },
  { key: 'minStock',       title: 'Минимальный запас на складе' },
  { key: 'priceBreakdown', title: 'Цены и компоненты для лекарства' },
];

const orderProdReports = [
  { key: 'inProduction',   title: 'Заказы в производстве' },
  { key: 'requiredDrugs',  title: 'Препараты для текущего производства' },
  { key: 'techByType',     title: 'Технологии по типам' },
  { key: 'techByMeds',     title: 'Технологии по медикаментам' },
  { key: 'techInProd',     title: 'Технологии в производстве' },
];

const warehouseReports = [
  // можно добавить позже
];

export default function SellerReports() {
  const [activeCategory, setActiveCategory] = useState('clients');
  const [activeReport,   setActiveReport]   = useState(null);
  const [reportResult,   setReportResult]   = useState(null);
  const [isLoading,      setIsLoading]      = useState(false);

  // Общие фильтры
  const [typeId,          setTypeId]         = useState('');
  const [startDate,       setStartDate]      = useState('');
  const [endDate,         setEndDate]        = useState('');
  const [medicamentIds,   setMedicamentIds]  = useState('');
  const [typeIds,         setTypeIds]        = useState('');

  // Фильтры для usageVolume
  const [usageStart,      setUsageStart]     = useState('');
  const [usageEnd,        setUsageEnd]       = useState('');
  const [usageMedIds,     setUsageMedIds]    = useState('');

  // Фильтр для priceBreakdown
  const [packageMedicId,  setPackageMedicId] = useState('');

  // Фильтры для technologyByType / technologyByMeds
  const [techTypeIds,     setTechTypeIds]    = useState('');
  const [techMedIds,      setTechMedIds]     = useState('');

  const getReportsList = () => {
    switch (activeCategory) {
      case 'clients':      return clientReports;
      case 'medicines':    return medicineReports;
      case 'ordersProd':   return orderProdReports;
      case 'warehouse':    return warehouseReports;
      default:             return [];
    }
  };

  const fetchReport = async (reportKey) => {
    setReportResult(null);
    setIsLoading(true);

    try {
      // 1. notPickedUp
      if (reportKey === 'notPickedUp') {
        const resp = await fetch('/api/reports/notPickedUp');
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const json = await resp.json();
        setReportResult({
          rows:  json.customers,
          total: json.total,
        });

      // 2. waitingStock
      } else if (reportKey === 'waitingStock') {
        const url = typeId
          ? `/api/reports/waitingStock?typeId=${encodeURIComponent(typeId)}`
          : '/api/reports/waitingStock';
        const resp = await fetch(url);
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const json = await resp.json();
        setReportResult({
          rows:  json.rows,
          total: json.total,
        });

      // 3. orderedByDrug
      } else if (reportKey === 'orderedByDrug') {
        const url = `/api/reports/orderedCustomers`
                  + `?startDate=${encodeURIComponent(startDate)}`
                  + `&endDate=${encodeURIComponent(endDate)}`
                  + `&medicamentIds=${encodeURIComponent(medicamentIds)}`
                  + `&typeIds=${encodeURIComponent(typeIds)}`;
        const resp = await fetch(url);
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const json = await resp.json();
        setReportResult({
          rows:  json.rows,
          total: json.total,
        });

      // 4. topCustomers
      } else if (reportKey === 'topCustomers') {
        let url;
        if (typeId) {
          url = `/api/reports/topCustomers?typeId=${encodeURIComponent(typeId)}`;
        } else {
          url = `/api/reports/topCustomers?medicamentIds=${encodeURIComponent(medicamentIds)}`;
        }
        const resp = await fetch(url);
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const json = await resp.json();
        setReportResult({
          rows:  json.rows,
          total: json.total,
        });

      // 5. topUsed
      } else if (reportKey === 'topUsed') {
        const url = typeId
          ? `/api/reports/topUsedDrugs?typeId=${encodeURIComponent(typeId)}`
          : '/api/reports/topUsedDrugs';
        const resp = await fetch(url);
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const json = await resp.json();
        setReportResult({
          rows:  json.rows,
          total: json.total,
        });

      // 6. usageVolume
      } else if (reportKey === 'usageVolume') {
        const url = `/api/reports/usedVolume`
                  + `?startDate=${encodeURIComponent(usageStart)}`
                  + `&endDate=${encodeURIComponent(usageEnd)}`
                  + `&medicamentIds=${encodeURIComponent(usageMedIds)}`;
        const resp = await fetch(url);
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const json = await resp.json();
        setReportResult({
          rows: json.rows,
        });

      // 7. criticalShort
      } else if (reportKey === 'criticalShort') {
        const resp = await fetch('/api/reports/criticalMedications');
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const json = await resp.json();
        setReportResult({
          rows:  json.rows,
          total: json.total,
        });

      // 8. minStock
      } else if (reportKey === 'minStock') {
        const url = typeId
          ? `/api/reports/minStock?typeId=${encodeURIComponent(typeId)}`
          : '/api/reports/minStock';
        const resp = await fetch(url);
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const json = await resp.json();
        setReportResult({
          rows:  json.rows,
          total: json.total,
        });

      // 9. priceBreakdown → две части: packagePrice и componentCosts
      } else if (reportKey === 'priceBreakdown') {
        // Для простоты: сначала получаем цену пакетов, затем компоненты (можно переключать)
        const pkgUrl = `/api/reports/packagePrice?medicamentId=${encodeURIComponent(packageMedicId)}`;
        const compUrl = `/api/reports/componentCosts?packageId=${encodeURIComponent(packageMedicId)}`;
        const [pkgResp, compResp] = await Promise.all([fetch(pkgUrl), fetch(compUrl)]);
        if (!pkgResp.ok || !compResp.ok) throw new Error('HTTP ошибка');
        const pkgJson = await pkgResp.json();
        const compJson = await compResp.json();
        setReportResult({
          packages: pkgJson.rows,
          components: compJson.rows
        });

      // 10. inProduction
      } else if (reportKey === 'inProduction') {
        const resp = await fetch('/api/reports/inProductionOrders');
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const json = await resp.json();
        setReportResult({
          rows:  json.rows,
          total: json.total,
        });

      // 11. requiredDrugs
      } else if (reportKey === 'requiredDrugs') {
        const resp = await fetch('/api/reports/requiredDrugs');
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const json = await resp.json();
        setReportResult({
          rows:  json.rows,
          total: json.total,
        });

      // 12. techByType / techByMeds / techInProd
      } else if (reportKey === 'techByType') {
        const url = `/api/reports/technologiesByType?typeIds=${encodeURIComponent(techTypeIds)}`;
        const resp = await fetch(url);
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const json = await resp.json();
        setReportResult({ rows: json.rows });

      } else if (reportKey === 'techByMeds') {
        const url = `/api/reports/technologiesByMedicaments?medicamentIds=${encodeURIComponent(techMedIds)}`;
        const resp = await fetch(url);
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const json = await resp.json();
        setReportResult({ rows: json.rows });

      } else if (reportKey === 'techInProd') {
        const resp = await fetch('/api/reports/technologiesInProduction');
        if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
        const json = await resp.json();
        setReportResult({ rows: json.rows });

      } else {
        setReportResult({ error: 'Ещё не реализовано' });
      }
    } catch (err) {
      console.error(err);
      setReportResult({ error: 'Ошибка при загрузке данных' });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="reports-container">
      {/* 1) Секция табов */}
      <div className="reports-tabs">
        {reportCategories.map((cat) => (
          <button
            key={cat.key}
            className={`tab-item ${activeCategory === cat.key ? 'active' : ''}`}
            onClick={() => {
              setActiveCategory(cat.key);
              setActiveReport(null);
              setReportResult(null);
              // сброс всех фильтров
              setTypeId('');
              setStartDate('');
              setEndDate('');
              setMedicamentIds('');
              setTypeIds('');
              setUsageStart('');
              setUsageEnd('');
              setUsageMedIds('');
              setPackageMedicId('');
              setTechTypeIds('');
              setTechMedIds('');
            }}
          >
            <span className="tab-icon">{cat.icon}</span>
            <span className="tab-text">{cat.title}</span>
          </button>
        ))}
      </div>

      {/* 2) Список карточек-отчётов */}
      <div className="reports-list">
        {getReportsList().map((rep) => (
          <button
            key={rep.key}
            className={`report-item ${activeReport === rep.key ? 'selected' : ''}`}
            onClick={() => {
              setActiveReport(rep.key);
              setReportResult(null);
              fetchReport(rep.key);
            }}
          >
            {rep.title}
          </button>
        ))}
      </div>

      {/* 3) Фильтры */}

      {/* waitingStock */}
      {activeReport === 'waitingStock' && (
        <div className="filter-panel">
          <label>
            ID категории:
            <input
              type="number"
              value={typeId}
              onChange={(e) => setTypeId(e.target.value)}
              placeholder="оставьте пустым для всех"
            />
          </label>
          <button className="apply-filter-btn" onClick={() => fetchReport('waitingStock')}>
            Применить
          </button>
        </div>
      )}

      {/* orderedByDrug */}
      {activeReport === 'orderedByDrug' && (
        <div className="filter-panel-ordered">
          <label>
            Начало периода:
            <input
              type="date"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
          </label>
          <label>
            Конец периода:
            <input
              type="date"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </label>
          <label>
            ID лекарств (через запятую):
            <input
              type="text"
              value={medicamentIds}
              onChange={(e) => setMedicamentIds(e.target.value)}
              placeholder="например: 1,2,3"
            />
          </label>
          <label>
            ID типов (через запятую):
            <input
              type="text"
              value={typeIds}
              onChange={(e) => setTypeIds(e.target.value)}
              placeholder="например: 4,5"
            />
          </label>
          <button className="apply-filter-btn" onClick={() => fetchReport('orderedByDrug')}>
            Применить
          </button>
        </div>
      )}

      {/* topCustomers */}
      {activeReport === 'topCustomers' && (
        <div className="filter-panel-top">
          <label>
            ID типа:
            <input
              type="number"
              value={typeId}
              onChange={(e) => setTypeId(e.target.value)}
              placeholder="оставьте пустым для лекарств"
            />
          </label>
          <label>
            ID лекарств (через запятую):
            <input
              type="text"
              value={medicamentIds}
              onChange={(e) => setMedicamentIds(e.target.value)}
              placeholder="например: 1,2,3"
            />
          </label>
          <button className="apply-filter-btn" onClick={() => fetchReport('topCustomers')}>
            Применить
          </button>
        </div>
      )}

      {/* topUsed */}
      {activeReport === 'topUsed' && (
        <div className="filter-panel-topUsed">
          <label>
            ID типа:
            <input
              type="number"
              value={typeId}
              onChange={(e) => setTypeId(e.target.value)}
              placeholder="оставьте пустым для всех"
            />
          </label>
          <button className="apply-filter-btn" onClick={() => fetchReport('topUsed')}>
            Применить
          </button>
        </div>
      )}

      {/* usageVolume */}
      {activeReport === 'usageVolume' && (
        <div className="filter-panel-usage">
          <label>
            Начало:
            <input
              type="date"
              value={usageStart}
              onChange={(e) => setUsageStart(e.target.value)}
            />
          </label>
          <label>
            Конец:
            <input
              type="date"
              value={usageEnd}
              onChange={(e) => setUsageEnd(e.target.value)}
            />
          </label>
          <label>
            ID лекарств (через запятую):
            <input
              type="text"
              value={usageMedIds}
              onChange={(e) => setUsageMedIds(e.target.value)}
              placeholder="например: 1,2,3"
            />
          </label>
          <button className="apply-filter-btn" onClick={() => fetchReport('usageVolume')}>
            Применить
          </button>
        </div>
      )}

      {/* criticalShort */}
      {activeReport === 'criticalShort' && (
        <p>Нажмите «Применить» ниже, чтобы получить список.</p>
      )}

      {/* minStock */}
      {activeReport === 'minStock' && (
        <div className="filter-panel">
          <label>
            ID типа:
            <input
              type="number"
              value={typeId}
              onChange={(e) => setTypeId(e.target.value)}
              placeholder="оставьте пустым для всех"
            />
          </label>
          <button className="apply-filter-btn" onClick={() => fetchReport('minStock')}>
            Применить
          </button>
        </div>
      )}

      {/* priceBreakdown */}
      {activeReport === 'priceBreakdown' && (
        <div className="filter-panel">
          <label>
            ID лекарства:
            <input
              type="number"
              value={packageMedicId}
              onChange={(e) => setPackageMedicId(e.target.value)}
              placeholder="ID лекарства (например: 32)"
            />
          </label>
          <button className="apply-filter-btn" onClick={() => fetchReport('priceBreakdown')}>
            Применить
          </button>
        </div>
      )}

      {/* inProduction */}
      {activeReport === 'inProduction' && (
        <p>Данные загружены автоматически при выборе отчёта.</p>
      )}

      {/* requiredDrugs */}
      {activeReport === 'requiredDrugs' && (
        <p>Данные загружены автоматически при выборе отчёта.</p>
      )}

      {/* techByType */}
      {activeReport === 'techByType' && (
        <div className="filter-panel">
          <label>
            ID типов (через запятую):
            <input
              type="text"
              value={techTypeIds}
              onChange={(e) => setTechTypeIds(e.target.value)}
              placeholder="например: 1,2"
            />
          </label>
          <button className="apply-filter-btn" onClick={() => fetchReport('techByType')}>
            Применить
          </button>
        </div>
      )}

      {/* techByMeds */}
      {activeReport === 'techByMeds' && (
        <div className="filter-panel">
          <label>
            ID лекарств (через запятую):
            <input
              type="text"
              value={techMedIds}
              onChange={(e) => setTechMedIds(e.target.value)}
              placeholder="например: 5,7"
            />
          </label>
          <button className="apply-filter-btn" onClick={() => fetchReport('techByMeds')}>
            Применить
          </button>
        </div>
      )}

      {/* techInProd */}
      {activeReport === 'techInProd' && (
        <p>Данные загружены автоматически при выборе отчёта.</p>
      )}

      {/* 4) Блок вывода результатов */}
      <div className="reports-result">
        {activeReport === null ? (
          <p>Выберите отчёт, чтобы увидеть результат.</p>

        ) : isLoading ? (
          <p>Загрузка данных…</p>

        ) : reportResult && reportResult.error ? (
          <p className="error">{reportResult.error}</p>

        ) : activeReport === 'notPickedUp' && reportResult ? (
          <>
            <h3>Всего «просроченных» клиентов: {reportResult.total}</h3>
            <table className="result-table">
              <thead>
                <tr>
                  <th>Код клиента</th>
                  <th>ФИО</th>
                  <th>Телефон</th>
                  <th>Адрес</th>
                  <th>Код заказа</th>
                  <th>Назначенное время</th>
                </tr>
              </thead>
              <tbody>
                {reportResult.rows.length === 0 ? (
                  <tr>
                    <td colSpan="6" style={{ textAlign: 'center', color: '#64748b' }}>
                      Просроченных клиентов нет
                    </td>
                  </tr>
                ) : (
                  reportResult.rows.map((r) => (
                    <tr key={r.orderId}>
                      <td>{r.customerId}</td>
                      <td>{r.fullName}</td>
                      <td>{r.phoneNumber}</td>
                      <td>{r.address}</td>
                      <td>{r.orderId}</td>
                      <td>{new Date(r.appointedDatetime).toLocaleString('ru-RU')}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </>
        ) : activeReport === 'waitingStock' && reportResult ? (
          <>
            <h3>Всего клиентов, ожидающих прибытия: {reportResult.total}</h3>
            <table className="result-table">
              <thead>
                <tr>
                  <th>Код клиента</th>
                  <th>ФИО</th>
                  <th>Телефон</th>
                  <th>Адрес</th>
                  <th>Код упаковки</th>
                  <th>Наименование медикамента</th>
                  <th>Ожидаемое количество</th>
                </tr>
              </thead>
              <tbody>
                {reportResult.rows.length === 0 ? (
                  <tr>
                    <td colSpan="7" style={{ textAlign: 'center', color: '#64748b' }}>
                      Нет клиентов, ожидающих прибытия
                    </td>
                  </tr>
                ) : (
                  reportResult.rows.map((r, idx) => (
                    <tr key={`${r.customerId}-${idx}`}>
                      <td>{r.customerId}</td>
                      <td>{r.customerName}</td>
                      <td>{r.phoneNumber}</td>
                      <td>{r.address}</td>
                      <td>{r.packageId}</td>
                      <td>{r.medicamentName}</td>
                      <td>{r.waitingAmount}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </>
        ) : activeReport === 'orderedByDrug' && reportResult ? (
          <>
            <h3>Всего клиентов, заказывавших: {reportResult.total}</h3>
            <table className="result-table">
              <thead>
                <tr>
                  <th>Код клиента</th>
                  <th>ФИО</th>
                  <th>Телефон</th>
                  <th>Адрес</th>
                </tr>
              </thead>
              <tbody>
                {reportResult.rows.length === 0 ? (
                  <tr>
                    <td colSpan="4" style={{ textAlign: 'center', color: '#64748b' }}>
                      Нет клиентов за данный период по заданным параметрам
                    </td>
                  </tr>
                ) : (
                  reportResult.rows.map((r) => (
                    <tr key={r.customerId}>
                      <td>{r.customerId}</td>
                      <td>{r.customerName}</td>
                      <td>{r.phoneNumber}</td>
                      <td>{r.address}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </>
        ) : activeReport === 'topCustomers' && reportResult ? (
          <>
            <h3>Всего клиентов, часто делающих заказы: {reportResult.total}</h3>
            <table className="result-table">
              <thead>
                <tr>
                  <th>Код клиента</th>
                  <th>ФИО</th>
                  <th>Телефон</th>
                  <th>Адрес</th>
                  <th>Количество заказов</th>
                </tr>
              </thead>
              <tbody>
                {reportResult.rows.length === 0 ? (
                  <tr>
                    <td colSpan="5" style={{ textAlign: 'center', color: '#64748b' }}>
                      Нет клиентов по указанным параметрам
                    </td>
                  </tr>
                ) : (
                  reportResult.rows.map((r) => (
                    <tr key={r.customerId}>
                      <td>{r.customerId}</td>
                      <td>{r.customerName}</td>
                      <td>{r.phoneNumber}</td>
                      <td>{r.address}</td>
                      <td>{r.ordersCount}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </>
        ) : activeReport === 'topUsed' && reportResult ? (
          <>
            <h3>Топ-10 наиболее часто используемых медикаментов ({reportResult.total})</h3>
            <table className="result-table">
              <thead>
                <tr>
                  <th>Код медикамента</th>
                  <th>Название медикамента</th>
                  <th>Индекс использования</th>
                </tr>
              </thead>
              <tbody>
                {reportResult.rows.length === 0 ? (
                  <tr>
                    <td colSpan="3" style={{ textAlign: 'center', color: '#64748b' }}>
                      Нет данных по использованию
                    </td>
                  </tr>
                ) : (
                  reportResult.rows.map((r) => (
                    <tr key={r.medicamentId}>
                      <td>{r.medicamentId}</td>
                      <td>{r.medicamentName}</td>
                      <td>{r.usageScore}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </>
        ) : activeReport === 'usageVolume' && reportResult ? (
          <>
            <h3>Объём использованных пачек за период</h3>
            <table className="result-table">
              <thead>
                <tr>
                  <th>Код медикамента</th>
                  <th>Название медикамента</th>
                  <th>Общее количество пачек</th>
                </tr>
              </thead>
              <tbody>
                {reportResult.rows.length === 0 ? (
                  <tr>
                    <td colSpan="3" style={{ textAlign: 'center', color: '#64748b' }}>
                      Нет данных за указанный период
                    </td>
                  </tr>
                ) : (
                  reportResult.rows.map((r) => (
                    <tr key={r.medicamentId}>
                      <td>{r.medicamentId}</td>
                      <td>{r.medicamentName}</td>
                      <td>{r.totalPackagesUsed}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </>
        ) : activeReport === 'criticalShort' && reportResult ? (
          <>
            <h3>Лекарства на критическом уровне ({reportResult.total})</h3>
            <table className="result-table">
              <thead>
                <tr>
                  <th>Код медикамента</th>
                  <th>Название</th>
                  <th>Тип</th>
                  <th>Доступно</th>
                  <th>Критический порог</th>
                </tr>
              </thead>
              <tbody>
                {reportResult.rows.length === 0 ? (
                  <tr>
                    <td colSpan="5" style={{ textAlign: 'center', color: '#64748b' }}>
                      Нет медикаментов на критическом уровне
                    </td>
                  </tr>
                ) : (
                  reportResult.rows.map((r) => (
                    <tr key={r.medicamentId}>
                      <td>{r.medicamentId}</td>
                      <td>{r.medicamentName}</td>
                      <td>{r.medicamentType}</td>
                      <td>{r.availableAmount}</td>
                      <td>{r.criticalAmount}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </>
        ) : activeReport === 'minStock' && reportResult ? (
          <>
            <h3>Минимальный запас ({reportResult.total})</h3>
            <table className="result-table">
              <thead>
                <tr>
                  <th>Код медикамента</th>
                  <th>Название</th>
                  <th>Доступно</th>
                  <th>Минимально (общий/в типе)</th>
                  <th>Количество на минимуме</th>
                </tr>
              </thead>
              <tbody>
                {reportResult.rows.length === 0 ? (
                  <tr>
                    <td colSpan="5" style={{ textAlign: 'center', color: '#64748b' }}>
                      Нет данных о минимальном запасе
                    </td>
                  </tr>
                ) : (
                  reportResult.rows.map((r) => (
                    <tr key={r.medicamentId}>
                      <td>{r.medicamentId}</td>
                      <td>{r.medicamentName}</td>
                      <td>{r.availableAmount}</td>
                      {typeId
                        ? <>
                            <td>{r.minimalInType}</td>
                            <td>{r.countAtMinimumInType}</td>
                          </>
                        : <>
                            <td>{r.minimalOverall}</td>
                            <td>{r.countAtMinimum}</td>
                          </>
                      }
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </>
        ) : activeReport === 'priceBreakdown' && reportResult ? (
          <>
            <h3>Цены пакетов и компонентов</h3>
            <div className="price-section">
              <h4>Пакеты:</h4>
              <table className="result-table">
                <thead>
                  <tr>
                    <th>Код упаковки</th>
                    <th>Название медикамента</th>
                    <th>Цена</th>
                  </tr>
                </thead>
                <tbody>
                  {reportResult.packages.length === 0 ? (
                    <tr>
                      <td colSpan="3" style={{ textAlign: 'center', color: '#64748b' }}>
                        Нет данных о ценах пакетов
                      </td>
                    </tr>
                  ) : (
                    reportResult.packages.map((r) => (
                      <tr key={r.packageId}>
                        <td>{r.packageId}</td>
                        <td>{r.medicamentName}</td>
                        <td>{r.price}</td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
              <h4>Компоненты:</h4>
              <table className="result-table">
                <thead>
                  <tr>
                    <th>Код технологии</th>
                    <th>Код компонента</th>
                    <th>Название компонента</th>
                    <th>Количество</th>
                    <th>Цена за ед.</th>
                    <th>Общая стоимость</th>
                    <th>Выпущено пачек</th>
                    <th>Стоимость 1 пачки</th>
                  </tr>
                </thead>
                <tbody>
                  {reportResult.components.length === 0 ? (
                    <tr>
                      <td colSpan="8" style={{ textAlign: 'center', color: '#64748b' }}>
                        Нет данных о компонентах
                      </td>
                    </tr>
                  ) : (
                    reportResult.components.map((r) => (
                      <tr key={`${r.technologyId}-${r.componentPackageId}`}>
                        <td>{r.technologyId}</td>
                        <td>{r.componentPackageId}</td>
                        <td>{r.componentMedicamentName}</td>
                        <td>{r.componentAmount}</td>
                        <td>{r.componentPrice}</td>
                        <td>{r.componentTotalCost}</td>
                        <td>{r.producedPackages}</td>
                        <td>{r.costPerOnePackage}</td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </>
        ) : activeReport === 'inProduction' && reportResult ? (
          <>
            <h3>Заказы в производстве: {reportResult.total}</h3>
            <table className="result-table">
              <thead>
                <tr>
                  <th>Код заказа</th>
                  <th>Дата регистрации</th>
                  <th>Код клиента</th>
                  <th>Имя клиента</th>
                  <th>Код производства</th>
                  <th>Начало производства</th>
                </tr>
              </thead>
              <tbody>
                {reportResult.rows.length === 0 ? (
                  <tr>
                    <td colSpan="6" style={{ textAlign: 'center', color: '#64748b' }}>
                      Нет заказов в производстве
                    </td>
                  </tr>
                ) : (
                  reportResult.rows.map((r) => (
                    <tr key={r.productionId}>
                      <td>{r.orderId}</td>
                      <td>{new Date(r.registrationDatetime).toLocaleString('ru-RU')}</td>
                      <td>{r.customerId}</td>
                      <td>{r.customerName}</td>
                      <td>{r.productionId}</td>
                      <td>{new Date(r.startTime).toLocaleString('ru-RU')}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </>
        ) : activeReport === 'requiredDrugs' && reportResult ? (
          <>
            <h3>Препараты, требуемые для производства: {reportResult.total}</h3>
            <table className="result-table">
              <thead>
                <tr>
                  <th>Код медикамента</th>
                  <th>Название</th>
                  <th>Требуемое количество пачек</th>
                </tr>
              </thead>
              <tbody>
                {reportResult.rows.length === 0 ? (
                  <tr>
                    <td colSpan="3" style={{ textAlign: 'center', color: '#64748b' }}>
                      Нет требуемых препаратов
                    </td>
                  </tr>
                ) : (
                  reportResult.rows.map((r) => (
                    <tr key={r.medicamentId}>
                      <td>{r.medicamentId}</td>
                      <td>{r.medicamentName}</td>
                      <td>{r.totalRequiredPacks}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </>
        ) : activeReport === 'techByType' && reportResult ? (
          <>
            <h3>Технологии по типам лекарств</h3>
            <table className="result-table">
              <thead>
                <tr>
                  <th>Код технологии</th>
                  <th>Метод приготовления</th>
                  <th>Код упаковки</th>
                  <th>Название медикамента</th>
                  <th>Тип медикамента</th>
                  <th>Время (мин)</th>
                  <th>Кол-во за раз</th>
                </tr>
              </thead>
              <tbody>
                {reportResult.rows.length === 0 ? (
                  <tr>
                    <td colSpan="7" style={{ textAlign: 'center', color: '#64748b' }}>
                      Нет данных о технологиях по заданным типам
                    </td>
                  </tr>
                ) : (
                  reportResult.rows.map((r) => (
                    <tr key={r.technologyId}>
                      <td>{r.technologyId}</td>
                      <td>{r.preparationMethod}</td>
                      <td>{r.packageId}</td>
                      <td>{r.medicamentName}</td>
                      <td>{r.medicamentType}</td>
                      <td>{r.prepTimeMinutes}</td>
                      <td>{r.amountPerBatch}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </>
        ) : activeReport === 'techByMeds' && reportResult ? (
          <>
            <h3>Технологии по конкретным лекарствам</h3>
            <table className="result-table">
              <thead>
                <tr>
                  <th>Код технологии</th>
                  <th>Метод приготовления</th>
                  <th>Код упаковки</th>
                  <th>Название медикамента</th>
                  <th>Тип медикамента</th>
                  <th>Время (мин)</th>
                  <th>Кол-во за раз</th>
                </tr>
              </thead>
              <tbody>
                {reportResult.rows.length === 0 ? (
                  <tr>
                    <td colSpan="7" style={{ textAlign: 'center', color: '#64748b' }}>
                      Нет данных о технологиях по указанным медикаментам
                    </td>
                  </tr>
                ) : (
                  reportResult.rows.map((r) => (
                    <tr key={r.technologyId}>
                      <td>{r.technologyId}</td>
                      <td>{r.preparationMethod}</td>
                      <td>{r.packageId}</td>
                      <td>{r.medicamentName}</td>
                      <td>{r.medicamentType}</td>
                      <td>{r.prepTimeMinutes}</td>
                      <td>{r.amountPerBatch}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </>
        ) : activeReport === 'techInProd' && reportResult ? (
          <>
            <h3>Технологии в текущем производстве</h3>
            <table className="result-table">
              <thead>
                <tr>
                  <th>Код технологии</th>
                  <th>Метод приготовления</th>
                  <th>Код упаковки</th>
                  <th>Название медикамента</th>
                  <th>Тип медикамента</th>
                  <th>Время (мин)</th>
                  <th>Кол-во за раз</th>
                </tr>
              </thead>
              <tbody>
                {reportResult.rows.length === 0 ? (
                  <tr>
                    <td colSpan="7" style={{ textAlign: 'center', color: '#64748b' }}>
                      Нет данных о технологиях в производстве
                    </td>
                  </tr>
                ) : (
                  reportResult.rows.map((r) => (
                    <tr key={r.technologyId}>
                      <td>{r.technologyId}</td>
                      <td>{r.preparationMethod}</td>
                      <td>{r.packageId}</td>
                      <td>{r.medicamentName}</td>
                      <td>{r.medicamentType}</td>
                      <td>{r.prepTimeMinutes}</td>
                      <td>{r.amountPerBatch}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </>
        ) : null}
      </div>
    </div>
  );
}
