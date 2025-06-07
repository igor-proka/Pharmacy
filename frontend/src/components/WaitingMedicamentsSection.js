import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function WaitingMedicamentsSection() {
  const [waitingList, setWaitingList] = useState([]);
  const [orders, setOrders] = useState([]);
  const [packages, setPackages] = useState([]);
  const [form, setForm] = useState({
    orderId: '',
    medicamentPackageId: '',
    amount: '',
  });
  const [editing, setEditing] = useState(null);
  const [filterOrderId, setFilterOrderId] = useState('');
  const formRef = useRef(null);

  useEffect(() => {
    Promise.all([
      fetch('/api/waiting-medicaments').then(r => r.json()),
      fetch('/api/orders').then(r => r.json()),
      fetch('/api/medicament-packages').then(r => r.json()),
    ])
      .then(([wm, ord, packs]) => {
        setWaitingList(wm);
        setOrders(ord);
        setPackages(packs);
      })
      .catch(() => alert('Ошибка загрузки данных'));
  }, []);

  const resetForm = () => {
    setForm({
      orderId: '',
      medicamentPackageId: '',
      amount: '',
    });
    setEditing(null);
  };

  const handleChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    const { orderId, medicamentPackageId, amount } = form;
    if (!orderId || !medicamentPackageId || !amount) {
      alert('Заполните все поля');
      return;
    }

    const order = orders.find(o => o.id === Number(orderId));
    const pkg = packages.find(p => p.id === Number(medicamentPackageId));
    if (!order || !pkg) {
      alert('Неверный заказ или медикамент');
      return;
    }

    const payload = {
      id: {
        orderId: order.id,
        medicamentId: pkg.id,
      },
      order,
      medicament: pkg,
      amount: Number(amount),
    };

    const method = editing ? 'PUT' : 'POST';
    const url = editing
      ? `/api/waiting-medicaments/${order.id}/${pkg.id}`
      : '/api/waiting-medicaments';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(() => fetch('/api/waiting-medicaments').then(r => r.json()))
      .then(setWaitingList)
      .then(resetForm)
      .catch(() => alert('Ошибка при сохранении'));
  };

  const handleEdit = item => {
    setEditing(item);
    setForm({
      orderId: item.order.id.toString(),
      medicamentPackageId: item.medicament.id.toString(),
      amount: item.amount.toString(),
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  const handleDelete = item => {
    if (!window.confirm('Удалить запись?')) return;
    fetch(`/api/waiting-medicaments/${item.order.id}/${item.medicament.id}`, {
      method: 'DELETE',
    })
      .then(res =>
        res.ok ? fetch('/api/waiting-medicaments').then(r => r.json()) : Promise.reject()
      )
      .then(setWaitingList)
      .catch(() => alert('Ошибка при удалении'));
  };

  const formatOrder = o =>
    `${o.customer.fullName}, ${o.customer.phoneNumber}, ${o.customer.address} — ${o.registrationDatetime?.replace('T', ' ')}`;

  const filtered = filterOrderId
    ? waitingList.filter(wm => wm.order.id === Number(filterOrderId))
    : waitingList;

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Ожидание медикаментов</h2>

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
          name="medicamentPackageId"
          value={form.medicamentPackageId}
          onChange={handleChange}
        >
          <option value="">Пачка (медикамент — цена)</option>
          {packages.map(p => (
            <option key={p.id} value={p.id}>
              {p.medicament.name} — {p.price}₽
            </option>
          ))}
        </select>

        <input
          type="number"
          name="amount"
          className="form-input"
          placeholder="Количество"
          value={form.amount}
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
              <th>Пачка (медикамент — цена)</th>
              <th>Количество</th>
              <th>Действия</th>
            </tr>
          </thead>
          <tbody>
            {filtered.length ? (
              filtered.map(wm => (
                <tr key={`${wm.order.id}-${wm.medicament.id}`}>
                  <td>{formatOrder(wm.order)}</td>
                  <td>
                    {wm.medicament.medicament.name} — {wm.medicament.price}₽
                  </td>
                  <td>{wm.amount}</td>
                  <td className="actions-cell">
                    <button className="btn btn-edit" onClick={() => handleEdit(wm)}>Редактировать</button>
                    <button className="btn btn-delete" onClick={() => handleDelete(wm)}>Удалить</button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="4" className="no-data">Нет данных</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default WaitingMedicamentsSection;
