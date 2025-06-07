// src/components/MedicamentsSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function MedicamentsSection() {
  const [data, setData] = useState([]);
  const [filteredData, setFilteredData] = useState([]);
  const [types, setTypes] = useState([]);
  const [units, setUnits] = useState([]);
  const [filters, setFilters] = useState({
    typeId: '',
    cookable: '',
    prescriptionRequired: '',
  });
  const [form, setForm] = useState({
    name: '',
    typeId: '',
    unitId: '',
    cookable: false,
    prescriptionRequired: false,
    description: '',
  });
  const [editingItem, setEditingItem] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const formRef = useRef(null);

  useEffect(() => {
    Promise.all([
      fetch('/api/medicaments').then(r => r.json()),
      fetch('/api/medicament-types').then(r => r.json()),
      fetch('/api/units-of-measurement').then(r => r.json()),
    ])
      .then(([meds, types, units]) => {
        setData(meds);
        setTypes(types);
        setUnits(units);
        setFilteredData(meds);
      })
      .catch(err => alert('Ошибка загрузки данных'))
      .finally(() => setIsLoading(false));
  }, []);

  useEffect(() => {
    let filtered = [...data];
    if (filters.typeId) filtered = filtered.filter(m => m.type.id === Number(filters.typeId));
    if (filters.cookable) filtered = filtered.filter(m => String(m.cookable) === filters.cookable);
    if (filters.prescriptionRequired) filtered = filtered.filter(m => String(m.prescriptionRequired) === filters.prescriptionRequired);
    setFilteredData(filtered);
  }, [filters, data]);

  const resetForm = () => {
    setForm({
      name: '',
      typeId: '',
      unitId: '',
      cookable: false,
      prescriptionRequired: false,
      description: '',
    });
    setEditingItem(null);
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = () => {
    const { name, typeId, unitId, description } = form;
    if (!name || !typeId || !unitId || !description) {
      alert('Пожалуйста, заполните все поля');
      return;
    }

    const payload = {
      name: form.name.trim(),
      type: types.find(t => t.id === Number(form.typeId)),
      unit: units.find(u => u.id === Number(form.unitId)),
      cookable: form.cookable,
      prescriptionRequired: form.prescriptionRequired,
      description: form.description.trim(),
    };

    const method = editingItem ? 'PUT' : 'POST';
    const url = editingItem
      ? `/api/medicaments/${editingItem.id}`
      : '/api/medicaments';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => res.ok ? res.json() : Promise.reject('Ошибка сохранения'))
      .then(saved => {
        const updated = editingItem
          ? data.map(m => m.id === saved.id ? saved : m)
          : [...data, saved];
        setData(updated);
        resetForm();
      })
      .catch(() => alert('Ошибка при сохранении'));
  };

  const handleEdit = (med) => {
    setEditingItem(med);
    setForm({
      name: med.name,
      typeId: med.type.id.toString(),
      unitId: med.unit.id.toString(),
      cookable: med.cookable,
      prescriptionRequired: med.prescriptionRequired,
      description: med.description,
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  const handleDelete = (med) => {
    if (!window.confirm(`Удалить медикамент "${med.name}"?`)) return;

    fetch(`/api/medicaments/${med.id}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        const updated = data.filter(m => m.id !== med.id);
        setData(updated);
      })
      .catch(() => alert('Не удалось удалить'));
  };

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Медикаменты</h2>

      <div className="form-container" ref={formRef}>
        <input
          type="text"
          className="form-input"
          name="name"
          placeholder="Название"
          value={form.name}
          onChange={handleChange}
        />

        <select className="form-input" name="typeId" value={form.typeId} onChange={handleChange}>
          <option value="">Тип</option>
          {types.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}
        </select>

        <select className="form-input" name="unitId" value={form.unitId} onChange={handleChange}>
          <option value="">Ед. изм.</option>
          {units.map(u => <option key={u.id} value={u.id}>{u.name}</option>)}
        </select>

        <label className="checkbox-label">
          <input type="checkbox" name="cookable" checked={form.cookable} onChange={handleChange} />
          Изготовляется аптекой
        </label>

        <label className="checkbox-label">
          <input type="checkbox" name="prescriptionRequired" checked={form.prescriptionRequired} onChange={handleChange} />
          Требуется рецепт
        </label>

        <textarea
          className="form-input"
          name="description"
          placeholder="Описание"
          value={form.description}
          onChange={handleChange}
        />

        <div className="button-group">
          <button className="btn btn-save" onClick={handleSubmit}>
            {editingItem ? 'Сохранить' : 'Добавить'}
          </button>
          {editingItem && <button className="btn btn-cancel" onClick={resetForm}>Отмена</button>}
        </div>
      </div>

      {/* Фильтры */}
      <div className="form-container">
        <select className="form-input" name="typeId" value={filters.typeId} onChange={handleFilterChange}>
          <option value="">Фильтр по типу</option>
          {types.map(t => <option key={t.id} value={t.id}>{t.name}</option>)}
        </select>

        <select className="form-input" name="cookable" value={filters.cookable} onChange={handleFilterChange}>
          <option value="">Фильтр: Изготовляемое</option>
          <option value="true">Да</option>
          <option value="false">Нет</option>
        </select>

        <select className="form-input" name="prescriptionRequired" value={filters.prescriptionRequired} onChange={handleFilterChange}>
          <option value="">Фильтр: Рецепт</option>
          <option value="true">Да</option>
          <option value="false">Нет</option>
        </select>
      </div>

      {isLoading ? (
        <div className="loading-spinner" />
      ) : (
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Название</th>
                <th>Тип</th>
                <th>Ед. изм.</th>
                <th>Изготовляемое</th>
                <th>Рецепт</th>
                <th>Описание</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filteredData.length ? filteredData.map(med => (
                <tr key={med.id}>
                  <td>{med.name}</td>
                  <td>{med.type.name}</td>
                  <td>{med.unit.name}</td>
                  <td>{med.cookable ? 'Да' : 'Нет'}</td>
                  <td>{med.prescriptionRequired ? 'Да' : 'Нет'}</td>
                  <td>{med.description}</td>
                  <td className="actions-cell">
                    <button className="btn btn-edit" onClick={() => handleEdit(med)}>Редактировать</button>
                    <button className="btn btn-delete" onClick={() => handleDelete(med)}>Удалить</button>
                  </td>
                </tr>
              )) : (
                <tr>
                  <td colSpan="7" className="no-data">Нет данных</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default MedicamentsSection;
