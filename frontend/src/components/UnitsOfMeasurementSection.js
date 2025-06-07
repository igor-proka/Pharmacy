// src/components/UnitsOfMeasurementSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function UnitsOfMeasurementSection() {
  const [data, setData] = useState([]);
  const [name, setName] = useState('');
  const [editingItem, setEditingItem] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const formRef = useRef(null);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = () => {
    setIsLoading(true);
    fetch('/api/units-of-measurement')
      .then(res => res.json())
      .then(setData)
      .catch(console.error)
      .finally(() => setIsLoading(false));
  };

  const resetForm = () => {
    setName('');
    setEditingItem(null);
  };

  const handleAdd = () => {
    const trimmedName = name.trim();
    if (!trimmedName) {
      alert('Название не может быть пустым.');
      return;
    }

    if (data.some(unit => unit.name.toLowerCase() === trimmedName.toLowerCase())) {
      alert('Такая единица измерения уже существует.');
      return;
    }

    fetch('/api/units-of-measurement', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: trimmedName }),
    })
      .then(res => res.ok ? res.json() : Promise.reject('Ошибка при создании'))
      .then(newUnit => {
        setData(prev => [...prev, newUnit]);
        resetForm();
      })
      .catch(err => alert(err));
  };

  const handleEdit = (unit) => {
    setEditingItem(unit);
    setName(unit.name);
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  const handleUpdate = () => {
    const trimmedName = name.trim();
    if (!trimmedName) {
      alert('Название не может быть пустым.');
      return;
    }

    if (data.some(unit =>
      unit.id !== editingItem.id &&
      unit.name.toLowerCase() === trimmedName.toLowerCase()
    )) {
      alert('Такая единица измерения уже существует.');
      return;
    }

    fetch(`/api/units-of-measurement/${editingItem.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: trimmedName }),
    })
      .then(res => res.ok ? res.json() : Promise.reject('Ошибка при обновлении'))
      .then(updatedUnit => {
        setData(prev => prev.map(unit => unit.id === updatedUnit.id ? updatedUnit : unit));
        resetForm();
      })
      .catch(err => alert(err));
  };

  const handleDelete = (unit) => {
    if (!window.confirm(`Удалить единицу "${unit.name}"?`)) return;

    fetch(`/api/units-of-measurement/${unit.id}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        setData(prev => prev.filter(u => u.id !== unit.id));
      })
      .catch(() => alert('Не удалось удалить. Возможно, есть зависимые записи.'));
  };

  const handleCancel = () => resetForm();

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Единицы измерения</h2>

      <div className="form-container" ref={formRef}>
        <input
          type="text"
          className="form-input"
          placeholder="Название"
          value={name}
          onChange={e => setName(e.target.value)}
        />

        <div className="button-group">
          {editingItem ? (
            <>
              <button className="btn btn-save" onClick={handleUpdate}>Сохранить</button>
              <button className="btn btn-cancel" onClick={handleCancel}>Отмена</button>
            </>
          ) : (
            <button className="btn btn-add" onClick={handleAdd}>Добавить</button>
          )}
        </div>
      </div>

      {isLoading ? (
        <div className="loading-spinner" />
      ) : (
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Название</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {data.length ? data.map(unit => (
                <tr key={unit.id}>
                  <td>{unit.name}</td>
                  <td className="actions-cell">
                    <button className="btn btn-edit" onClick={() => handleEdit(unit)}>Редактировать</button>
                    <button className="btn btn-delete" onClick={() => handleDelete(unit)}>Удалить</button>
                  </td>
                </tr>
              )) : (
                <tr>
                  <td colSpan="2" className="no-data">Нет данных</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default UnitsOfMeasurementSection;
