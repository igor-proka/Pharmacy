// src/components/SuppliesSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function SuppliesSection() {
  const [suppliesData, setSuppliesData] = useState([]);
  const [packages, setPackages] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [filterMedId, setFilterMedId] = useState('');
  const [form, setForm] = useState({
    medicamentPackageId: '',
    medicamentAmount: '',
    cost: '',
    assignedDatetime: '',
    deliveryDatetime: '',
    supplierId: '',
  });
  const [editingItem, setEditingItem] = useState(null);
  const [loading, setLoading] = useState(true);
  const formRef = useRef(null);

  // Загрузка данных: поставки + пачки + поставщики
  useEffect(() => {
    setLoading(true);
    Promise.all([
      fetch('/api/supplies').then(r => r.json()),
      fetch('/api/medicament-packages').then(r => r.json()),
      fetch('/api/suppliers').then(r => r.json()),
    ])
      .then(([suppliesList, pkgList, supList]) => {
        setSuppliesData(suppliesList);
        setPackages(pkgList);
        setSuppliers(supList);
      })
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setLoading(false));
  }, []);

  const resetForm = () => {
    setForm({
      medicamentPackageId: '',
      medicamentAmount: '',
      cost: '',
      assignedDatetime: '',
      deliveryDatetime: '',
      supplierId: '',
    });
    setEditingItem(null);
  };

  const handleFormChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    const {
      medicamentPackageId,
      medicamentAmount,
      cost,
      assignedDatetime,
      deliveryDatetime,
      supplierId,
    } = form;

    if (
      !medicamentPackageId ||
      !medicamentAmount ||
      !cost ||
      !assignedDatetime ||
      !supplierId
    ) {
      alert('Заполните все обязательные поля');
      return;
    }

    const pkg = packages.find(p => p.id === Number(medicamentPackageId));
    const sup = suppliers.find(s => s.id === Number(supplierId));
    if (!pkg || !sup) {
      alert('Выберите корректные пачку и поставщика');
      return;
    }

    const payload = {
      medicament: pkg,
      medicamentAmount: parseInt(medicamentAmount, 10),
      cost: parseInt(cost, 10),
      assignedDatetime,
      deliveryDatetime: deliveryDatetime || null,
      supplier: sup,
    };

    const method = editingItem ? 'PUT' : 'POST';
    const url = editingItem
      ? `/api/supplies/${editingItem.id}`
      : '/api/supplies';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(() => fetch('/api/supplies').then(r => r.json()))
      .then(all => {
        setSuppliesData(all);
        resetForm();
      })
      .catch(() => alert('Ошибка при сохранении'));
  };

  const handleEdit = item => {
    setEditingItem(item);
    setForm({
      medicamentPackageId: item.medicament.id.toString(),
      medicamentAmount: item.medicamentAmount.toString(),
      cost: item.cost.toString(),
      assignedDatetime: item.assignedDatetime.replace('Z', ''),
      deliveryDatetime: item.deliveryDatetime ? item.deliveryDatetime.replace('Z', '') : '',
      supplierId: item.supplier.id.toString(),
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  const handleDelete = item => {
    if (!window.confirm('Удалить запись о поставке?')) return;
    fetch(`/api/supplies/${item.id}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        return fetch('/api/supplies');
      })
      .then(r => r.json())
      .then(all => setSuppliesData(all))
      .catch(() => alert('Ошибка при удалении'));
  };

  // Фильтр по medicament (берём id медикамента из medicamentPackage.medicament.id)
  const filtered = filterMedId
    ? suppliesData.filter(
        s => s.medicament.medicament.id === Number(filterMedId)
      )
    : suppliesData;

  // Уникальный список медикаментов для фильтра
  const uniqueMedMap = {};
  packages.forEach(p => {
    const med = p.medicament;
    uniqueMedMap[med.id] = med.name;
  });
  const uniqueMedList = Object.entries(uniqueMedMap).map(([id, label]) => ({
    id: Number(id),
    label,
  }));

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Поставки лекарств</h2>

      <div className="form-container" ref={formRef}>
        <select
          className="form-input"
          name="medicamentPackageId"
          value={form.medicamentPackageId}
          onChange={handleFormChange}
        >
          <option value="">Пачка (медикамент — цена)</option>
          {packages.map(p => (
            <option key={p.id} value={p.id}>
              {p.medicament.name} — {p.price}₽
            </option>
          ))}
        </select>

        <input
          type="number"
          name="medicamentAmount"
          className="form-input"
          placeholder="Кол-во медикаментов"
          value={form.medicamentAmount}
          onChange={handleFormChange}
        />

        <input
          type="number"
          name="cost"
          className="form-input"
          placeholder="Стоимость"
          value={form.cost}
          onChange={handleFormChange}
        />

        <input
          type="datetime-local"
          name="assignedDatetime"
          className="form-input"
          value={form.assignedDatetime}
          onChange={handleFormChange}
        />

        <input
          type="datetime-local"
          name="deliveryDatetime"
          className="form-input"
          value={form.deliveryDatetime}
          onChange={handleFormChange}
        />

        <select
          className="form-input"
          name="supplierId"
          value={form.supplierId}
          onChange={handleFormChange}
        >
          <option value="">Поставщик</option>
          {suppliers.map(s => (
            <option key={s.id} value={s.id}>
              {s.name}
            </option>
          ))}
        </select>

        <div className="button-group">
          <button className="btn btn-save" onClick={handleSubmit}>
            {editingItem ? 'Сохранить' : 'Добавить'}
          </button>
          {editingItem && (
            <button className="btn btn-cancel" onClick={resetForm}>
              Отмена
            </button>
          )}
        </div>
      </div>

      {/* Фильтр по медикаменту */}
      <div className="form-container">
        <select
          className="form-input"
          value={filterMedId}
          onChange={e => setFilterMedId(e.target.value)}
        >
          <option value="">Все медикаменты</option>
          {uniqueMedList.map(m => (
            <option key={m.id} value={m.id}>
              {m.label}
            </option>
          ))}
        </select>
      </div>

      {loading ? (
        <div className="loading-spinner" />
      ) : (
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Пачка (медикамент — цена)</th>
                <th>Кол-во</th>
                <th>Стоимость</th>
                <th>Дата назначения</th>
                <th>Дата доставки</th>
                <th>Поставщик</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.length ? (
                filtered.map(item => (
                  <tr key={item.id}>
                    <td>
                      {item.medicament.medicament.name} —{' '}
                      {item.medicament.price}₽
                    </td>
                    <td>{item.medicamentAmount}</td>
                    <td>{item.cost}</td>
                    <td>
                      {new Date(item.assignedDatetime).toLocaleString('sv-SE', {
                        year: 'numeric',
                        month: '2-digit',
                        day: '2-digit',
                        hour: '2-digit',
                        minute: '2-digit',
                        second: '2-digit'
                      }).replace(' ', 'T')}
                    </td>
                    <td>
                      {item.deliveryDatetime
                        ? new Date(item.deliveryDatetime).toLocaleString('sv-SE', {
                            year: 'numeric',
                            month: '2-digit',
                            day: '2-digit',
                            hour: '2-digit',
                            minute: '2-digit',
                            second: '2-digit'
                          }).replace(' ', 'T')
                        : '—'}
                    </td>
                    <td>{item.supplier.name}</td>
                    <td className="actions-cell">
                      <button
                        className="btn btn-edit"
                        onClick={() => handleEdit(item)}
                      >
                        Редактировать
                      </button>
                      <button
                        className="btn btn-delete"
                        onClick={() => handleDelete(item)}
                      >
                        Удалить
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="7" className="no-data">
                    Нет данных
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default SuppliesSection;
