// src/components/PrescriptionsContentSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function PrescriptionsContentSection() {
  const [contents, setContents] = useState([]);
  const [prescriptions, setPrescriptions] = useState([]);
  const [medicaments, setMedicaments] = useState([]);
  const [ways, setWays] = useState([]);
  const [filterPrescriptionId, setFilterPrescriptionId] = useState('');
  const [form, setForm] = useState({
    prescriptionId: '',
    medicamentId: '',
    amount: '',
    wayToUse: '',
  });
  const [editingItem, setEditingItem] = useState(null);
  const [loading, setLoading] = useState(true);
  const formRef = useRef(null);

  // Загрузка всех необходимых данных
  useEffect(() => {
    Promise.all([
      fetch('/api/prescriptions-content').then(r => r.json()),
      fetch('/api/prescriptions').then(r => r.json()),
      fetch('/api/medicaments').then(r => r.json()),
      fetch('/api/ways-of-use').then(r => r.json()),
    ])
      .then(([cntData, presData, medData, wayData]) => {
        setContents(cntData);
        setPrescriptions(presData);
        setMedicaments(medData);
        setWays(wayData);
      })
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setLoading(false));
  }, []);

  const resetForm = () => {
    setForm({
      prescriptionId: '',
      medicamentId: '',
      amount: '',
      wayToUse: '',
    });
    setEditingItem(null);
  };

  const makePrescriptionLabel = pres => {
    const patient = pres.patient;
    const doctor = pres.doctor;
    const disease = pres.disease;
    return `${patient.fullName} (${patient.birthday}) — ${doctor.fullName} (${doctor.position}) — ${pres.prescriptionDate} — ${disease.name}`;
  };

  const prescriptionOptions = prescriptions.map(p => ({
    id: p.id,
    label: makePrescriptionLabel(p),
  }));

  const handleFilterChange = e => {
    setFilterPrescriptionId(e.target.value);
  };

  const filtered = filterPrescriptionId
    ? contents.filter(c => c.prescription.id === Number(filterPrescriptionId))
    : contents;

  const handleFormChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = () => {
    const { prescriptionId, medicamentId, amount, wayToUse } = form;
    if (!prescriptionId || !medicamentId || !amount || !wayToUse) {
      alert('Заполните все поля');
      return;
    }
    const payload = {
      id: {
        prescriptionId: Number(prescriptionId),
        medicamentId: Number(medicamentId),
        amount: Number(amount),
        wayToUse: Number(wayToUse),
      },
      prescription: { id: Number(prescriptionId) },
      medicament: { id: Number(medicamentId) },
      wayOfUse: { id: Number(wayToUse) },
    };

    const method = editingItem ? 'PUT' : 'POST';
    const url = editingItem
      ? `/api/prescriptions-content/id?prescriptionId=${editingItem.prescription.id}&medicamentId=${editingItem.medicament.id}&amount=${editingItem.amount}&wayToUse=${editingItem.wayOfUse.id}`
      : '/api/prescriptions-content';

    fetch(url, {
      method,
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
      .then(res => (res.ok ? res.json() : Promise.reject()))
      .then(() => fetch('/api/prescriptions-content').then(r => r.json()))
      .then(all => {
        setContents(all);
        resetForm();
      })
      .catch(() => alert('Ошибка при сохранении'));
  };

  const handleEdit = item => {
    setEditingItem(item);
    setForm({
      prescriptionId: item.prescription.id.toString(),
      medicamentId: item.medicament.id.toString(),
      amount: item.amount.toString(),
      wayToUse: item.wayOfUse.id.toString(),
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  const handleDelete = item => {
    const { prescription, medicament, amount, wayOfUse } = item;
    const params = `?prescriptionId=${prescription.id}&medicamentId=${medicament.id}&amount=${amount}&wayToUse=${wayOfUse.id}`;
    if (!window.confirm('Удалить эту строку рецепта?')) return;
    fetch('/api/prescriptions-content/id' + params, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        return fetch('/api/prescriptions-content');
      })
      .then(r => r.json())
      .then(all => setContents(all))
      .catch(() => alert('Ошибка удаления'));
  };

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Содержание рецептов</h2>

      <div className="form-container" ref={formRef}>
        <select
          className="form-input"
          name="prescriptionId"
          value={form.prescriptionId}
          onChange={handleFormChange}
        >
          <option value="">Рецепт</option>
          {prescriptionOptions.map(opt => (
            <option key={opt.id} value={opt.id}>
              {opt.label}
            </option>
          ))}
        </select>

        <select
          className="form-input"
          name="medicamentId"
          value={form.medicamentId}
          onChange={handleFormChange}
        >
          <option value="">Медикамент</option>
          {medicaments.map(m => (
            <option key={m.id} value={m.id}>
              {m.name}
            </option>
          ))}
        </select>

        <input
          type="number"
          className="form-input"
          name="amount"
          placeholder="Количество"
          value={form.amount}
          onChange={handleFormChange}
        />

        <select
          className="form-input"
          name="wayToUse"
          value={form.wayToUse}
          onChange={handleFormChange}
        >
          <option value="">Способ применения</option>
          {ways.map(w => (
            <option key={w.id} value={w.id}>
              {w.description}
            </option>
          ))}
        </select>

        <div className="button-group">
          <button className="btn btn-save" onClick={handleSubmit}>
            {editingItem ? 'Сохранить' : 'Добавить'}
          </button>
          {editingItem && (
            <button className="btn btn-cancel" onClick={resetForm}>
              Отмена
            </button>
          )}
        </div>
      </div>

      <div className="form-container">
        <select
          className="form-input"
          value={filterPrescriptionId}
          onChange={handleFilterChange}
        >
          <option value="">Фильтр по рецепту</option>
          {prescriptionOptions.map(opt => (
            <option key={opt.id} value={opt.id}>
              {opt.label}
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
                <th>Рецепт</th>
                <th>Медикамент</th>
                <th>Количество</th>
                <th>Способ применения</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.length ? (
                filtered.map(item => (
                  <tr
                    key={`${item.prescription.id}-${item.medicament.id}-${item.amount}-${item.wayOfUse.id}`}
                  >
                    <td>{makePrescriptionLabel(item.prescription)}</td>
                    <td>{item.medicament.name}</td>
                    <td>{item.amount}</td>
                    <td>{item.wayOfUse.description}</td>
                    <td className="actions-cell">
                      <button className="btn btn-edit" onClick={() => handleEdit(item)}>
                        Редактировать
                      </button>
                      <button className="btn btn-delete" onClick={() => handleDelete(item)}>
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

export default PrescriptionsContentSection;
