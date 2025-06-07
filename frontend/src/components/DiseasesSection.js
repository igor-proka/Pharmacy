// src/components/DiseasesSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function DiseasesSection() {
  const [diseases, setDiseases] = useState([]);
  const [formName, setFormName] = useState('');
  const [editingItem, setEditingItem] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const formRef = useRef(null);

  // Загрузка списка болезней
  useEffect(() => {
    setIsLoading(true);
    fetch('/api/diseases')
      .then(res => res.json())
      .then(data => setDiseases(data))
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setIsLoading(false));
  }, []);

  // Сброс формы
  const resetForm = () => {
    setFormName('');
    setEditingItem(null);
  };

  // Добавление или обновление
  const handleSubmit = () => {
    const trimmed = formName.trim();
    if (!trimmed) {
      alert('Введите название болезни');
      return;
    }
    // Проверка дубликата (для добавления и при редактировании)
    const dup = diseases.some(d =>
      d.name.toLowerCase() === trimmed.toLowerCase() &&
      (!editingItem || d.id !== editingItem.id)
    );
    if (dup) {
      alert('Болезнь с таким названием уже существует');
      return;
    }

    const payload = { name: trimmed };
    if (editingItem) {
      // PUT
      fetch(`/api/diseases/${editingItem.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })
        .then(res => res.ok ? res.json() : Promise.reject())
        .then(updated => {
          setDiseases(prev => prev.map(d => d.id === updated.id ? updated : d));
          resetForm();
        })
        .catch(() => alert('Ошибка при обновлении'));
    } else {
      // POST
      fetch('/api/diseases', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })
        .then(res => res.ok ? res.json() : Promise.reject())
        .then(created => {
          setDiseases(prev => [...prev, created]);
          resetForm();
        })
        .catch(() => alert('Ошибка при добавлении'));
    }
  };

  // Редактировать
  const handleEdit = item => {
    setEditingItem(item);
    setFormName(item.name);
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  // Удалить
  const handleDelete = item => {
    if (!window.confirm('Удалить эту болезнь?')) return;
    fetch(`/api/diseases/${item.id}`, { method: 'DELETE' })
      .then(res => {
        if (res.ok) {
          setDiseases(prev => prev.filter(d => d.id !== item.id));
        } else {
          throw new Error();
        }
      })
      .catch(() => alert('Ошибка при удалении'));
  };

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Болезни</h2>

      <div className="form-container" ref={formRef}>
        <input
          type="text"
          className="form-input"
          placeholder="Название болезни"
          value={formName}
          onChange={e => setFormName(e.target.value)}
        />

        <div className="button-group">
          {editingItem ? (
            <>
              <button className="btn btn-save" onClick={handleSubmit}>Сохранить</button>
              <button className="btn btn-cancel" onClick={resetForm}>Отмена</button>
            </>
          ) : (
            <button className="btn btn-add" onClick={handleSubmit}>Добавить</button>
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
              {diseases.length ? diseases.map(item => (
                <tr key={item.id}>
                  <td>{item.name}</td>
                  <td className="actions-cell">
                    <button className="btn btn-edit" onClick={() => handleEdit(item)}>Редактировать</button>
                    <button className="btn btn-delete" onClick={() => handleDelete(item)}>Удалить</button>
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

export default DiseasesSection;
