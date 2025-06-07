import React, { useEffect, useState } from 'react';
import './Section.css';

function TechnologyComponentsSection() {
  const [components, setComponents] = useState([]);
  const [technologies, setTechnologies] = useState([]);
  const [packages, setPackages] = useState([]);
  const [form, setForm] = useState({
    technologyId: '',
    componentId: '',
    componentAmount: '',
  });
  const [filterTechnologyId, setFilterTechnologyId] = useState('');
  const [editing, setEditing] = useState(null);

  useEffect(() => {
    Promise.all([
      fetch('/api/technology-components').then(res => res.json()),
      fetch('/api/technologies').then(res => res.json()),
      fetch('/api/medicament-packages').then(res => res.json()),
    ])
      .then(([components, technologies, packages]) => {
        setComponents(components);
        setTechnologies(technologies);
        setPackages(packages);
      })
      .catch(() => alert('Ошибка загрузки данных'));
  }, []);

  const resetForm = () => {
    setForm({ technologyId: '', componentId: '', componentAmount: '' });
    setEditing(null);
  };

  const handleChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    const method = editing ? 'PUT' : 'POST';
    const url = editing
      ? `/api/technology-components/${editing.technology.id}/${editing.component.id}`
      : '/api/technology-components';

    const body = {
      technology: technologies.find(t => t.id === Number(form.technologyId)),
      component: packages.find(p => p.id === Number(form.componentId)),
      componentAmount: parseInt(form.componentAmount, 10),
    };

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body),
    })
      .then(res => res.ok ? res.json() : Promise.reject())
      .then(() => fetch('/api/technology-components').then(r => r.json()))
      .then(setComponents)
      .then(resetForm)
      .catch(() => alert('Ошибка при сохранении компонента'));
  };

  const handleEdit = (item) => {
    setEditing(item);
    setForm({
      technologyId: item.technology.id.toString(),
      componentId: item.component.id.toString(),
      componentAmount: item.componentAmount.toString(),
    });
  };

  const handleDelete = (item) => {
    if (!window.confirm('Удалить компонент из технологии?')) return;
    fetch(`/api/technology-components/${item.technology.id}/${item.component.id}`, {
      method: 'DELETE',
    })
      .then(res => res.ok ? fetch('/api/technology-components').then(r => r.json()) : Promise.reject())
      .then(setComponents)
      .catch(() => alert('Ошибка при удалении компонента'));
  };

  const filtered = filterTechnologyId
    ? components.filter(c => c.technology.id === Number(filterTechnologyId))
    : components;

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Компоненты для приготовления</h2>

      <div className="form-container">
        <select
          name="technologyId"
          className="form-input"
          value={form.technologyId}
          onChange={handleChange}
        >
          <option value="">Технология (медикамент — цена)</option>
          {technologies.map(t => (
            <option key={t.id} value={t.id}>
              {t.medicament.medicament.name} — {t.medicament.price}₽
            </option>
          ))}
        </select>

        <select
          name="componentId"
          className="form-input"
          value={form.componentId}
          onChange={handleChange}
        >
          <option value="">Компонент (медикамент — цена)</option>
          {packages.map(p => (
            <option key={p.id} value={p.id}>
              {p.medicament.name} — {p.price}₽
            </option>
          ))}
        </select>

        <input
          type="number"
          name="componentAmount"
          className="form-input"
          placeholder="Количество компонента"
          value={form.componentAmount}
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

      {/* Фильтр по технологии */}
      <div className="form-container">
        <select
          className="form-input"
          value={filterTechnologyId}
          onChange={e => setFilterTechnologyId(e.target.value)}
        >
          <option value="">Все технологии</option>
          {technologies.map(t => (
            <option key={t.id} value={t.id}>
              {t.medicament.medicament.name} — {t.medicament.price}₽
            </option>
          ))}
        </select>
      </div>

      <div className="table-container">
        <table className="data-table">
          <thead>
            <tr>
              <th>Технология (медикамент — цена)</th>
              <th>Компонент</th>
              <th>Количество</th>
              <th>Действия</th>
            </tr>
          </thead>
          <tbody>
            {filtered.length ? (
              filtered.map((item, idx) => (
                <tr key={idx}>
                  <td>{item.technology.medicament.medicament.name} — {item.technology.medicament.price}₽</td>
                  <td>{item.component.medicament.name} — {item.component.price}₽</td>
                  <td>{item.componentAmount}</td>
                  <td className="actions-cell">
                    <button className="btn btn-edit" onClick={() => handleEdit(item)}>Редактировать</button>
                    <button className="btn btn-delete" onClick={() => handleDelete(item)}>Удалить</button>
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

export default TechnologyComponentsSection;
