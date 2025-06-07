import React, { useEffect, useState } from 'react';
import './Section.css';

function OrdersSection() {
  const [orders, setOrders] = useState([]);
  const [customers, setCustomers] = useState([]);
  const [form, setForm] = useState({
    customerId: '',
    cost: '',
    paid: false,
    registrationDatetime: '',
    appointedDatetime: '',
    obtainingDatetime: '',
  });
  const [filterCustomerId, setFilterCustomerId] = useState('');
  const [editing, setEditing] = useState(null);

  useEffect(() => {
    Promise.all([
      fetch('/api/orders').then(res => res.json()),
      fetch('/api/customers').then(res => res.json()),
    ])
      .then(([ordersData, customersData]) => {
        setOrders(ordersData);
        setCustomers(customersData);
      })
      .catch(() => alert('Ошибка при загрузке данных заказов или клиентов'));
  }, []);

  const resetForm = () => {
    setForm({
      customerId: '',
      cost: '',
      paid: false,
      registrationDatetime: '',
      appointedDatetime: '',
      obtainingDatetime: '',
    });
    setEditing(null);
  };

  const handleChange = e => {
    const { name, value, type, checked } = e.target;
    const val = type === 'checkbox' ? checked : value;
    setForm(prev => ({ ...prev, [name]: val }));
  };

  const handleSubmit = () => {
    const method = editing ? 'PUT' : 'POST';
    const url = editing ? `/api/orders/${editing.id}` : '/api/orders';

    const customer = customers.find(c => c.id === Number(form.customerId));
    const body = {
      customer,
      cost: parseInt(form.cost, 10),
      paid: form.paid,
      registrationDatetime: form.registrationDatetime,
      appointedDatetime: form.appointedDatetime || null,
      obtainingDatetime: form.obtainingDatetime || null,
    };

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    })
      .then(res => res.ok ? res.json() : Promise.reject())
      .then(() => fetch('/api/orders').then(r => r.json()))
      .then(setOrders)
      .then(resetForm)
      .catch(() => alert('Ошибка при сохранении заказа'));
  };

  const handleEdit = (order) => {
    setEditing(order);
    setForm({
      customerId: order.customer.id.toString(),
      cost: order.cost.toString(),
      paid: order.paid,
      registrationDatetime: order.registrationDatetime?.slice(0, 16),
      appointedDatetime: order.appointedDatetime?.slice(0, 16) || '',
      obtainingDatetime: order.obtainingDatetime?.slice(0, 16) || '',
    });
  };

  const handleDelete = (order) => {
    if (!window.confirm('Удалить заказ?')) return;
    fetch(`/api/orders/${order.id}`, { method: 'DELETE' })
      .then(res => res.ok ? fetch('/api/orders').then(r => r.json()) : Promise.reject())
      .then(setOrders)
      .catch(() => alert('Ошибка при удалении заказа'));
  };

  const filtered = filterCustomerId
    ? orders.filter(o => o.customer.id === Number(filterCustomerId))
    : orders;

  const formatCustomer = (c) => `${c.fullName}, ${c.phoneNumber}, ${c.address}`;

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Заказы</h2>

      <div className="form-container">
        <select
          name="customerId"
          className="form-input"
          value={form.customerId}
          onChange={handleChange}
        >
          <option value="">Клиент</option>
          {customers.map(c => (
            <option key={c.id} value={c.id}>
              {formatCustomer(c)}
            </option>
          ))}
        </select>

        <input
          type="number"
          name="cost"
          className="form-input"
          placeholder="Стоимость"
          value={form.cost}
          onChange={handleChange}
        />

        <input
          type="datetime-local"
          name="registrationDatetime"
          className="form-input"
          value={form.registrationDatetime}
          onChange={handleChange}
        />

        <input
          type="datetime-local"
          name="appointedDatetime"
          className="form-input"
          value={form.appointedDatetime}
          onChange={handleChange}
        />

        <input
          type="datetime-local"
          name="obtainingDatetime"
          className="form-input"
          value={form.obtainingDatetime}
          onChange={handleChange}
        />

        <label className="checkbox-label">
          <input
            type="checkbox"
            name="paid"
            checked={form.paid}
            onChange={handleChange}
          />
          Оплачен
        </label>

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
          value={filterCustomerId}
          onChange={e => setFilterCustomerId(e.target.value)}
        >
          <option value="">Все клиенты</option>
          {customers.map(c => (
            <option key={c.id} value={c.id}>
              {formatCustomer(c)}
            </option>
          ))}
        </select>
      </div>

      <div className="table-container">
        <table className="data-table">
          <thead>
            <tr>
              <th>Клиент</th>
              <th>Регистрация</th>
              <th>Назначено</th>
              <th>Выдано</th>
              <th>Стоимость</th>
              <th>Оплачен</th>
              <th>Действия</th>
            </tr>
          </thead>
          <tbody>
            {filtered.length ? (
              filtered.map(order => (
                <tr key={order.id}>
                  <td>{formatCustomer(order.customer)}</td>
                  <td>{order.registrationDatetime?.replace('T', ' ')}</td>
                  <td>{order.appointedDatetime?.replace('T', ' ') || '-'}</td>
                  <td>{order.obtainingDatetime?.replace('T', ' ') || '-'}</td>
                  <td>{order.cost}₽</td>
                  <td>{order.paid ? 'Да' : 'Нет'}</td>
                  <td className="actions-cell">
                    <button className="btn btn-edit" onClick={() => handleEdit(order)}>Редактировать</button>
                    <button className="btn btn-delete" onClick={() => handleDelete(order)}>Удалить</button>
                  </td>
                </tr>
              ))
            ) : (
              <tr><td colSpan="7" className="no-data">Нет данных</td></tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default OrdersSection;
