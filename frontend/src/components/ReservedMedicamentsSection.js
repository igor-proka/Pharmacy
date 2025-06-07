// src/components/ReservedMedicamentsSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function ReservedMedicamentsSection() {
  const [reservedList, setReservedList] = useState([]);
  const [orders, setOrders] = useState([]);
  const [storageList, setStorageList] = useState([]);
  const [form, setForm] = useState({
    orderId: '',
    storageId: '',
    packageCount: '',
  });
  const [editing, setEditing] = useState(null);
  const [filterOrderId, setFilterOrderId] = useState('');
  const formRef = useRef(null);

  useEffect(() => {
    Promise.all([
      fetch('/api/reserved-medicaments').then(r => r.json()),
      fetch('/api/orders').then(r => r.json()),
      fetch('/api/storage').then(r => r.json()),
    ])
      .then(([reservedData, ordersData, storageData]) => {
        setReservedList(reservedData);
        setOrders(ordersData);
        setStorageList(storageData);
      })
      .catch(() => alert('Ошибка при загрузке данных'));
  }, []);

  const resetForm = () => {
    setForm({
      orderId: '',
      storageId: '',
      packageCount: '',
    });
    setEditing(null);
  };

  const handleChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    const { orderId, storageId, packageCount } = form;
    if (!orderId || !storageId || !packageCount) {
      alert('Заполните все поля');
      return;
    }

    const order = orders.find(o => o.id === Number(orderId));
    const storage = storageList.find(s => s.id === Number(storageId));
    if (!order || !storage) {
      alert('Неверный заказ или складская запись');
      return;
    }

    const payload = {
      id: {
        orderId: order.id,
        storageId: storage.id,
      },
      order,
      storage,
      packageCount: Number(packageCount),
    };

    const method = editing ? 'PUT' : 'POST';
    const url = editing
      ? `/api/reserved-medicaments/${editing.order.id}/${editing.storage.id}`
      : '/api/reserved-medicaments';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(() => fetch('/api/reserved-medicaments').then(r => r.json()))
      .then(setReservedList)
      .then(resetForm)
      .catch(() => alert('Ошибка при сохранении'));
  };

  const handleEdit = item => {
    setEditing(item);
    setForm({
      orderId: item.order.id.toString(),
      storageId: item.storage.id.toString(),
      packageCount: item.packageCount.toString(),
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  const handleDelete = item => {
    if (!window.confirm('Удалить зарезервированный медикамент?')) return;
    fetch(`/api/reserved-medicaments/${item.order.id}/${item.storage.id}`, {
      method: 'DELETE',
    })
      .then(res =>
        res.ok ? fetch('/api/reserved-medicaments').then(r => r.json()) : Promise.reject()
      )
      .then(setReservedList)
      .catch(() => alert('Ошибка при удалении'));
  };

  const formatOrder = o =>
    `${o.customer.fullName}, ${o.customer.phoneNumber}, ${o.customer.address} — ${o.registrationDatetime?.replace('T', ' ')}`;

  // Составляем строку для отображения каждой записи storage:
  // "медикамент — цена₽, доступно: X, изготовлено: YYYY-MM-DD"
  const getStorageLabel = storage => {
    if (!storage || !storage.medicamentPackage) return 'Нет данных';
    const pkg = storage.medicamentPackage;
    const medName = pkg.medicament.name;
    const price = pkg.price;
    const available = storage.availableAmount;
    const mfgDate = storage.manufactureDate || '—';
    return `${medName} — ${price}₽, доступно: ${available}, изготовлено: ${mfgDate}`;
  };

  const filtered = filterOrderId
    ? reservedList.filter(rm => rm.order.id === Number(filterOrderId))
    : reservedList;

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Зарезервированные лекарства</h2>

      <div className="form-container" ref={formRef}>
        <select
          className="form-input"
          name="orderId"
          value={form.orderId}
          onChange={handleChange}
        >
          <option value="">Выберите заказ</option>
          {orders.map(o => (
            <option key={o.id} value={o.id}>
              {formatOrder(o)}
            </option>
          ))}
        </select>

        <select
          className="form-input"
          name="storageId"
          value={form.storageId}
          onChange={handleChange}
        >
          <option value="">Выберите складскую запись</option>
          {storageList.map(s => (
            <option key={s.id} value={s.id}>
              {getStorageLabel(s)}
            </option>
          ))}
        </select>

        <input
          type="number"
          name="packageCount"
          className="form-input"
          placeholder="Кол-во пачек"
          value={form.packageCount}
          onChange={handleChange}
        />

        <div className="button-group">
          <button className="btn btn-save" onClick={handleSubmit}>
            {editing ? 'Сохранить' : 'Добавить'}
          </button>
          {editing && (
            <button className="btn btn-cancel" onClick={resetForm}>
              Отмена
            </button>
          )}
        </div>
      </div>

      <div className="form-container">
        <select
          className="form-input"
          value={filterOrderId}
          onChange={e => setFilterOrderId(e.target.value)}
        >
          <option value="">Все заказы</option>
          {orders.map(o => (
            <option key={o.id} value={o.id}>
              {formatOrder(o)}
            </option>
          ))}
        </select>
      </div>

      <div className="table-container">
        <table className="data-table">
          <thead>
            <tr>
              <th>Заказ</th>
              <th>Медикамент (медикамент — цена, доступно, изготовлено)</th>
              <th>Кол-во пачек</th>
              <th>Действия</th>
            </tr>
          </thead>
          <tbody>
            {filtered.length ? (
              filtered.map(rm => (
                <tr key={`${rm.order.id}-${rm.storage.id}`}>
                  <td>{formatOrder(rm.order)}</td>
                  <td>{getStorageLabel(rm.storage)}</td>
                  <td>{rm.packageCount}</td>
                  <td className="actions-cell">
                    <button
                      className="btn btn-edit"
                      onClick={() => handleEdit(rm)}
                    >
                      Редактировать
                    </button>
                    <button
                      className="btn btn-delete"
                      onClick={() => handleDelete(rm)}
                    >
                      Удалить
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="4" className="no-data">
                  Нет данных
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default ReservedMedicamentsSection;
