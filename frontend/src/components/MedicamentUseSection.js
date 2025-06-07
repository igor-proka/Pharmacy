// src/components/MedicamentUseSection.js
import React, { useRef, useEffect, useState } from 'react';
import './Section.css';

function MedicamentUseSection() {
  const [data, setData] = useState([]);
  const [types, setTypes] = useState([]);
  const [uses, setUses] = useState([]);
  const [selectedType, setSelectedType] = useState('');
  const [selectedUse, setSelectedUse] = useState('');
  const [editingItem, setEditingItem] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  // внутри компонента
  const formRef = useRef(null);

  useEffect(() => {
    setIsLoading(true);
    Promise.all([
      fetch('/api/medicament-use').then((res) => res.json()),
      fetch('/api/medicament-types').then((res) => res.json()),
      fetch('/api/ways-of-use').then((res) => res.json()),
    ])
      .then(([usesData, typesData, waysData]) => {
        setData(usesData);
        setTypes(typesData);
        setUses(waysData);
      })
      .catch(console.error)
      .finally(() => setIsLoading(false));
  }, []);

  const handleAdd = () => {
    if (!selectedType || !selectedUse) {
      alert('Выберите тип и способ использования.');
      return;
    }
    if (data.some(item => item.typeId === selectedType && item.useId === selectedUse)) {
      alert('Такая запись уже существует.');
      return;
    }
    fetch('/api/medicament-use', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ typeId: selectedType, useId: selectedUse }),
    })
      .then(res => res.json())
      .then(created => {
        setData(prev => [...prev, created]);
        setSelectedType('');
        setSelectedUse('');
      })
      .catch(err => alert('Ошибка при добавлении: ' + err.message));
  };

  const handleEdit = item => {
    setEditingItem(item);
    setSelectedType(item.typeId);
    setSelectedUse(item.useId);
    setTimeout(() => {
      const el = formRef.current;
      if (el) {
        const y = el.getBoundingClientRect().top + window.pageYOffset - 20; // 20px отступ
        window.scrollTo({ top: y, behavior: 'smooth' });
      }
    }, 0);
  };

  const handleDelete = item => {
    if (!window.confirm('Удалить запись?')) return;
    fetch(`/api/medicament-use/${item.typeId}/${item.useId}`, { method: 'DELETE' })
      .then(() => {
        setData(prev => prev.filter(d => !(d.typeId === item.typeId && d.useId === item.useId)));
      })
      .catch(err => alert('Ошибка при удалении: ' + err.message));
  };

  const handleUpdate = () => {
    if (!selectedType || !selectedUse) {
      alert('Выберите тип и способ использования.');
      return;
    }
    if (data.some(item =>
      !(item.typeId === editingItem.typeId && item.useId === editingItem.useId) &&
      item.typeId === selectedType && item.useId === selectedUse
    )) {
      alert('Такая запись уже существует.');
      return;
    }
    fetch(`/api/medicament-use/${editingItem.typeId}/${editingItem.useId}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ typeId: selectedType, useId: selectedUse }),
    })
      .then(res => res.json())
      .then(updated => {
        setData(prev => [
          ...prev.filter(item =>
            !(item.typeId === editingItem.typeId && item.useId === editingItem.useId)
          ),
          updated
        ]);
        setEditingItem(null);
        setSelectedType('');
        setSelectedUse('');
      })
      .catch(err => alert('Ошибка при обновлении: ' + err.message));
  };

  const handleCancel = () => {
    setEditingItem(null);
    setSelectedType('');
    setSelectedUse('');
  };

  const getTypeName = id => types.find(t => t.id === id)?.name || '';
  const getUseDescription = id => uses.find(u => u.id === id)?.description || '';

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Способы применения медикаментов</h2>

      <div className="form-container" ref={formRef}>
        <select
          className="form-input"
          value={selectedType}
          onChange={e => {
            const v = e.target.value;
            setSelectedType(v === '' ? '' : Number(v));
          }}
        >
          <option value="">Выберите тип лекарства</option>
          {types.map(t => (
            <option key={t.id} value={t.id}>{t.name}</option>
          ))}
        </select>

        <select
          className="form-input"
          value={selectedUse}
          onChange={e => {
            const v = e.target.value;
            setSelectedUse(v === '' ? '' : Number(v));
          }}
        >
          <option value="">Выберите способ использования</option>
          {uses.map(u => (
            <option key={u.id} value={u.id}>{u.description}</option>
          ))}
        </select>

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
                <th>Тип лекарства</th>
                <th>Способ использования</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {data.length ? data.map(item => (
                <tr key={`${item.typeId}-${item.useId}`}>
                  <td>{getTypeName(item.typeId)}</td>
                  <td>{getUseDescription(item.useId)}</td>
                  <td className="actions-cell">
                    <button className="btn btn-edit" onClick={() => handleEdit(item)}>Редактировать</button>
                    <button className="btn btn-delete" onClick={() => handleDelete(item)}>Удалить</button>
                  </td>
                </tr>
              )) : (
                <tr>
                  <td colSpan="3" className="no-data">Нет данных для отображения</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default MedicamentUseSection;
