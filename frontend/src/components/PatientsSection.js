// src/components/PatientsSection.js
import React, { useRef, useEffect, useState } from 'react';
import './Section.css';

function PatientsSection() {
  const [data, setData] = useState([]);
  const [fullName, setFullName] = useState('');
  const [birthday, setBirthday] = useState('');
  const [editingItem, setEditingItem] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  // ref для плавного скролла к форме при редактировании
  const formRef = useRef(null);

  useEffect(() => {
    setIsLoading(true);
    fetch('/api/patients')
      .then((res) => res.json())
      .then((patients) => {
        setData(patients);
      })
      .catch((err) => console.error(err))
      .finally(() => setIsLoading(false));
  }, []);

  const resetForm = () => {
    setFullName('');
    setBirthday('');
    setEditingItem(null);
  };

  const handleAdd = () => {
    if (!fullName.trim() || !birthday) {
      alert('Заполните ФИО и дату рождения.');
      return;
    }
    // Проверка на дубликат по ФИО + дате
    if (data.some(item => item.fullName === fullName.trim() && item.birthday === birthday)) {
      alert('Пациент с таким ФИО и датой рождения уже существует.');
      return;
    }
    fetch('/api/patients', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ fullName: fullName.trim(), birthday }),
    })
      .then(res => {
        if (!res.ok) throw new Error('Ошибка при создании пациента');
        return res.json();
      })
      .then(created => {
        setData(prev => [...prev, created]);
        resetForm();
      })
      .catch(err => alert(err.message));
  };

  const handleEdit = (item) => {
    setEditingItem(item);
    setFullName(item.fullName);
    setBirthday(item.birthday);
    // плавный скролл к форме
    setTimeout(() => {
      const el = formRef.current;
      if (el) {
        const y = el.getBoundingClientRect().top + window.pageYOffset - 20;
        window.scrollTo({ top: y, behavior: 'smooth' });
      }
    }, 0);
  };

  const handleDelete = (item) => {
    if (!window.confirm('Удалить пациента?')) return;
    fetch(`/api/patients/${item.id}`, { method: 'DELETE' })
      .then(res => {
        if (res.ok) {
          setData(prev => prev.filter(d => d.id !== item.id));
        } else {
          throw new Error('Ошибка при удалении пациента');
        }
      })
      .catch(err => alert(err.message));
  };

  const handleUpdate = () => {
    if (!fullName.trim() || !birthday) {
      alert('Заполните ФИО и дату рождения.');
      return;
    }
    // Проверка на дубликат (кроме текущего редактируемого)
    if (
      data.some(item =>
        item.id !== editingItem.id &&
        item.fullName === fullName.trim() &&
        item.birthday === birthday
      )
    ) {
      alert('Пациент с таким ФИО и датой рождения уже существует.');
      return;
    }
    fetch(`/api/patients/${editingItem.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ fullName: fullName.trim(), birthday }),
    })
      .then(res => {
        if (!res.ok) throw new Error('Ошибка при обновлении пациента');
        return res.json();
      })
      .then(updated => {
        setData(prev => prev.map(item => item.id === updated.id ? updated : item));
        resetForm();
      })
      .catch(err => alert(err.message));
  };

  const handleCancel = () => {
    resetForm();
  };

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Пациенты</h2>

      <div className="form-container" ref={formRef}>
        <input
          type="text"
          className="form-input"
          placeholder="ФИО"
          value={fullName}
          onChange={e => setFullName(e.target.value)}
        />

        <input
          type="date"
          className="form-input"
          value={birthday}
          onChange={e => setBirthday(e.target.value)}
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
                <th>ФИО</th>
                <th>Дата рождения</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {data.length ? data.map(item => (
                <tr key={item.id}>
                  <td>{item.fullName}</td>
                  <td>{item.birthday}</td>
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

export default PatientsSection;
