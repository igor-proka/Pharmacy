import React, { useEffect, useState } from 'react';
import './Section.css'; // Переиспользуем стили

function MedicamentTypesSection() {
  const [data, setData] = useState([]);
  const [newName, setNewName] = useState('');
  const [editingItem, setEditingItem] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    setIsLoading(true);
    fetch('/api/medicament-types')
      .then((res) => res.json())
      .then(setData)
      .catch(console.error)
      .finally(() => setIsLoading(false));
  }, []);

  const handleAdd = () => {
    if (!newName.trim()) {
      alert('Название не может быть пустым.');
      return;
    }

    if (data.some(item => item.name.toLowerCase() === newName.trim().toLowerCase())) {
      alert('Такой тип уже существует.');
      return;
    }

    fetch('/api/medicament-types', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: newName }),
    })
      .then(async (res) => {
        if (!res.ok) {
          const errorText = await res.text();
          throw new Error(errorText || 'Ошибка добавления');
        }
        return res.json();
      })
      .then((created) => {
        setData([...data, created]);
        setNewName('');
      })
      .catch((error) => {
        alert('Ошибка при добавлении: ' + error.message);
      });
  };

  const handleDelete = (id) => {
    if (!window.confirm('Удалить этот тип лекарства?')) return;

    fetch(`/api/medicament-types/${id}`, {
      method: 'DELETE',
    }).then(() => {
      setData(data.filter((item) => item.id !== id));
    });
  };

  const handleEdit = (item) => {
    setEditingItem(item);
    setNewName(item.name);
  };

  const handleUpdate = () => {
    if (!newName.trim()) {
      alert('Название не может быть пустым.');
      return;
    }

    if (
      data.some(
        (item) =>
          item.id !== editingItem.id &&
          item.name.toLowerCase() === newName.trim().toLowerCase()
      )
    ) {
      alert('Такой тип уже существует.');
      return;
    }

    fetch(`/api/medicament-types/${editingItem.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: newName }),
    })
      .then(async (res) => {
        if (!res.ok) {
          const errorText = await res.text();
          throw new Error(errorText || 'Ошибка при обновлении');
        }
        return res.json();
      })
      .then((updated) => {
        setData(data.map((item) => (item.id === updated.id ? updated : item)));
        setEditingItem(null);
        setNewName('');
      })
      .catch((error) => {
        alert('Ошибка при обновлении: ' + error.message);
      });
  };

  const handleCancel = () => {
    setEditingItem(null);
    setNewName('');
  };

  return (
    <div className="ways-of-use-container">
      <h2 className="section-title">Типы лекарств</h2>

      <div className="form-container">
        <input
          type="text"
          value={newName}
          onChange={(e) => setNewName(e.target.value)}
          placeholder="Введите название типа..."
          className="form-input"
        />
        <div className="button-group">
          {editingItem ? (
            <>
              <button className="btn btn-save" onClick={handleUpdate}>
                Сохранить
              </button>
              <button className="btn btn-cancel" onClick={handleCancel}>
                Отмена
              </button>
            </>
          ) : (
            <button className="btn btn-add" onClick={handleAdd}>
              Добавить
            </button>
          )}
        </div>
      </div>

      {isLoading ? (
        <div className="loading-spinner"></div>
      ) : (
        <div className="table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Название</th>
                <th>Действия</th>
              </tr>
            </thead>
            <tbody>
              {data.length > 0 ? (
                data.map((item) => (
                  <tr key={item.id}>
                    <td>{item.name}</td>
                    <td className="actions-cell">
                      <button
                        className="btn btn-edit"
                        onClick={() => handleEdit(item)}
                      >
                        Редактировать
                      </button>
                      <button
                        className="btn btn-delete"
                        onClick={() => handleDelete(item.id)}
                      >
                        Удалить
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="2" className="no-data">
                    Нет данных для отображения
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

export default MedicamentTypesSection;
