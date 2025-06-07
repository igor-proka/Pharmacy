import React, { useEffect, useState } from 'react';
import './Section.css';

function ProductionSection() {
  const [productions, setProductions] = useState([]);
  const [orders, setOrders] = useState([]);
  const [technologies, setTechnologies] = useState([]);
  const [form, setForm] = useState({
    orderId: '',
    technologyId: '',
    medicamentAmount: '',
    startTime: '',
    endTime: '',
  });
  const [editing, setEditing] = useState(null);
  const [filterOrderId, setFilterOrderId] = useState('');

  useEffect(() => {
    Promise.all([
      fetch('/api/production').then(res => res.json()),
      fetch('/api/orders').then(res => res.json()),
      fetch('/api/technologies').then(res => res.json()),
    ])
      .then(([prodData, ordersData, techsData]) => {
        setProductions(prodData);
        setOrders(ordersData);
        setTechnologies(techsData);
      })
      .catch(() => alert('Ошибка при загрузке данных'));
  }, []);

  const resetForm = () => {
    setForm({
      orderId: '',
      technologyId: '',
      medicamentAmount: '',
      startTime: '',
      endTime: '',
    });
    setEditing(null);
  };

  const handleChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    const method = editing ? 'PUT' : 'POST';
    const url = editing ? `/api/production/${editing.id}` : '/api/production';

    const order = orders.find(o => o.id === Number(form.orderId));
    const technology = technologies.find(t => t.id === Number(form.technologyId));
    const body = {
      order,
      technology,
      medicamentAmount: parseInt(form.medicamentAmount, 10),
      startTime: form.startTime || null,
      endTime: form.endTime || null,
    };

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(() => fetch('/api/production').then(r => r.json()))
      .then(setProductions)
      .then(resetForm)
      .catch(() => alert('Ошибка при сохранении производства'));
  };

  const handleEdit = prod => {
    setEditing(prod);
    setForm({
      orderId: prod.order.id.toString(),
      technologyId: prod.technology.id.toString(),
      medicamentAmount: prod.medicamentAmount.toString(),
      startTime: prod.startTime?.slice(0, 16) || '',
      endTime: prod.endTime?.slice(0, 16) || '',
    });
  };

  const handleDelete = prod => {
    if (!window.confirm('Удалить запись производства?')) return;
    fetch(`/api/production/${prod.id}`, { method: 'DELETE' })
      .then(res =>
        res.ok ? fetch('/api/production').then(r => r.json()) : Promise.reject()
      )
      .then(setProductions)
      .catch(() => alert('Ошибка при удалении'));
  };

  const filtered = filterOrderId
    ? productions.filter(p => p.order.id === Number(filterOrderId))
    : productions;

  const formatOrder = o =>
    `${o.customer.fullName}, ${o.customer.phoneNumber}, ${o.customer.address} — ${o.registrationDatetime?.replace('T', ' ')}`;

  // Функция для отображения "Название медикамента — цена пачки"
  const getTechLabel = technology => {
    if (!technology || !technology.medicament) return 'Нет данных';
    const med = technology.medicament;
    // med может содержать { medicament: { name: string }, price: number }
    // Если структура такая, используй мед.medicament.name, иначе просто med.name
    const medName = med.medicament ? med.medicament.name : med.name;
    const price = med.price || 'N/A';
    return `${medName} — ${price}₽`;
  };

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Производство лекарств</h2>

      <div className="form-container">
        <select
          name="orderId"
          className="form-input"
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
          name="technologyId"
          className="form-input"
          value={form.technologyId}
          onChange={handleChange}
        >
          <option value="">Выберите технологию</option>
          {technologies.map(t => (
            <option key={t.id} value={t.id}>
              {getTechLabel(t)}
            </option>
          ))}
        </select>

        <input
          type="number"
          name="medicamentAmount"
          className="form-input"
          placeholder="Кол-во лекарства"
          value={form.medicamentAmount}
          onChange={handleChange}
        />

        <input
          type="datetime-local"
          name="startTime"
          className="form-input"
          value={form.startTime}
          onChange={handleChange}
        />

        <input
          type="datetime-local"
          name="endTime"
          className="form-input"
          value={form.endTime}
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
              <th>Заказ (Клиент + дата)</th>
              <th>Технология</th>
              <th>Кол-во</th>
              <th>Начало</th>
              <th>Конец</th>
              <th>Действия</th>
            </tr>
          </thead>
          <tbody>
            {filtered.length ? (
              filtered.map(prod => (
                <tr key={prod.id}>
                  <td>{formatOrder(prod.order)}</td>
                  <td>{getTechLabel(prod.technology)}</td>
                  <td>{prod.medicamentAmount}</td>
                  <td>{prod.startTime?.replace('T', ' ') || '-'}</td>
                  <td>{prod.endTime?.replace('T', ' ') || '-'}</td>
                  <td className="actions-cell">
                    <button
                      className="btn btn-edit"
                      onClick={() => handleEdit(prod)}
                    >
                      Редактировать
                    </button>
                    <button
                      className="btn btn-delete"
                      onClick={() => handleDelete(prod)}
                    >
                      Удалить
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6" className="no-data">
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

export default ProductionSection;
