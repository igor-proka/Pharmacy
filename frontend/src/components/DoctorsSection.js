import React, { useRef, useEffect, useState } from 'react';
import './Section.css';

function DoctorsSection() {
  const [data, setData] = useState([]);
  const [fullName, setFullName] = useState('');
  const [position, setPosition] = useState('');
  const [editingItem, setEditingItem] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const formRef = useRef(null);

  useEffect(() => {
    setIsLoading(true);
    fetch('/api/doctors')
      .then(res => res.json())
      .then(doctors => setData(doctors))
      .catch(err => console.error(err))
      .finally(() => setIsLoading(false));
  }, []);

  const resetForm = () => {
    setFullName('');
    setPosition('');
    setEditingItem(null);
  };

  const handleAdd = () => {
    if (!fullName.trim() || !position.trim()) {
      alert('Заполните ФИО и должность.');
      return;
    }

    if (data.some(doc => doc.fullName === fullName.trim() && doc.position === position.trim())) {
      alert('Доктор с такими данными уже существует.');
      return;
    }

    fetch('/api/doctors', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ fullName: fullName.trim(), position: position.trim() }),
    })
      .then(res => {
        if (!res.ok) throw new Error('Ошибка при создании доктора');
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
    setPosition(item.position);

    setTimeout(() => {
      const el = formRef.current;
      if (el) {
        const y = el.getBoundingClientRect().top + window.pageYOffset - 20;
        window.scrollTo({ top: y, behavior: 'smooth' });
      }
    }, 0);
  };

  const handleDelete = (item) => {
    if (!window.confirm('Удалить доктора?')) return;

    fetch(`/api/doctors/${item.id}`, { method: 'DELETE' })
      .then(res => {
        if (res.ok) {
          setData(prev => prev.filter(d => d.id !== item.id));
        } else {
          throw new Error('Ошибка при удалении');
        }
      })
      .catch(err => alert(err.message));
  };

  const handleUpdate = () => {
    if (!fullName.trim() || !position.trim()) {
      alert('Заполните ФИО и должность.');
      return;
    }

    if (
      data.some(doc =>
        doc.id !== editingItem.id &&
        doc.fullName === fullName.trim() &&
        doc.position === position.trim()
      )
    ) {
      alert('Доктор с такими данными уже существует.');
      return;
    }

    fetch(`/api/doctors/${editingItem.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ fullName: fullName.trim(), position: position.trim() }),
    })
      .then(res => {
        if (!res.ok) throw new Error('Ошибка при обновлении');
        return res.json();
      })
      .then(updated => {
        setData(prev => prev.map(doc => doc.id === updated.id ? updated : doc));
        resetForm();
      })
      .catch(err => alert(err.message));
  };

  const handleCancel = () => {
    resetForm();
  };

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Доктора</h2>

      <div className="form-container" ref={formRef}>
        <input
          type="text"
          className="form-input"
          placeholder="ФИО"
          value={fullName}
          onChange={e => setFullName(e.target.value)}
        />
        <input
          type="text"
          className="form-input"
          placeholder="Должность"
          value={position}
          onChange={e => setPosition(e.target.value)}
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
                <th>Должность</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {data.length ? data.map(item => (
                <tr key={item.id}>
                  <td>{item.fullName}</td>
                  <td>{item.position}</td>
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

export default DoctorsSection;
