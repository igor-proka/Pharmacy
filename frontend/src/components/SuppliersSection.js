// src/components/SuppliersSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function SuppliersSection() {
  const [data, setData] = useState([]);
  const [name, setName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [address, setAddress] = useState('');
  const [editingItem, setEditingItem] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const formRef = useRef(null);

  useEffect(() => {
    setIsLoading(true);
    fetch('/api/suppliers')
      .then(res => res.json())
      .then(setData)
      .catch(console.error)
      .finally(() => setIsLoading(false));
  }, []);

  const resetForm = () => {
    setName('');
    setPhoneNumber('');
    setAddress('');
    setEditingItem(null);
  };

  const validatePhone = (phone) => /^\+7\d{10}$/.test(phone);

  const handleAdd = () => {
    if (!name.trim() || !phoneNumber.trim() || !address.trim()) {
      alert('Заполните все поля.');
      return;
    }

    if (!validatePhone(phoneNumber)) {
      alert('Номер телефона должен быть в формате +7XXXXXXXXXX');
      return;
    }

    if (data.some(item =>
      item.name === name.trim() &&
      item.phoneNumber === phoneNumber.trim())) {
      alert('Такой поставщик уже существует.');
      return;
    }

    fetch('/api/suppliers', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: name.trim(),
        phoneNumber: phoneNumber.trim(),
        address: address.trim()
      }),
    })
      .then(res => {
        if (!res.ok) throw new Error('Ошибка при создании поставщика');
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
    setName(item.name);
    setPhoneNumber(item.phoneNumber);
    setAddress(item.address);

    setTimeout(() => {
      const el = formRef.current;
      if (el) {
        const y = el.getBoundingClientRect().top + window.pageYOffset - 20;
        window.scrollTo({ top: y, behavior: 'smooth' });
      }
    }, 0);
  };

  const handleUpdate = () => {
    if (!name.trim() || !phoneNumber.trim() || !address.trim()) {
      alert('Заполните все поля.');
      return;
    }

    if (!validatePhone(phoneNumber)) {
      alert('Номер телефона должен быть в формате +7XXXXXXXXXX');
      return;
    }

    if (
      data.some(item =>
        item.id !== editingItem.id &&
        item.name === name.trim() &&
        item.phoneNumber === phoneNumber.trim())
    ) {
      alert('Такой поставщик уже существует.');
      return;
    }

    fetch(`/api/suppliers/${editingItem.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        name: name.trim(),
        phoneNumber: phoneNumber.trim(),
        address: address.trim()
      }),
    })
      .then(res => {
        if (!res.ok) throw new Error('Ошибка при обновлении поставщика');
        return res.json();
      })
      .then(updated => {
        setData(prev => prev.map(i => i.id === updated.id ? updated : i));
        resetForm();
      })
      .catch(err => alert(err.message));
  };

  const handleDelete = (item) => {
    if (!window.confirm('Удалить поставщика?')) return;
    fetch(`/api/suppliers/${item.id}`, { method: 'DELETE' })
      .then(res => {
        if (res.ok) {
          setData(prev => prev.filter(d => d.id !== item.id));
        } else {
          throw new Error('Ошибка при удалении');
        }
      })
      .catch(err => alert(err.message));
  };

  const handleCancel = () => {
    resetForm();
  };

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Поставщики</h2>

      <div className="form-container" ref={formRef}>
        <input
          type="text"
          className="form-input"
          placeholder="Название"
          value={name}
          onChange={e => setName(e.target.value)}
        />

        <input
          type="text"
          className="form-input"
          placeholder="Телефон (+7...)"
          value={phoneNumber}
          onChange={e => setPhoneNumber(e.target.value)}
        />

        <input
          type="text"
          className="form-input"
          placeholder="Адрес"
          value={address}
          onChange={e => setAddress(e.target.value)}
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
                <th>Телефон</th>
                <th>Адрес</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {data.length ? data.map(item => (
                <tr key={item.id}>
                  <td>{item.name}</td>
                  <td>{item.phoneNumber}</td>
                  <td>{item.address}</td>
                  <td className="actions-cell">
                    <button className="btn btn-edit" onClick={() => handleEdit(item)}>Редактировать</button>
                    <button className="btn btn-delete" onClick={() => handleDelete(item)}>Удалить</button>
                  </td>
                </tr>
              )) : (
                <tr>
                  <td colSpan="4" className="no-data">Нет данных для отображения</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default SuppliersSection;
