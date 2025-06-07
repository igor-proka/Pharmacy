// src/components/MixturesSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function MixturesSection() {
  const [mixtures, setMixtures] = useState([]);
  const [allPackages, setAllPackages] = useState([]);
  const [filterMedId, setFilterMedId] = useState('');
  const [form, setForm] = useState({
    medicamentPackageId: '',
    solvent: '',
    sugarFree: false,
    volumeMl: '',
  });
  const [editing, setEditing] = useState(null);
  const [loading, setLoading] = useState(true);
  const formRef = useRef(null);

  // Загружаем смеси и все пачки
  useEffect(() => {
    Promise.all([
      fetch('/api/mixtures').then(res => res.json()),
      fetch('/api/medicament-packages').then(res => res.json()),
    ])
      .then(([mixData, pkgData]) => {
        setMixtures(mixData);
        setAllPackages(pkgData);
      })
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setLoading(false));
  }, []);

  // Сбрасываем форму
  const resetForm = () => {
    setForm({
      medicamentPackageId: '',
      solvent: '',
      sugarFree: false,
      volumeMl: '',
    });
    setEditing(null);
  };

  // Обработчик сабмита
  const handleSubmit = () => {
    const pkg = mixturePackages.find(p => p.id === Number(form.medicamentPackageId));
    if (!pkg) {
      alert('Выберите корректную пачку микстуры');
      return;
    }

    const payload = {
      medicamentId: pkg.id,
      medicamentPackage: pkg,
      solvent: form.solvent,
      sugarFree: form.sugarFree,
      volumeMl: parseInt(form.volumeMl, 10),
    };

    const method = editing ? 'PUT' : 'POST';
    const url = editing ? `/api/mixtures/${editing.medicamentId}` : '/api/mixtures';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(saved => {
        setMixtures(prev => {
          if (editing) {
            return prev.map(m => (m.medicamentId === saved.medicamentId ? saved : m));
          }
          return [...prev, saved];
        });
        resetForm();
      })
      .catch(() => alert('Ошибка при сохранении'));
  };

  // Редактирование
  const handleEdit = mix => {
    setEditing(mix);
    setForm({
      medicamentPackageId: mix.medicamentPackage.id.toString(),
      solvent: mix.solvent,
      sugarFree: mix.sugarFree,
      volumeMl: mix.volumeMl.toString(),
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  // Удаление
  const handleDelete = mix => {
    if (!window.confirm('Удалить микстуру?')) return;
    fetch(`/api/mixtures/${mix.medicamentId}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        setMixtures(prev => prev.filter(m => m.medicamentId !== mix.medicamentId));
      })
      .catch(() => alert('Ошибка удаления'));
  };

  // Получаем только пачки, где тип медикамента "микстура"
  const mixturePackages = allPackages.filter(
    p => p.medicament.type && p.medicament.type.name.toLowerCase() === 'микстура'
  );

  // Собираем уникальный список медикаментов (id + name) из mixturePackages
  const uniqueMedicamentsMap = {};
  mixturePackages.forEach(p => {
    const med = p.medicament;
    uniqueMedicamentsMap[med.id] = med.name;
  });
  const uniqueMedicaments = Object.entries(uniqueMedicamentsMap).map(
    ([id, name]) => ({ id: Number(id), name })
  );

  // Фильтрация смесей по выбранному медикаменту
  const filtered = filterMedId
    ? mixtures.filter(m => m.medicamentPackage.medicament.id === Number(filterMedId))
    : mixtures;

  const getMedName = mix => mix.medicamentPackage?.medicament?.name || '—';

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Микстуры</h2>

      <div className="form-container" ref={formRef}>
        <select
          className="form-input"
          name="medicamentPackageId"
          value={form.medicamentPackageId}
          onChange={e => setForm({ ...form, medicamentPackageId: e.target.value })}
        >
          <option value="">Выберите пачку микстуры</option>
          {mixturePackages.map(p => (
            <option key={p.id} value={p.id}>
              {p.medicament.name} — {p.price}₽
            </option>
          ))}
        </select>

        <input
          type="text"
          name="solvent"
          className="form-input"
          placeholder="Растворитель"
          value={form.solvent}
          onChange={e => setForm({ ...form, solvent: e.target.value })}
        />

        <input
          type="number"
          name="volumeMl"
          className="form-input"
          placeholder="Объем (мл)"
          value={form.volumeMl}
          onChange={e => setForm({ ...form, volumeMl: e.target.value })}
        />

        <label className="checkbox-label">
          <input
            type="checkbox"
            checked={form.sugarFree}
            onChange={e => setForm({ ...form, sugarFree: e.target.checked })}
          />
          Без сахара
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

      <div className="filter-container">
        <select
          className="form-input"
          value={filterMedId}
          onChange={e => setFilterMedId(e.target.value)}
        >
          <option value="">Все медикаменты (микстуры)</option>
          {uniqueMedicaments.map(m => (
            <option key={m.id} value={m.id}>
              {m.name}
            </option>
          ))}
        </select>
      </div>

      {loading ? (
        <div className="loading-spinner" />
      ) : (
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Медикамент</th>
                <th>Растворитель</th>
                <th>Объем (мл)</th>
                <th>Без сахара</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.length ? (
                filtered.map(mix => (
                  <tr key={mix.medicamentId}>
                    <td>{getMedName(mix)}</td>
                    <td>{mix.solvent}</td>
                    <td>{mix.volumeMl}</td>
                    <td>{mix.sugarFree ? 'Да' : 'Нет'}</td>
                    <td className="actions-cell">
                      <button className="btn btn-edit" onClick={() => handleEdit(mix)}>
                        Редактировать
                      </button>
                      <button className="btn btn-delete" onClick={() => handleDelete(mix)}>
                        Удалить
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" className="no-data">
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

export default MixturesSection;
