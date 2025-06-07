// src/components/PrescriptionsSection.js
import React, { useEffect, useState, useRef } from 'react';
import './Section.css';

function PrescriptionsSection() {
  const [prescriptions, setPrescriptions] = useState([]);
  const [diseases, setDiseases] = useState([]);
  const [patients, setPatients] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [filters, setFilters] = useState({
    patientId: '',
    doctorId: '',
  });
  const [form, setForm] = useState({
    diseaseId: '',
    patientId: '',
    doctorId: '',
    prescriptionDate: '',
  });
  const [editingItem, setEditingItem] = useState(null);
  const [loading, setLoading] = useState(true);
  const formRef = useRef(null);

  // Загрузка справочников и списка рецептов
  useEffect(() => {
    Promise.all([
      fetch('/api/prescriptions').then(r => r.json()),
      fetch('/api/diseases').then(r => r.json()),
      fetch('/api/patients').then(r => r.json()),
      fetch('/api/doctors').then(r => r.json()),
    ])
      .then(([presData, disData, patData, docData]) => {
        setPrescriptions(presData);
        setDiseases(disData);
        setPatients(patData);
        setDoctors(docData);
      })
      .catch(() => alert('Ошибка загрузки данных'))
      .finally(() => setLoading(false));
  }, []);

  // Сброс формы
  const resetForm = () => {
    setForm({
      diseaseId: '',
      patientId: '',
      doctorId: '',
      prescriptionDate: '',
    });
    setEditingItem(null);
  };

  // Обработчик изменений фильтров
  const handleFilterChange = e => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  // Фильтрация списка
  const filtered = prescriptions.filter(p => {
    return (
      (!filters.patientId || p.patient.id === Number(filters.patientId)) &&
      (!filters.doctorId || p.doctor.id === Number(filters.doctorId))
    );
  });

  // Обработчик изменений формы
  const handleFormChange = e => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  // Добавление/обновление рецепта
  const handleSubmit = () => {
    const { diseaseId, patientId, doctorId, prescriptionDate } = form;
    if (!diseaseId || !patientId || !doctorId || !prescriptionDate) {
      alert('Заполните все поля');
      return;
    }
    const payload = {
      disease: { id: Number(diseaseId) },
      patient: { id: Number(patientId) },
      doctor: { id: Number(doctorId) },
      prescriptionDate,
    };

    const method = editingItem ? 'PUT' : 'POST';
    const url = editingItem
      ? `/api/prescriptions/${editingItem.id}`
      : '/api/prescriptions';

    fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })
        .then(res => {
          if (!res.ok) throw new Error();
          return res.json();
        })
        .then(() => {
          // После успешного создания — делаем новый GET всех рецептов:
          return fetch('/api/prescriptions').then(r => r.json());
        })
        .then(all => {
          setPrescriptions(all);
          resetForm();
        })
        .catch(() => alert('Ошибка при сохранении'));
  };

  // Редактирование записи
  const handleEdit = item => {
    setEditingItem(item);
    setForm({
      diseaseId: item.disease.id.toString(),
      patientId: item.patient.id.toString(),
      doctorId: item.doctor.id.toString(),
      prescriptionDate: item.prescriptionDate,
    });
    setTimeout(() => {
      const y = formRef.current?.getBoundingClientRect().top + window.scrollY - 20;
      window.scrollTo({ top: y, behavior: 'smooth' });
    }, 0);
  };

  // Удаление записи
  const handleDelete = item => {
    if (!window.confirm('Удалить рецепт?')) return;
    fetch(`/api/prescriptions/${item.id}`, { method: 'DELETE' })
      .then(res => {
        if (!res.ok) throw new Error();
        setPrescriptions(prev => prev.filter(p => p.id !== item.id));
      })
      .catch(() => alert('Ошибка удаления'));
  };

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Рецепты</h2>

      <div className="form-container" ref={formRef}>
        <select
          className="form-input"
          name="diseaseId"
          value={form.diseaseId}
          onChange={handleFormChange}
        >
          <option value="">Болезнь</option>
          {diseases.map(d => (
            <option key={d.id} value={d.id}>
              {d.name}
            </option>
          ))}
        </select>

        <select
          className="form-input"
          name="patientId"
          value={form.patientId}
          onChange={handleFormChange}
        >
          <option value="">Пациент</option>
          {patients.map(p => (
            <option key={p.id} value={p.id}>
              {p.fullName} ({p.birthday})
            </option>
          ))}
        </select>

        <select
          className="form-input"
          name="doctorId"
          value={form.doctorId}
          onChange={handleFormChange}
        >
          <option value="">Врач</option>
          {doctors.map(d => (
            <option key={d.id} value={d.id}>
              {d.fullName} ({d.position})
            </option>
          ))}
        </select>

        <input
          type="date"
          className="form-input"
          name="prescriptionDate"
          value={form.prescriptionDate}
          onChange={handleFormChange}
        />

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
          name="patientId"
          value={filters.patientId}
          onChange={handleFilterChange}
        >
          <option value="">Фильтр по пациенту</option>
          {patients.map(p => (
            <option key={p.id} value={p.id}>
              {p.fullName} ({p.birthday})
            </option>
          ))}
        </select>

        <select
          className="form-input"
          name="doctorId"
          value={filters.doctorId}
          onChange={handleFilterChange}
        >
          <option value="">Фильтр по врачу</option>
          {doctors.map(d => (
            <option key={d.id} value={d.id}>
              {d.fullName} ({d.position})
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
                <th>Пациент</th>
                <th>Врач</th>
                <th>Болезнь</th>
                <th>Дата</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {filtered.length ? (
                filtered.map(item => (
                  <tr key={item.id}>
                    <td>
                      {item.patient.fullName} ({item.patient.birthday})
                    </td>
                    <td>
                      {item.doctor.fullName} ({item.doctor.position})
                    </td>
                    <td>{item.disease.name}</td>
                    <td>{item.prescriptionDate}</td>
                    <td className="actions-cell">
                      <button
                        className="btn btn-edit"
                        onClick={() => handleEdit(item)}
                      >
                        Редактировать
                      </button>
                      <button
                        className="btn btn-delete"
                        onClick={() => handleDelete(item)}
                      >
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

export default PrescriptionsSection;
