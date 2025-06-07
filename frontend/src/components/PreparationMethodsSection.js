// src/components/PreparationMethodsSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function PreparationMethodsSection() {
  const [methods, setMethods] = useState([]);
  const [formName, setFormName] = useState('');
  const [editingItem, setEditingItem] = useState(null);
  const [loading, setLoading] = useState(true);
  const formRef = useRef(null);

  useEffect(() => {
    setLoading(true);
    fetch('/api/preparation-methods')
      .then(r => r.json())
      .then(data => setMethods(data))
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setLoading(false));
  }, []);

  const resetForm = () => {
    setFormName('');
    setEditingItem(null);
  };

  const handleSubmit = () => {
    const name = formName.trim();
    if (!name) {
      alert('Введите название метода');
      return;
    }

    const payload = { name };
    const method = editingItem ? 'PUT' : 'POST';
    const url = editingItem
      ? `/api/preparation-methods/${editingItem.id}`
      : '/api/preparation-methods';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(() => fetch('/api/preparation-methods').then(r => r.json()))
      .then(all => {
        setMethods(all);
        resetForm();
      })
      .catch(() => alert('Ошибка при сохранении'));
  };

  const handleEdit = item => {
    setEditingItem(item);
    setFormName(item.name);
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  const handleDelete = item => {
    if (!window.confirm(`Удалить "${item.name}"?`)) return;
    fetch(`/api/preparation-methods/${item.id}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        return fetch('/api/preparation-methods');
      })
      .then(r => r.json())
      .then(all => setMethods(all))
      .catch(() => alert('Ошибка при удалении'));
  };

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Методы приготовления лекарств</h2>

      <div className="form-container" ref={formRef}>
        <input
          type="text"
          className="form-input"
          placeholder="Название метода"
          value={formName}
          onChange={e => setFormName(e.target.value)}
        />
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

      {loading ? (
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
              {methods.length ? (
                methods.map(item => (
                  <tr key={item.id}>
                    <td>{item.name}</td>
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
                  <td colSpan="2" className="no-data">
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

export default PreparationMethodsSection;
